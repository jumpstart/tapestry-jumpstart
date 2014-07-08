package jumpstart.web.pages.theapp.security;

import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.Role;
import jumpstart.business.domain.security.UserRole;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

@ProtectedPage
public class RoleEdit extends SimpleBasePage {

	// Activation context

	@Property
	private Long roleId;

	// Screen fields

	@Property
	private Role role;

	@Property
	private List<UserRole> userRoles;

	@Property
	private UserRole userRole;

	// Work fields

	// This carries version through the redirect that follows a server-side validation failure.
	@Persist(PersistenceConstants.FLASH)
	private Integer versionFlash;

	// Other pages

	@InjectPage
	private UserRoleAdd addPage;

	@InjectPage
	private UserRoleView viewPage;

	@InjectPage
	private UserRoleEdit editPage;

	// Generally useful bits and pieces

	@InjectComponent
	private Form form;

	@Inject
	private PageRenderLinkSource pageRenderLinkSource;

	@EJB
	private ISecurityFinderServiceLocal securityFinderService;

	@EJB
	private ISecurityManagerServiceLocal securityManagerService;

	// The code

	public void set(Long roleId) {
		this.roleId = roleId;
	}

	void onActivate(Long roleId) {
		this.roleId = roleId;
	}

	Long onPassivate() {
		return roleId;
	}

	void setupRender() {
		userRoles = securityFinderService.findUserRolesShallowishByRole(roleId);
	}

	void onPrepareForRender() throws Exception {
		role = findRole(roleId);
		// Handle null person in the template.

		// If the form has errors then we're redisplaying after a redirect.
		// Form will restore your input values but it's up to us to restore Hidden values.

		if (form.getHasErrors()) {
			if (role != null) {
				role.setVersion(versionFlash);
			}
		}
	}

	void onPrepareForSubmit() {
		role = findRole(roleId);

		if (role == null) {
			role = new Role();
			form.recordError("Role has been deleted by another process.");
		}
	}

	private Role findRole(Long roleId) {
		try {
			return securityFinderService.findRole(roleId);
		}
		catch (DoesNotExistException e) {
			// Handle null role in the template
			return null;
		}
	}

	void onValidateFromForm() {

		if (form.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		try {
			securityManagerService.changeRole(role);
		}
		catch (Exception e) {
			form.recordError(interpretBusinessServicesExceptionForChange(e));
		}
	}

	Object onSuccess() {
		return RoleSearch.class;
	}

	void onFailure() {
		versionFlash = role.getVersion();
	}

	void onRefresh() {
	}

	Object onCancel() {
		return RoleSearch.class;
	}

	Object onAddUserRole() {
		addPage.set(null, roleId, createLinkToThisPage());
		return addPage;
	}

	Object onViewUserRole(Long id) {
		viewPage.set(id, createLinkToThisPage());
		return viewPage;
	}

	Object onEditUserRole(Long id) {
		editPage.set(id, createLinkToThisPage());
		return editPage;
	}

	void onRemoveUserRole(Long id, Integer version) {

		if (form.isValid()) {

			// Delete the user from the database unless they've been modified elsewhere

			try {
				UserRole userRole = securityFinderService.findUserRole(id);
				if (!userRole.getVersion().equals(version)) {
					form.recordError("Cannot remove user role because has been updated or deleted since last displayed.  Please refresh and try again.");
					return;
				}
				securityManagerService.removeUserRole(userRole);
			}
			catch (Exception e) {
				form.recordError(interpretBusinessServicesExceptionForRemove(e));
				return;
			}
		}
	}

	private Link createLinkToThisPage() {
		Link thisPageLink = pageRenderLinkSource.createPageRenderLinkWithContext(this.getClass(), onPassivate());
		return thisPageLink;
	}

	public boolean isRoleExists() {
		// We get the role in onPrepareFromForm, so this test will work correctly ONLY if it is in or below the Form on
		// the page.
		return role != null;
	}
}
