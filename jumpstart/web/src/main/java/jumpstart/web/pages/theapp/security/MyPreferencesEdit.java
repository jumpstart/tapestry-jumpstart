package jumpstart.web.pages.theapp.security;

import javax.ejb.EJB;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.User.PageStyle;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.util.EnumValueEncoder;

@ProtectedPage
public class MyPreferencesEdit extends SimpleBasePage {

	// Screen fields

	@Property
	private User user;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String message;

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

	@Inject
	private TypeCoercer typeCoercer;

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
		try {
			securityManagerService.changeUser(user);
		}
		catch (Exception e) {
			form.recordError(interpretBusinessServicesExceptionForChange(e));
		}
	}

	void onSuccess() {
		getVisit().noteChanges(user);
		message = getMessages().get("User_preferences_saved");
	}

	void onFailure() {
		versionFlash = user.getVersion();
	}

	void onRefresh() {
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
