package jumpstart.web.pages.theapp.security;

import javax.ejb.EJB;

import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.User.PageStyle;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.util.EnumValueEncoder;

@ProtectedPage
public class UserCreate extends SimpleBasePage {

	// Screen fields

	@Property
	private User user;

	@Property
	private String password;

	@Property
	private String confirmPassword;

	// Other pages

	@InjectPage
	private UserSearch userSearch;

	// Generally useful bits and pieces

	@Component(id = "form")
	private Form form;

	@InjectComponent("password")
	private PasswordField passwordField;

	@EJB
	ISecurityManagerServiceLocal securityManagerService;

	@Inject
	private TypeCoercer typeCoercer;

	// The code

	void onPrepare() {
		// Instantiate a User for the form fields to overlay.
		user = new User();
		user.setActive(true);
	}

	void onValidateFromForm() {

		if (form.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		if (password != null && confirmPassword != null && !password.equals(confirmPassword)) {
			form.recordError(passwordField, getMessages().get("User_confirmpassword_does_not_match"));
			return;
		}

		try {
			securityManagerService.createUser(user, password);
		}
		catch (Exception e) {
			form.recordError(interpretBusinessServicesExceptionForCreate(e));
			return;
		}
	}

	Object onSuccess() {
		return userSearch.createLinkWithLastSearch();
	}

	void onReset() {
	}

	Object onCancel() {
		return userSearch.createLinkWithLastSearch();
	}

	public String[] getSalutations() {
		return User.SALUTATIONS;
	}

	public PageStyle getBoxy() {
		return User.PageStyle.BOXY;
	}

	public PageStyle getWide() {
		return User.PageStyle.WIDE;
	}

	public String[] getDatePatterns() {
		return User.DATE_PATTERNS;
	}

	public EnumValueEncoder<PageStyle> getPageStyleEncoder() {
		return new EnumValueEncoder<PageStyle>(typeCoercer, User.PageStyle.class);
	}
}
