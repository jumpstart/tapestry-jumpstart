package jumpstart.web.pages.theapp.security;

import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.User.PageStyle;
import jumpstart.business.domain.security.UserRole;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.util.EnumValueEncoder;

@ProtectedPage
public class UserEdit extends SimpleBasePage {

	// Activation context

	@Property
	private Long userId;

	// Screen fields

	@Property
	private User user;

	@Property
	private List<UserRole> userRoles;

	@Property
	private UserRole userRole;

	@Property
	private final UserRoleKeyEncoder encoder = new UserRoleKeyEncoder();

	// Work fields

	// This carries version through the redirect that follows a server-side validation failure.
	@Persist(PersistenceConstants.FLASH)
	private Integer versionFlash;

	// Other pages

	@InjectPage
	private UserSearch userSearch;

	@InjectPage
	private UserPasswordChange userPasswordChange;

	@InjectPage
	private UserRoleAdd addPage;

	@InjectPage
	private UserRoleView viewPage;

	@InjectPage
	private UserRoleEdit editPage;

	// Generally useful bits and pieces

	@Component(id = "form")
	private Form form;

	@Inject
	private PageRenderLinkSource pageRenderLinkSource;

	@EJB
	private ISecurityFinderServiceLocal securityFinderService;

	@EJB
	private ISecurityManagerServiceLocal securityManagerService;

	@Inject
	private TypeCoercer typeCoercer;

	// The code

	public void set(Long id) {
		userId = id;
	}

	Long onPassivate() {
		return userId;
	}

	void onActivate(Long id) {
		userId = id;
	}

	void setupRender() {
		userRoles = securityFinderService.findUserRolesShallowishByUser(userId);
	}

	void onPrepareForRender() throws Exception {
		user = findUser(userId);
		// Handle null person in the template.

		// If the form has errors then we're redisplaying after a redirect.
		// Form will restore your input values but it's up to us to restore Hidden values.

		if (form.getHasErrors()) {
			if (user != null) {
				user.setVersion(versionFlash);
			}
		}
	}

	void onPrepareForSubmit() {
		user = findUser(userId);

		if (user == null) {
			user = new User();
			form.recordError("User has been deleted by another process.");
		}
	}

	private User findUser(Long userId) {
		try {
			return securityFinderService.findUser(userId);
		}
		catch (DoesNotExistException e) {
			// Handle null user in the template
			return null;
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
		getVisit().noteChanges(user);
		return userSearch.createLinkWithLastSearch();
	}

	void onFailure() {
		versionFlash = user.getVersion();
	}

	Object onChangePassword() {
		userPasswordChange.set(userId);
		return userPasswordChange;
	}

	void onRefresh() {
	}

	Object onCancel() {
		return userSearch.createLinkWithLastSearch();
	}

	Object onAddUserRole() {
		addPage.set(userId, null, createLinkToThisPage());
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

	private class UserRoleKeyEncoder implements ValueEncoder<UserRole> {

		@Override
		public String toClient(UserRole value) {
			return value.getId().toString();
		}

		@Override
		public UserRole toValue(String keyAsString) {
			Long id = new Long(keyAsString);
			try {
				return securityFinderService.findUserRole(id);
			}
			catch (DoesNotExistException e) {
				return null;
			}
		}
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

	public boolean isUserExists() {
		// We get the user in onPrepareFromForm, so this test will work correctly ONLY if it is in or below the Form on
		// the page.
		return user != null;
	}
}
