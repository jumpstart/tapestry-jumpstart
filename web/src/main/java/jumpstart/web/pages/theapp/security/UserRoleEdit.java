package jumpstart.web.pages.theapp.security;

import javax.ejb.EJB;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.UserRole;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

@ProtectedPage
public class UserRoleEdit extends SimpleBasePage {

	// Screen fields

	@Property
	private Long userRoleId;

	@Persist
	private Link returnTo;

	@Property
	private UserRole userRole;

	// Work fields

	// This carries version through the redirect that follows a server-side validation failure.
	@Persist(PersistenceConstants.FLASH)
	private Integer versionFlash;

	// Generally useful bits and pieces

	@InjectComponent
	private Form form;

	@EJB
	private ISecurityFinderServiceLocal securityFinderService;

	@EJB
	private ISecurityManagerServiceLocal securityManagerService;

	// The code

	public void set(Long userRoleId, Link returnTo) {
		this.userRoleId = userRoleId;
		this.returnTo = returnTo;
	}

	void onActivate(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	Long onPassivate() {
		return userRoleId;
	}

	void onPrepareForRender() throws Exception {
		userRole = findUserRole(userRoleId);
		// Handle null person in the template.

		// If the form has errors then we're redisplaying after a redirect.
		// Form will restore your input values but it's up to us to restore Hidden values.

		if (form.getHasErrors()) {
			if (userRole != null) {
				userRole.setVersion(versionFlash);
			}
		}
	}

	void onPrepareForSubmit() throws DoesNotExistException {
		userRole = findUserRole(userRoleId);

		if (userRole == null) {
			userRole = new UserRole();
			form.recordError("User role has been deleted by another process.");
		}
	}

	private UserRole findUserRole(Long userRoleId) {
		try {
			return securityFinderService.findUserRoleShallowish(userRoleId);
		}
		catch (DoesNotExistException e) {
			// Handle null role in the template
			return null;
		}
	}

	void onValidateFromForm() {
		try {
			securityManagerService.changeUserRole(userRole);
		}
		catch (Exception e) {
			form.recordError(interpretBusinessServicesExceptionForChange(e));
		}
	}

	Link onSuccess() {
		return returnTo;
	}

	void onFailure() {
		versionFlash = userRole.getVersion();
	}

	void onRefresh() {
	}

	Link onCancel() {
		return returnTo;
	}
}
