package jumpstart.web.pages.theapp.security;

import javax.ejb.EJB;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;

@ProtectedPage
public class MyPasswordChange extends SimpleBasePage {

	// Screen fields

	@Property
	private User user;

	@Property
	private String currentPassword;

	@Property
	private String newPassword;

	@Property
	private String confirmNewPassword;

	// Generally useful bits and pieces

	@InjectComponent("newPassword")
	private PasswordField newPasswordField;

	@InjectComponent
	private Form form;

	@EJB
	private ISecurityFinderServiceLocal securityFinderService;

	@EJB
	private ISecurityManagerServiceLocal securityManagerService;

	// The code

	void onPrepare() throws DoesNotExistException {
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

		if (newPassword != null && newPassword.equals(currentPassword)) {
			form.recordError(newPasswordField, getMessages().get("User_newpassword_same_as_current"));
			return;
		}

		if (newPassword != null && confirmNewPassword != null && !newPassword.equals(confirmNewPassword)) {
			form.recordError(newPasswordField, getMessages().get("User_confirmnewpassword_does_not_match"));
			return;
		}

		try {
			Long userId = getVisit().getMyUserId();
			securityManagerService.changeUserPassword(userId, currentPassword, newPassword);
		}
		catch (Exception e) {
			form.recordError(interpretBusinessServicesExceptionForChange(e));
		}
	}

	Object onSuccess() {
		return MyAccountView.class;
	}

	Object onCancel() {
		return MyAccountView.class;
	}

}
