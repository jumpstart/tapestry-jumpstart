package jumpstart.web.pages.theapp.security;

import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.UserRole;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

@ProtectedPage
public class MyAccountEdit extends SimpleBasePage {

	// Screen fields

	@Property
	private User user;

	@Property
	private List<UserRole> userRoles;

	@Property
	private UserRole userRole;

	// Work fields

	// This carries version through the redirect that follows a server-side validation failure.
	@Persist(PersistenceConstants.FLASH)
	private Integer versionFlash;

	// Generally useful bits and pieces

	@Component(id = "form")
	private Form form;

	@EJB
	private ISecurityFinderServiceLocal securityFinderService;

	@EJB
	private ISecurityManagerServiceLocal securityManagerService;

	// The code

	void onPrepareForRender() throws DoesNotExistException {
		Long userId = getVisit().getMyUserId();
		user = securityFinderService.findUser(userId);

		// If the form has errors then we're redisplaying after a redirect.
		// Form will restore your input values but it's up to us to restore Hidden values.

		if (form.getHasErrors()) {
			user.setVersion(versionFlash);
		}
	}

	void onPrepareForSubmit() throws DoesNotExistException {
		Long userId = getVisit().getMyUserId();
		user = securityFinderService.findUser(userId);
		
		if (user == null) {
			user = new User();
			form.recordError("Your account has been deleted by another process.");
		}
	}

	void onValidateFromForm() {

		if (form.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		try {
			securityManagerService.changeUser(user);
		}
		catch (Exception e) {
			form.recordError(interpretBusinessServicesExceptionForChange(e));
		}
	}

	Object onSuccess() {
		return MyAccountView.class;
	}

	void onFailure() {
		versionFlash = user.getVersion();
	}

	Object onCancel() {
		return MyAccountView.class;
	}

	public String[] getSalutations() {
		return User.SALUTATIONS;
	}
}
