package jumpstart.web.pages.theapp.security;

import javax.ejb.EJB;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;

@ProtectedPage
public class UserPasswordChange extends SimpleBasePage {

	// Screen fields
	
	@Property
	private Long userId;

	@Property
	private User user;

	@Property
	private String newPassword;

	@Property
	private String confirmNewPassword;

	// Generally useful bits and pieces
	
	@Component(id = "newPassword")
	private PasswordField newPasswordField;

	@InjectPage
	private UserEdit userEdit;

	@Component(id = "form")
	private Form form;
	
	@EJB
	private ISecurityFinderServiceLocal securityFinderService;
	
	@EJB
	private ISecurityManagerServiceLocal securityManagerService;

	// The code
	
	public void set(Long userId) {
		this.userId = userId;
	}

	void onActivate(Long userId) {
		this.userId = userId;
	}

	Long onPassivate() {
		return userId;
	}

	void onPrepareForRender() throws DoesNotExistException {
		user = securityFinderService.findUser(userId);
	}

	void onPrepareForSubmit() throws DoesNotExistException {
		user = securityFinderService.findUser(userId);
		
		if (user == null) {
			user = new User();
			form.recordError("User has been deleted by another process.");
		}
	}

	void onValidateFromForm() {

		if (form.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		if (newPassword != null && confirmNewPassword != null && !newPassword.equals(confirmNewPassword)) {
			form.recordError(newPasswordField, getMessages().get("User_confirmnewpassword_does_not_match"));
			return;
		}

		try {
			securityManagerService.changeUserPassword(userId, newPassword);
			userEdit.set(user.getId());
		}
		catch (Exception e) {
			form.recordError(interpretBusinessServicesExceptionForChange(e));
		}
	}

	Object onSuccess() {
		return userEdit;
	}

	Object onCancel() {
		userEdit.set(userId);
		return userEdit;
	}

}
