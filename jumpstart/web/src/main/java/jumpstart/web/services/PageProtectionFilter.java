// Based on http://tapestryjava.blogspot.com/2009/12/securing-tapestry-pages-with.html

package jumpstart.web.services;

import java.io.IOException;
import java.io.OutputStream;

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
 * ProtectedPage and the user is not logged in.
 * <p>
 * To use this filter, contribute it to Tapestry's ComponentRequestHandler service as we do in AppModule.
 * 
 */
public class PageProtectionFilter implements ComponentRequestFilter {
	private final String autoLoginStr = System.getProperty("jumpstart.auto-login");

	private final PageRenderLinkSource pageRenderLinkSource;
	private final ComponentSource componentSource;
	private final Request request;
	private final Response response;
	private ApplicationStateManager sessionStateManager;
	private final Logger logger;
	private IBusinessServicesLocator businessServicesLocator;

	/**
	 * Receive all the services needed as constructor arguments. When we bind this service, T5 IoC will provide all the
	 * services!
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

	public void handlePageRender(PageRenderRequestParameters parameters, ComponentRequestHandler handler)
			throws IOException {

		if (isAuthorisedToPage(parameters.getLogicalPageName(), parameters.getActivationContext())) {
			handler.handlePageRender(parameters);
		}
		else {
			// The method will have redirected us to the Login page or the PageDenied page
			return;
		}

	}

	public void handleComponentEvent(ComponentEventRequestParameters parameters, ComponentRequestHandler handler)
			throws IOException {

		if (isAuthorisedToPage(parameters.getActivePageName(), parameters.getEventContext())) {
			handler.handleComponentEvent(parameters);
		}
		else {
			// The method will have redirected us to the Login page or the PageDenied page
			return;
		}

	}

	public boolean isAuthorisedToPage(String requestedPageName, EventContext eventContext) throws IOException {

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
			return true;
		}

		// Else if request is AJAX with no session, return an AJAX response that forces reload of the page

		else if (request.isXHR() && request.getSession(false) == null) {
			OutputStream os = response.getOutputStream("application/json;charset=UTF-8");
			os.write("{\"script\":\"window.location.reload();\"}".getBytes());
			os.flush();
			return false;
		}

		// Else if user has already been authenticated (ie. already logged in)...

		else if (isAuthenticated()) {

			// If user is authorised to the page, then all is well so allow access

			if (isAuthorised(rolesAllowed)) {
				return true;
			}

			// Else, redirect to the PageDenied page

			else {
				Link pageProtectedLink = pageRenderLinkSource.createPageRenderLinkWithContext(PageDenied.class,
						requestedPageName);
				response.sendRedirect(pageProtectedLink);
				return false;
			}
		}

		// Else... go to the Login page

		else {

			// Get the Login page, give it a link to the requested page, and redirect to Login

			IIntermediatePage loginPage = (IIntermediatePage) componentSource.getPage(Login.class);

			Link requestedPageLink = makeLinkToRequestedPage(requestedPageName, eventContext);
			loginPage.setNextPageLink(requestedPageLink);

			Link loginPageLink = pageRenderLinkSource.createPageRenderLink(Login.class);
			response.sendRedirect(loginPageLink);

			return false;
		}

	}

	private Link makeLinkToRequestedPage(String requestedPageName, EventContext eventContext) {
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

	private ISecurityFinderServiceLocal getSecurityFinderService() {
		return (ISecurityFinderServiceLocal) businessServicesLocator.getService(ISecurityFinderServiceLocal.class);
	}
}
