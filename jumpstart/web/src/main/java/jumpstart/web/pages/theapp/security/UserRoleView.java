package jumpstart.web.pages.theapp.security;

import javax.ejb.EJB;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.UserRole;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

@ProtectedPage
public class UserRoleView extends SimpleBasePage {

	// Activation context

	@Property
	private Long userRoleId;
	
	// Screen fields

	@Property
	private UserRole userRole;
	
	// Generally useful bits and pieces
	
	@Persist
	private Link returnTo;
	
	@EJB
	private ISecurityFinderServiceLocal securityFinderService;

	// The code
	
	public void set(Long userRoleId, Link returnTo) {
		this.userRoleId = userRoleId;
		this.returnTo = returnTo;
	}
	
	Long onPassivate() {
		return userRoleId;
	}

	void onActivate(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	void setupRender() throws DoesNotExistException {
		userRole = securityFinderService.findUserRoleShallowish(userRoleId);
	}
	
	void cleanupRender() {
		userRole = null;
	}

	Link onCancel() {
		return returnTo;
	}
}
