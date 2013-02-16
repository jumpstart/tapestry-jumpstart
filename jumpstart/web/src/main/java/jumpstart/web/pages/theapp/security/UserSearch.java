package jumpstart.web.pages.theapp.security;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.business.domain.security.iface.UserSearchFields;
import jumpstart.util.StringUtil;
import jumpstart.util.query.SearchOptions;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

@ProtectedPage
public class UserSearch extends SimpleBasePage {
	static private final String ISO_DATE_PATTERN = "yyyy-MM-dd";

	// The options for userSearchFields.active are null, true, and false but we have to present them using
	// String[] instead of Boolean[] because the inbuilt type coercer coerces null to false.
	// (see https://issues.apache.org/jira/browse/TAPESTRY-1928).
	static private final String[] ACTIVE_OPTIONS = { "true", "false" };

	// Query string parameters

	@ActivationRequestParameter(value = "loginid")
	private String loginId;

	@ActivationRequestParameter(value = "salutation")
	private String salutation;

	@ActivationRequestParameter(value = "firstname")
	private String firstName;

	@ActivationRequestParameter(value = "lastname")
	private String lastName;

	@ActivationRequestParameter(value = "email")
	private String email;

	// We could declare expiry to be a Date if we also contribute a ValueEncoder for Date in AppModule.
	@ActivationRequestParameter(value = "expiry")
	private String expiry;

	@ActivationRequestParameter(value = "active")
	private Boolean active;

	@Property
	@ActivationRequestParameter(value = "show")
	private Boolean showResult;

	// Screen fields

	@Property
	private UserSearchFields searchFields = new UserSearchFields();

	@Property
	private List<User> users;

	@Property
	private User user;

	// Generally useful bits and pieces

	@Persist
	private UserSearchFields lastSearchFields;

	@Persist
	private Boolean lastShowResult;

	@Component(id = "form")
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
		users = null;

		if (showResult == Boolean.TRUE) {
			users = search(searchFields);
			if (users == null) {
				users = new ArrayList<User>();
			}
		}

		lastSearchFields = new UserSearchFields(searchFields);
		lastShowResult = showResult;
	}

	void onSuccess() {
		setRequest(searchFields, true);
	}

	void onSort() {
		setRequest(searchFields, true);
	}

	void onReset() {
		setRequest(new UserSearchFields(), (Boolean) null);
		users = null;
	}

	Object onNew() {
		return UserCreate.class;
	}

	void onDelete(Long id, Integer version) {

		if (form.isValid()) {

			// Delete the user from the database unless they've been modified elsewhere

			try {
				User user = securityFinderService.findUser(id);
				if (!user.getVersion().equals(version)) {
					form.recordError("Cannot delete user because has been updated or deleted since last displayed.  Please refresh and try again.");
				}
				else {
					securityManagerService.deleteUser(user);
				}
			}
			catch (Exception e) {
				form.recordError(interpretBusinessServicesExceptionForDelete(e));
			}
		}
		setRequest(lastSearchFields, lastShowResult);
	}

	List<User> search(UserSearchFields searchFields) {
		SearchOptions searchOptions = new SearchOptions();
		List<User> l = securityFinderService.findUsersShallowish(searchFields, searchOptions);
		return l;
	}

	void setSearchFieldsFromRequest() {

		// Set the search filter criteria from the request URL query string fields.
		// We could have put the filter fields in the activation context, but arguably it's more RESTful to use
		// query string for filter criteria. The URL is certainly a more reliable bookmark this way.
		// Eg. See http://blpsilva.wordpress.com/2008/04/05/query-strings-in-restful-web-services/

		searchFields.setLoginId(loginId);
		searchFields.setSalutation(salutation);
		searchFields.setFirstName(firstName);
		searchFields.setLastName(lastName);
		searchFields.setEmailAddress(email);
		searchFields.setExpiryDate(toDate(expiry));
		searchFields.setActive(active);
	}

	void setRequest(UserSearchFields search, Boolean showResult) {

		// Return a link with the non-null search filter criteria in it.
		// We could have used onPassivate to output the search fields as the activation context, but arguably
		// it's more RESTful to use a query string for filter criteria. The URL is certainly a more reliable
		// bookmark this way.
		// Eg. See http://blpsilva.wordpress.com/2008/04/05/query-strings-in-restful-web-services/

		if (lastSearchFields == null) {
			loginId = null;
			salutation = null;
			firstName = null;
			lastName = null;
			email = null;
			expiry = null;
			active = null;
		}
		else {
			loginId = search.getLoginId();
			salutation = search.getSalutation();
			firstName = search.getFirstName();
			lastName = search.getLastName();
			email = search.getEmailAddress();
			expiry = toString(search.getExpiryDate());
			active = search.getActive();
		}

		this.showResult = showResult;
	}

	private String toString(Date value) {
		DateFormat isoDateFormat = new SimpleDateFormat(ISO_DATE_PATTERN);
		return value == null ? null : isoDateFormat.format(value);
	}

	private Date toDate(String value) {
		try {
			DateFormat isoDateFormat = new SimpleDateFormat(ISO_DATE_PATTERN);
			return value == null ? null : isoDateFormat.parse(value);
		}
		catch (ParseException e) {
			return null;
		}
	}

	public Link createLinkWithLastSearch() {
		setRequest(lastSearchFields, lastShowResult);

		Link link = pageRenderLinkSource.createPageRenderLink(this.getClass());
		return link;
	}

	public String[] getSalutations() {
		return User.SALUTATIONS;
	}

	public String[] getActiveOptions() {
		return ACTIVE_OPTIONS;
	}

	public String getActiveAsString() {
		Boolean active = searchFields.getActive();
		return active == null ? "" : active.toString();
	}

	public void setActiveAsString(String activeAsString) {

		// We can't simply use an encoder in the template to convert this field to/from a selected option because
		// the built in type coercer intercepts Booleans and converts null to false! We want null to mean null.

		Boolean active = StringUtil.isEmpty(activeAsString) ? null : Boolean.valueOf(activeAsString);
		searchFields.setActive(active);
	}

}
