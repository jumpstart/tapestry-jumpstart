package jumpstart.web.pages.theapp;

import javax.ejb.EJB;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.base.theapp.SimpleBasePage;
import jumpstart.web.commons.IIntermediatePage;
import jumpstart.web.pages.Index;
import jumpstart.web.pages.theapp.general.Welcome;
import jumpstart.web.state.theapp.Visit;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

// To make this page accessible only by HTTPS, annotate it with @Secure and ensure your web server can deliver HTTPS.
// See http://tapestry.apache.org/secure.html .
// @Secure
public class Login extends SimpleBasePage implements IIntermediatePage {

	// The activation context

	@Property
	private String loginId;

	// Screen fields

	@Property
	private String password;

	// Generally useful bits and pieces

	@Persist
	private Link nextPageLink;

	@Component(id = "login")
	private Form form;

	@Component(id = "loginId")
	private TextField loginIdField;

	@Inject
	private Logger logger;
	
	@Inject
	private ComponentResources componentResources;
	
	@EJB
	private ISecurityFinderServiceLocal securityFinderService;

	// The code
	
	@Override
	public void setNextPageLink(Link nextPageLink) {
		this.nextPageLink = nextPageLink;
	}

	String onPassivate() {
		return loginId;
	}

	void onActivate(String loginId) {
		this.loginId = loginId;
	}

	void onValidateFromLogin() {

		if (form.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		try {
			// Authenticate the user

			User user = securityFinderService.authenticateUser(loginId, password);

			// Store the user in the Visit

			setVisit(new Visit(user));
			logger.info(user.getLoginId() + " has logged in.");
		}
		catch (BusinessException e) {
			form.recordError(loginIdField, e.getLocalizedMessage());
		}
		catch (Exception e) {
			logger.error("Could not log in.  Stack trace follows...");
			logger.error(ExceptionUtil.printStackTrace(e));
			form.recordError(getMessages().get("login_problem"));
		}
	}

	Object onSuccess() {
		
		if (nextPageLink == null) {
			return Welcome.class;
		}
		else {
			componentResources.discardPersistentFieldChanges();
			return nextPageLink;
		}

	}
	
	Object onGoHome() {
		componentResources.discardPersistentFieldChanges();
		return Index.class;
	}

}
