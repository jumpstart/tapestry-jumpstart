package jumpstart.web.pages.theapp.security;

import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.domain.security.Role;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.UserRole;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.business.domain.security.iface.UserRoleSearchFields;
import jumpstart.util.query.SearchOptions;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;
import jumpstart.web.models.app.select.RoleIdSelectModel;
import jumpstart.web.models.app.select.UserIdSelectModel;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

@ProtectedPage
public class UserRoleSearch extends SimpleBasePage {

	// Query string parameters

	@ActivationRequestParameter(value = "userid")
	private Long userId;

	@ActivationRequestParameter(value = "roleid")
	private Long roleId;

	@Property
	@ActivationRequestParameter(value = "show")
	private Boolean showResult;

	// Screen fields

	@Property
	private UserRoleSearchFields searchFields = new UserRoleSearchFields();

	@Persist
	private UserRoleSearchFields lastSearchFields;

	@Persist
	private Boolean lastShowResult;

	// Screen fields

	@Property
	private List<UserRole> userRoles;

	@Property
	private UserRole userRole;

	@Property
	private SelectModel userIdsModel;

	@Property
	private SelectModel roleIdsModel;

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

	void onActivate() {
		setSearchFieldsFromRequest();
	}

	void setupRender() {
		userRoles = null;

		// If needed, do the search.

		if (showResult == Boolean.TRUE) {
			userRoles = search(searchFields);
		}

		// Put a selection list of users onto the page.

		List<User> users = securityFinderService.findUsersShallowish();
		userIdsModel = new UserIdSelectModel(users);

		// Put a selection list of roles onto the page.

		List<Role> roles = securityFinderService.findRolesShallowish();
		roleIdsModel = new RoleIdSelectModel(roles);

		// Save the search criteria for later.

		lastSearchFields = new UserRoleSearchFields(searchFields);
		lastShowResult = showResult;
	}

	List<UserRole> search(UserRoleSearchFields searchFields) {
		SearchOptions searchOptions = new SearchOptions();
		List<UserRole> l = securityFinderService.findUserRolesShallowish(searchFields, searchOptions);
		return l;
	}

	void onSuccess() {
		setRequest(searchFields, true);
	}

	void onReset() {
		setRequest(new UserRoleSearchFields(), (Boolean) null);
		userIdsModel = null;
	}

	Object onAdd() {
		setRequest(lastSearchFields, lastShowResult);
		Link link = pageRenderLinkSource.createPageRenderLink(this.getClass());
		addPage.set(null, null, link);
		return addPage;
	}

	Object onView(Long id) {
		setRequest(lastSearchFields, lastShowResult);
		Link link = pageRenderLinkSource.createPageRenderLink(this.getClass());
		viewPage.set(id, link);
		return viewPage;
	}

	Object onEdit(Long id) {
		setRequest(lastSearchFields, lastShowResult);
		Link link = pageRenderLinkSource.createPageRenderLink(this.getClass());
		editPage.set(id, link);
		return editPage;
	}

	void onRemove(Long id, Integer version) {

		if (form.isValid()) {

			// Delete the user from the database unless they've been modified elsewhere

			try {
				UserRole userRole = securityFinderService.findUserRole(id);
				if (!userRole.getVersion().equals(version)) {
					form.recordError("Cannot remove user role because has been updated or deleted since last displayed.  Please refresh and try again.");
				}
				else {
					securityManagerService.removeUserRole(userRole);
				}
			}
			catch (Exception e) {
				form.recordError(interpretBusinessServicesExceptionForRemove(e));
			}
		}

		setRequest(lastSearchFields, lastShowResult);
	}

	void setSearchFieldsFromRequest() {

		// Set the search filter criteria from the request URL query string fields.
		// We could have put the filter fields in the activation context, but arguably it's more RESTful to use
		// query string for filter criteria. The URL is certainly a more reliable bookmark this way.
		// Eg. See http://blpsilva.wordpress.com/2008/04/05/query-strings-in-restful-web-services/

		searchFields.setUserId(userId);
		searchFields.setRoleId(roleId);
	}

	void setRequest(UserRoleSearchFields search, Boolean showResult) {

		// Return a link with the non-null search filter criteria in it.
		// We could have used onPassivate to output the search fields as the activation context, but arguably
		// it's more RESTful to use a query string for filter criteria. The URL is certainly a more reliable
		// bookmark this way.
		// Eg. See http://blpsilva.wordpress.com/2008/04/05/query-strings-in-restful-web-services/

		if (lastSearchFields == null) {
			userId = null;
			roleId = null;
		}
		else {
			userId = search.getUserId();
			roleId = search.getRoleId();
		}

		this.showResult = showResult;
	}

}
