package jumpstart.web.pages.theapp.security;

import javax.ejb.EJB;

import jumpstart.business.domain.security.Role;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

@ProtectedPage
public class RoleCreate extends SimpleBasePage {

	// Screen fields

	@Property
	private Role role;

	// Generally useful bits and pieces

	@InjectComponent
	private Form form;

	@EJB
	ISecurityManagerServiceLocal securityManagerService;

	// The code

	void onPrepare() {
		// Instantiate a Role for the form data to overlay.
		role = new Role();
	}

	void onValidateFromForm() {

		if (form.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		try {
			securityManagerService.createRole(role);
		}
		catch (Exception e) {
			form.recordError(interpretBusinessServicesExceptionForCreate(e));
			return;
		}
	}

	Object onSuccess() {
		return RoleSearch.class;
	}

	void onReset() {
	}

	Object onCancel() {
		return RoleSearch.class;
	}
}
