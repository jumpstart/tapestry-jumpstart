package jumpstart.web.pages.theapp.security;

import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.domain.security.Role;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

@ProtectedPage
public class RoleSearch extends SimpleBasePage {

	// Screen fields

	@Property
	private List<Role> roles;

	@Property
	private Role role;

	// Generally useful bits and pieces

	@InjectComponent
	private Form form;

	@EJB
	private ISecurityFinderServiceLocal securityFinderService;

	@EJB
	private ISecurityManagerServiceLocal securityManagerService;

	// The code

	void setupRender() {
		roles = securityFinderService.findRolesShallowish();
	}

	Object onNew() {
		return RoleCreate.class;
	}

	void onDelete(Long id, Integer version) {

		if (form.isValid()) {

			// Delete the user from the database unless they've been modified elsewhere

			try {
				Role role = securityFinderService.findRole(id);
				if (!role.getVersion().equals(version)) {
					form.recordError("Cannot delete role because has been updated or deleted since last displayed.  Please refresh and try again.");
				}
				else {
					securityManagerService.deleteRole(role);
				}
			}
			catch (Exception e) {
				form.recordError(interpretBusinessServicesExceptionForDelete(e));
			}
		}
	}

}
