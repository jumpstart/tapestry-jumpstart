// Based on http://tapestryjava.blogspot.com/2009/12/securing-tapestry-pages-with.html

package jumpstart.web.services;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.client.BusinessServicesLocator;
import jumpstart.client.IBusinessServicesLocator;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.commons.IIntermediatePage;
import jumpstart.web.pages.infra.PageDenied;
import jumpstart.web.pages.theapp.Login;
import jumpstart.web.state.theapp.Visit;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.internal.EmptyEventContext;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;

/**
 * A service that protects pages annotated with {@link jumpstart.web.annotation.ProtectedPage}. It examines each
 * {@link org.apache.tapestry5.services.Request} and redirects it to the login page if the request is for a
 * ProtectedPage and the user is not logged in. If the page also has the {@link javax.annotation.security.RolesAllowed}
 * annotation then the user must belong to one of the listed roles.
 * <p>
 * To use this filter, contribute it to Tapestry's ComponentRequestHandler service as we do in AppModule.
 * 
 */
public class PageProtectionFilter implements ComponentRequestFilter {
	private static final String COMPONENT_PARAM_PREFIX = "t:";

	private final String autoLoginStr = System.getProperty("jumpstart.auto-login");

	private enum AuthCheckResult {
		AUTHORISED, DENIED, RELOAD_XHR, AUTHENTICATE;
	}

	private final PageRenderLinkSource pageRenderLinkSource;
	private final ComponentSource componentSource;
	private final Request request;
	private final Response response;
	private ApplicationStateManager sessionStateManager;
	private final Logger logger;
	private IBusinessServicesLocator businessServicesLocator;

	/**
	 * Receive all the services needed as constructor arguments. When we bind this service, T5 IoC will provide all the
	 * services.
	 */
	public PageProtectionFilter(PageRenderLinkSource pageRenderLinkSource, ComponentSource componentSource,
			Request request, Response response, ApplicationStateManager asm, Logger logger) {
		this.pageRenderLinkSource = pageRenderLinkSource;
		this.request = request;
		this.response = response;
		this.componentSource = componentSource;
		this.sessionStateManager = asm;
		this.logger = logger;
		this.businessServicesLocator = null;
	}

	@Override
	public void handlePageRender(PageRenderRequestParameters parameters, ComponentRequestHandler handler)
			throws IOException {

		AuthCheckResult result = checkAuthorityToPage(parameters.getLogicalPageName());

		if (result == AuthCheckResult.AUTHORISED) {
			handler.handlePageRender(parameters);
		}
		else if (result == AuthCheckResult.DENIED) {
			// The method will have set the response to redirect to the PageDenied page.
			return;
		}
		else if (result == AuthCheckResult.AUTHENTICATE) {

			// Redirect to the Login page, with memory of the request.

			Link requestedPageLink = createLinkToRequestedPage(parameters.getLogicalPageName(),
					parameters.getActivationContext());
			Link loginPageLink = createLoginPageLinkWithMemory(requestedPageLink);

			response.sendRedirect(loginPageLink);
		}
		else {
			throw new IllegalStateException(result.toString());
		}

	}

	@Override
	public void handleComponentEvent(ComponentEventRequestParameters parameters, ComponentRequestHandler handler)
			throws IOException {

		AuthCheckResult result = checkAuthorityToPage(parameters.getActivePageName());

		if (result == AuthCheckResult.AUTHORISED) {
			handler.handleComponentEvent(parameters);
		}
		else if (result == AuthCheckResult.DENIED) {
			// The method will have set the response to redirect to the PageDenied page.
			return;
		}
		else if (result == AuthCheckResult.RELOAD_XHR) {
			
			// Return an AJAX response that reloads the page.
			
			Link requestedPageLink = createLinkToRequestedPage(parameters.getActivePageName(),
					parameters.getPageActivationContext());
			OutputStream os = response.getOutputStream("application/json;charset=UTF-8");
			os.write(("{\"redirectURL\":\"" + requestedPageLink.toAbsoluteURI() + "\"}").getBytes());
			os.close();
			return;
		}
		else if (result == AuthCheckResult.AUTHENTICATE) {

			// Redirect to the Login page, with memory of the request.

			Link requestedPageLink = createLinkToRequestedPage(parameters.getActivePageName(),
					parameters.getPageActivationContext());
			Link loginPageLink = createLoginPageLinkWithMemory(requestedPageLink);

			response.sendRedirect(loginPageLink);
		}
		else {
			throw new IllegalStateException(result.toString());
		}

	}

