package jumpstart.web.pages.examples.ajax;

import java.text.DateFormat;
import java.text.Format;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

@Import(stylesheet = "css/examples/ajaxformsinaloop.css")
public class AjaxFormsInALoop {
	static private final int MAX_RESULTS = 30;

	// Screen fields

	@Property
	private List<Person> persons;

	@Property
	private Person person;

	@Property
	private boolean editing;

	@Property
	private final String BAD_NAME = "Acme";

	// Work fields

	private boolean loadingLoop;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@EJB
	private IPersonManagerServiceLocal personManagerService;

	@InjectComponent
	private Zone rowZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@InjectComponent
	private Form personForm;

	@Inject
	private Messages messages;

	@Inject
	private Locale currentLocale;

	// The code

	void onActivate() {
		loadingLoop = false;
	}

	void setupRender() {
		loadingLoop = true;

		// Get all persons - ask business service to find them (from the database)
		persons = personFinderService.findPersons(MAX_RESULTS);
	}

	void onPrepareForRenderFromPersonForm(Long personId) {

		// If the loop is being reloaded, the form may have had errors so clear them just in case.

		if (loadingLoop) {
			personForm.clearErrors();
			editing = false;
		}

		// If the form is valid then we're not redisplaying due to error, so get the person.

		if (personForm.isValid()) {
			person = personFinderService.findPerson(personId);
			// Handle null person in the template.
		}
	}

	void onPrepareForSubmitFromPersonForm(Long personId) {

		// Get objects for the form fields to overlay.
		person = personFinderService.findPerson(personId);

	}

	void onValidateFromPersonForm() {

		// Simulate a server-side validation error: return error if anyone's first name is BAD_NAME.

		if (person.getFirstName() != null && person.getFirstName().equals(BAD_NAME)) {
			personForm.recordError("First name must not be " + BAD_NAME + ".");
		}

		if (person.getId() == 2 && !person.getFirstName().equals("Mary")) {
			personForm.recordError("First name for this person must be Mary.");
		}

		if (personForm.getHasErrors()) {
			return;
		}

		try {
			personManagerService.changePerson(person);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			personForm.recordError(ExceptionUtil.getRootCauseMessage(e));
		}

	}

	void onSuccessFromPersonForm() {

		editing = false;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(rowZone);
		}
	}

	void onFailureFromPersonForm() {

		editing = true;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(rowZone);
		}
	}

	void onToEdit(Long personId) {
		person = personFinderService.findPerson(personId);
		editing = true;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(rowZone);
		}
	}

	void onCancel(Long personId) {
		person = personFinderService.findPerson(personId);
		editing = false;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(rowZone);
		}
	}

	public String getCurrentRowZoneId() {
		// The id attribute of a row must be the same every time that row asks for it and unique on the page.
		return "rowZone_" + person.getId();
	}

	public String getPersonRegion() {
		return messages.get(Regions.class.getSimpleName() + "." + person.getRegion().name());
	}

	public Format getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT, currentLocale);
	}

	public boolean isPersonFormHasErrors() {
		return personForm.getHasErrors();
	}

}
