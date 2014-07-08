package jumpstart.web.pages.theapp.security;

import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.Role;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.UserRole;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;
import jumpstart.web.models.app.select.RoleIdSelectModel;
import jumpstart.web.models.app.select.UserIdSelectModel;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

@ProtectedPage
public class UserRoleAdd extends SimpleBasePage {

	// The activation context

	private Long userId;

	private Long roleId;

	// A link back to the caller - there are many possible callers so each caller must provide this.

	@Persist
	private Link returnTo;

	// The User and/or Role specified by the caller

	@Property
	private User user;

	@Property
	private Role role;

	// If the User and/or Role were not specified by the caller then we'll need them in lists for selection.

	@Property
	private SelectModel userIdsModel;

	@Property
	private Long selectedUserId;

	@Property
	private SelectModel roleIdsModel;

	@Property
	private Long selectedRoleId;

	// The UserRole we're trying to add

	@Property
	private UserRole userRole;

	// Useful bits and pieces

	@InjectComponent
	private Form form;

	@EJB
	private ISecurityFinderServiceLocal securityFinderService;

	@EJB
	private ISecurityManagerServiceLocal securityManagerService;

	// The code

	public void set(Long userId, Long roleId, Link returnTo) {
		this.userId = userId;
		this.roleId = roleId;
		this.returnTo = returnTo;
	}

	void onActivate(Long userId, Long roleId) throws DoesNotExistException {
		this.userId = userId;
		this.roleId = roleId;
	}

	Long[] onPassivate() {
		return new Long[] { userId, roleId };
	}

	void setupRender() throws DoesNotExistException {

		// If user id not given, put a selection list of users on the page

		if (userId == null) {
			List<User> users = securityFinderService.findUsersShallowish();
			userIdsModel = new UserIdSelectModel(users);
		}

		// If role id not given, put a selection list of roles on the page

		if (roleId == null) {
			List<Role> roles = securityFinderService.findRolesShallowish();
			roleIdsModel = new RoleIdSelectModel(roles);
		}
	}

	void onPrepare() throws DoesNotExistException {

		// Instantiate a UserRole for the form data to overlay.

		userRole = new UserRole();
		userRole.setActive(true);

		// If user and/or role specified then get them.

		if (userId != null) {
			user = securityFinderService.findUser(userId);
		}

		if (roleId != null) {
			role = securityFinderService.findRole(roleId);
		}
	}

	void onValidateFromForm() {

		if (form.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		try {
			Long userId = this.userId != null ? this.userId : selectedUserId;
			Long roleId = this.roleId != null ? this.roleId : selectedRoleId;

			securityManagerService.addUserRole(userId, roleId, userRole);
		}
		catch (Exception e) {
			form.recordError(interpretBusinessServicesExceptionForCreate(e));
			return;
		}
	}

	Object onSuccess() {
		return returnTo;
	}

	void onReset() {
	}

	Object onCancel() {
		return returnTo;
	}
}