	public AuthCheckResult checkAuthorityToPage(String requestedPageName) throws IOException {

		// Does the page have security annotations @ProtectedPage or @RolesAllowed?

		Component page = componentSource.getPage(requestedPageName);
		boolean protectedPage = page.getClass().getAnnotation(ProtectedPage.class) != null;
		RolesAllowed rolesAllowed = page.getClass().getAnnotation(RolesAllowed.class);

		// If the security annotations on the page conflict in meaning, then error

		if (rolesAllowed != null && !protectedPage) {
			throw new IllegalStateException("Page \"" + requestedPageName
					+ "\" is annotated with @RolesAllowed but not @ProtectedPage.");
		}

		// If page is public (ie. not protected), then everyone is authorised to it so allow access

		if (!protectedPage) {
			return AuthCheckResult.AUTHORISED;
		}

		// If request is AJAX with no session, return an AJAX response that forces reload of the page

		if (request.isXHR() && request.getSession(false) == null) {
			return AuthCheckResult.RELOAD_XHR;
		}

		// If user has not been authenticated, disallow.

		if (!isAuthenticated()) {
			return AuthCheckResult.AUTHENTICATE;
		}

		// If user is authorised to the page, then all is well.

		if (isAuthorised(rolesAllowed)) {
			return AuthCheckResult.AUTHORISED;
		}

		// Fell through, so redirect to the PageDenied page.

		Link pageProtectedLink = pageRenderLinkSource.createPageRenderLinkWithContext(PageDenied.class,
				requestedPageName);
		response.sendRedirect(pageProtectedLink);
		return AuthCheckResult.DENIED;

	}

	private Link createLinkToRequestedPage(String requestedPageName, EventContext eventContext) {

		// Create a link to the page you wanted.

		Link linkToRequestedPage;

		if (eventContext instanceof EmptyEventContext) {
			linkToRequestedPage = pageRenderLinkSource.createPageRenderLink(requestedPageName);
		}
		else {
			Object[] args = new String[eventContext.getCount()];
			for (int i = 0; i < eventContext.getCount(); i++) {
				args[i] = eventContext.get(String.class, i);
			}
			linkToRequestedPage = pageRenderLinkSource.createPageRenderLinkWithContext(requestedPageName, args);
		}

		// Add any activation request parameters (AKA query parameters).

		List<String> parameterNames = request.getParameterNames();

		for (String parameterName : parameterNames) {
			linkToRequestedPage.removeParameter(parameterName);
			if (!parameterName.startsWith(COMPONENT_PARAM_PREFIX)) {
				linkToRequestedPage.addParameter(parameterName, request.getParameter(parameterName));
			}
		}

		return linkToRequestedPage;
	}

	private boolean isAuthenticated() throws IOException {

		// If a Visit already exists in the session then you have already been authenticated

		if (sessionStateManager.exists(Visit.class)) {
			return true;
		}

		// Else if "auto-login" is on, try auto-logging in.
		// - this facility is for development environment only. It avoids getting you thrown out of the
		// app every time the session clears eg. when app is restarted.

		else {
			if (isAutoLoginOn()) {
				autoLogin(1L);
				return true;
			}
		}

		return false;
	}

	private boolean isAuthorised(RolesAllowed rolesAllowed) throws IOException {
		boolean authorised = false;

		if (rolesAllowed == null) {
			authorised = true;
		}
		else {
			// Here we could check whether the user's role, or perhaps roles, include one of the rolesAllowed.
			// Typically we'd cache the user's roles in the Visit.
		}

		return authorised;
	}

	/**
	 * Checks the value of system property jumpstart.auto-login. If "true" then returns true; if "false" then return
	 * false; if not set then returns false.
	 */
	private boolean isAutoLoginOn() {
		boolean autoLogin = false;
		if (autoLoginStr == null) {
			autoLogin = false;
		}
		else if (autoLoginStr.equalsIgnoreCase("true")) {
			autoLogin = true;
		}
		else if (autoLoginStr.equalsIgnoreCase("false")) {
			autoLogin = false;
		}
		else {
			throw new IllegalStateException(
					"System property jumpstart.auto-login has been set to \""
							+ autoLoginStr
							+ "\".  Please set it to \"true\" or \"false\".  If not specified at all then it will default to \"false\".");
		}
		return autoLogin;
	}

	/**
	 * Automatically logs you in as the given user. Intended for use in development environment only.
	 */
	private void autoLogin(Long userId) {

		// Lazy-load the business services locator because it is only needed for auto-login

		if (businessServicesLocator == null) {
			businessServicesLocator = new BusinessServicesLocator(logger);
		}

		try {
			User user = getSecurityFinderService().findUser(userId);

			Visit visit = new Visit(user);
			logger.info(user.getLoginId() + " has been auto-logged-in.");

			sessionStateManager.set(Visit.class, visit);
		}
		catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private Link createLoginPageLinkWithMemory(Link requestedPageLink) {

		IIntermediatePage loginPage = (IIntermediatePage) componentSource.getPage(Login.class);
		loginPage.setNextPageLink(requestedPageLink);
		Link loginPageLink = pageRenderLinkSource.createPageRenderLink(Login.class);

		return loginPageLink;
	}

	private ISecurityFinderServiceLocal getSecurityFinderService() {
		return (ISecurityFinderServiceLocal) businessServicesLocator.getService(ISecurityFinderServiceLocal.class);
	}
}
