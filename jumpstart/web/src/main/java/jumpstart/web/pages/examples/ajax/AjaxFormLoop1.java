package jumpstart.web.pages.examples.ajax;

import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;

import jumpstart.business.commons.IdVersion;
import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.business.domain.person.iface.PersonDTO;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.encoders.examples.IdVersionsEncoder;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

public class AjaxFormLoop1 {
	static private final int MAX_RESULTS = 30;

	// Screen fields

	@Property
	private List<PersonDTO> persons;

	private PersonDTO person;

	@Property
	private final PersonDTOEncoder personEncoder = new PersonDTOEncoder();

	// A snapshot of the (id, version) of each person originally displayed before you added or removed any. On submit,
	// we use it to determine which persons you removed.
	@Property
	private List<IdVersion> personsDisplayed;

	@Property
	private final IdVersionsEncoder personsDisplayedEncoder = new IdVersionsEncoder();

	@Property
	private final String BAD_NAME = "Acme";

	@Property
	@Persist(PersistenceConstants.FLASH)
	private boolean disableSubmit;

	// Work fields

	private List<Person> personsInDB;

	private boolean inFormSubmission;

	private List<PersonDTO> personsSubmitted;

	private List<PersonDTO> personsToCreate;

	private List<PersonDTO> personsToChange;

	private List<IdVersion> personsToDelete;

	// Other pages

	@InjectPage
	private AjaxFormLoop2 page2;

	// Generally useful bits and pieces

	@InjectComponent
	private Zone personsEditZone;

	@Component(id = "personsEdit")
	private Form form;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@Inject
	private Locale currentLocale;

	@Inject
	private Request request;

	// The code

	void onActivate() {
		inFormSubmission = false;
	}

	// Form bubbles up the PREPARE_FOR_RENDER event during form render.

	void onPrepareForRender() {
		inFormSubmission = false;

		// If the page had errors and the user chose to reload it, then we detect that and clear the errors.

		if (personsSubmitted == null) {
			form.clearErrors();
		}

		// If fresh start, populate screen with all persons from the database

		if (form.isValid()) {
			// Get all persons - ask business service to find them (from the database)
			personsInDB = personFinderService.findPersons(MAX_RESULTS);

			// Populate the persons to be edited, and also a snapshot of who is in that list.
			persons = new ArrayList<PersonDTO>();
			personsDisplayed = new ArrayList<IdVersion>();

			for (Person personInDB : personsInDB) {
				persons.add(new PersonDTO(personInDB.getId(), personInDB.getVersion(), personInDB.getFirstName(),
						personInDB.getLastName(), personInDB.getRegion(), personInDB.getStartDate()));
				personsDisplayed.add(new IdVersion(personInDB.getId(), personInDB.getVersion()));
			}
		}

		// Else, we're rendering after a redirect, so populate the screen from the flash values

		else {
			persons = new ArrayList<PersonDTO>(personsSubmitted);
		}
	}

	PersonDTO onAddRow() {
		// Return a skeleton person DTO which AjaxFormLoop will overwrite.
		return new PersonDTO();
	}

	void onRemoveRow(PersonDTO person) {
		// Nothing to do.
	}

	// Form bubbles up the PREPARE_FOR_SUBMIT event during form submission.

	void onPrepareForSubmit() {
		inFormSubmission = true;
		personsSubmitted = new ArrayList<PersonDTO>();
		personsDisplayed = new ArrayList<IdVersion>();

		// Get all persons - ask business service to find them (from the database)
		personsInDB = personFinderService.findPersons(MAX_RESULTS);
	}

