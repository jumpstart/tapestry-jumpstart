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
import jumpstart.web.commons.EvenOdd;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

public class AjaxFormsInALoopWithDEH {
	static private final int MAX_RESULTS = 30;

	// Screen fields

	@Property
	private List<Person> persons;

	@Property
	private Person person;

	@Property
	private boolean editing;

	@Property
	private EvenOdd evenOdd;

	@Property
	private final String BAD_NAME = "Acme";

	@Property
	@Persist
	private boolean highlightZoneUpdates;

	// Work fields

	private boolean loadingLoop;

	private Actions action;

	private Long personId;

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

	@Component
	private Form personForm;

	@Inject
	private Messages messages;

	@Inject
	private Locale currentLocale;

	private enum Actions {
		TO_EDIT, CANCEL, SAVE;
	}

	// The code

	void onActivate() {
		loadingLoop = false;
	}

	void setupRender() {
		loadingLoop = true;

		// Get all persons - ask business service to find them (from the database)
		persons = personFinderService.findPersons(MAX_RESULTS);

		evenOdd = new EvenOdd();
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
		this.personId = personId;

		// Get objects for the form fields to overlay.
		person = personFinderService.findPerson(personId);

		if (person == null) {
			// Create an empty person for the form fields to overlay. It avoids NPE.
			person = new Person();
			personForm.recordError("Person has been deleted by another process.");
		}
	}

	void onSelectedFromEdit() {
		action = Actions.TO_EDIT;
	}

	void onSelectedFromSave() {
		action = Actions.SAVE;
	}

	void onSelectedFromCancel() {
		action = Actions.CANCEL;
	}

	void onValidateFromPersonForm() {

		if (action == Actions.SAVE) {

			if (personForm.getHasErrors()) {
				// We get here only if a server-side validator detected an error.
				return;
			}

			// Simulate a server-side validation error: return error if anyone's first name is BAD_NAME.

			if (person.getFirstName() != null && person.getFirstName().equals(BAD_NAME)) {
				personForm.recordError("First name cannot be " + BAD_NAME + ".");
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
		else if (action == Actions.TO_EDIT) {
			// Do nothing - there may be an error so don't clear the form.
		}
		else if (action == Actions.CANCEL) {
			personForm.clearErrors();
		}
		else {
			throw new IllegalStateException(action.name());
		}

	}

	void onSuccessFromPersonForm() {

		editing = action == Actions.TO_EDIT;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(rowZone);
		}
	}

	void onFailureFromPersonForm() {

		editing = action == Actions.SAVE;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(rowZone);
		}
	}

	public String getCurrentRowZoneId() {
		// The id attribute of a row must be the same every time that row asks for it and unique on the page.
		return "rowZone_" + getPersonId();
	}

	public Long getPersonId() {
		// Here we ensure we return the person's id even if they have been deleted since the page was loaded.
		return loadingLoop ? person.getId() : personId;
	}

	public String getPersonRegion() {
		// Follow the same naming convention that the Select component uses
		return messages.get(Regions.class.getSimpleName() + "." + person.getRegion().name());
	}

	public Format getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT, currentLocale);
	}

	public boolean isPersonFormHasErrors() {
		return personForm.getHasErrors();
	}

	public String getZoneUpdateFunction() {
		return highlightZoneUpdates ? "highlight" : "show";
	}

}