	void onValidateFromPersonsEdit() {

		if (form.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		personsToCreate = new ArrayList<PersonDTO>();
		personsToChange = new ArrayList<PersonDTO>();
		personsToDelete = new ArrayList<IdVersion>();

		// Error if any person submitted has a null id and non-null version - it means toValue(...) found they are no
		// longer in the database.

		for (PersonDTO personSubmitted : personsSubmitted) {
			if (personSubmitted.getId() == null && personSubmitted.getVersion() != null) {
				form.recordError("The list of persons is out of date. Please refresh and try again.");
				return;
			}
		}

		// Figure out which persons to delete, ie. see who you removed by comparing the submitted list to the displayed
		// list. It would not be correct to compare the submitted list to the database because we would end up deleting
		// persons that others have added since display.

		for (IdVersion personDisplayed : personsDisplayed) {
			boolean removed = true;
			for (PersonDTO personSubmitted : personsSubmitted) {
				if (personSubmitted.getId() != null && personSubmitted.getId().equals(personDisplayed.getId())) {
					removed = false;
					break;
				}
			}
			if (removed) {
				personsToDelete.add(personDisplayed);
			}
		}

		// Figure out which persons to create, ie. see which persons you have added. Treat the rest as persons to
		// change.

		for (PersonDTO personSubmitted : personsSubmitted) {
			if (personSubmitted.getId() == null) {
				personsToCreate.add(personSubmitted);
			}
			else {
				personsToChange.add(personSubmitted);
			}
		}

		// Simulate a server-side validation error: return error if anyone's first name is BAD_NAME.

		for (PersonDTO personSubmitted : personsSubmitted) {
			if (personSubmitted.getFirstName() != null && personSubmitted.getFirstName().equals(BAD_NAME)) {
				form.recordError("First name cannot be " + BAD_NAME + ".");
				return;
			}
		}

		try {
			System.out.println(">>> personsToCreate = " + personsToCreate);
			System.out.println(">>> personsToChange = " + personsToChange);
			System.out.println(">>> personsToDelete = " + personsToDelete);
			// In a real application you would persist them to the database instead of printing them.
			// personManagerService.bulkEditPersonsByDTOs(personsToCreate, personsToChange, personsToDelete);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			form.recordError(ExceptionUtil.getRootCauseMessage(e));
			return;
		}

	}

	Object onSuccess() {
		page2.set(personsToCreate, personsToChange, personsToDelete);
		return page2;
	}

	Object onFailure() {

		if (request.isXHR()) {
			if (personsToDelete != null && personsToDelete.size() > 0) {
				form.recordError("The submit button has been disabled to protect you from issue TAP5-1896.");
				disableSubmit = true;
			}
			return personsEditZone.getBody();
		}
		else {
			// Not an AJAX request, so don't bother. Just refresh the screen and it will display "JavaScript required".
			return onRefresh();
		}

	}

	Object onRefresh() {
		form.clearErrors();
		disableSubmit = false;

		return request.isXHR() ? personsEditZone.getBody() : null;
	}

	public PersonDTO getPerson() {
		return person;
	}

	public void setPerson(PersonDTO person) {
		this.person = person;

		if (inFormSubmission) {
			personsSubmitted.add(person);
		}
	}

	// This encoder is intended for use in a Loop or AjaxFormLoop:
	// - during render, to convert each person DTO to an id (AjaxFormLoop then stores the ids in the form, hidden).
	// - during form submission, to convert each id back to a person DTO. AjaxFormLoop will overwrite several fields of
	// the person DTO returned.

	private class PersonDTOEncoder implements ValueEncoder<PersonDTO> {

		@Override
		public String toClient(PersonDTO personDTO) {
			Long id = personDTO.getId();
			return id == null ? null : id.toString();
		}

		@Override
		public PersonDTO toValue(String idAsString) {
			PersonDTO person = null;

			if (idAsString == null) {
				person = new PersonDTO();
			}
			else {
				Long id = new Long(idAsString);
				person = findPerson(id);

				// If person has since been deleted from the DB, create an empty person.
				if (person == null) {
					person = new PersonDTO();
				}
			}

			// AjaxFormLoop will overwrite several fields of the person DTO returned.
			return person;
		}

		private PersonDTO findPerson(Long id) {

			// If in submit, we could find the person in the database but it's cheaper to search the list we got in
			// onPrepareForSubmit().

			if (inFormSubmission) {
				for (Person personInDB : personsInDB) {
					if (personInDB.getId().equals(id)) {
						PersonDTO personDTO = new PersonDTO(personInDB.getId(), personInDB.getVersion(),
								personInDB.getFirstName(), personInDB.getLastName(), personInDB.getRegion(),
								personInDB.getStartDate());
						return personDTO;
					}
				}
			}

			// Else, find the person in the database.

			else {
				Person person = personFinderService.findPerson(id);
				if (person != null) {
					PersonDTO personDTO = new PersonDTO(person.getId(), person.getVersion(), person.getFirstName(),
							person.getLastName(), person.getRegion(), person.getStartDate());
					return personDTO;
				}
			}

			return null;
		}

	}

	public Format getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT, currentLocale);
	}
}
