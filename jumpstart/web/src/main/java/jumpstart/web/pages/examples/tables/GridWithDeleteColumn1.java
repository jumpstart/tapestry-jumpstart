package jumpstart.web.pages.examples.tables;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import jumpstart.business.commons.IdVersion;
import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.commons.FieldCopy;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.corelib.components.Form;

@Import(stylesheet = "css/examples/plain.css")
public class GridWithDeleteColumn1 {
	static private final int MAX_RESULTS = 30;

	// Screen fields

	@Property
	private List<Person> persons;

	private Person person;

	@Property
	private final PersonEncoder personEncoder = new PersonEncoder();

	@Property
	private final DateEncoder dateEncoder = new DateEncoder();

	@Property
	private final String BAD_NAME = "Mary";

	// Work fields

	private List<Person> personsInDB;

	private boolean inFormSubmission;

	private List<Person> personsSubmitted;

	private List<IdVersion> personsToDelete;

	private int rowNum;
	private Map<Integer, FieldCopy> deleteFieldCopyByRowNum;

	// Other pages

	@InjectPage
	private GridWithDeleteColumn2 page2;

	// Generally useful bits and pieces

	@Component(id = "deletables")
	private Form form;

	@InjectComponent("delete")
	private Checkbox deleteField;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	// The code

	void onActivate() {
		inFormSubmission = false;
	}

	// Form bubbles up the PREPARE_FOR_RENDER event during form render.

	void onPrepareForRender() {

		// If fresh start, populate screen with all persons from the database

		if (form.isValid()) {
			// Get all persons - ask business service to find them (from the database)
			personsInDB = personFinderService.findPersons(MAX_RESULTS);

			persons = new ArrayList<Person>();
			for (Person personInDB : personsInDB) {
				persons.add(personInDB);
			}
		}

	}

	// Form bubbles up the PREPARE_FOR_SUBMIT event during form submission.

	void onPrepareForSubmit() {
		inFormSubmission = true;
		personsSubmitted = new ArrayList<Person>();
		personsToDelete = new ArrayList<IdVersion>();

		// Get all persons - ask business service to find them (from the database)
		personsInDB = personFinderService.findPersons(MAX_RESULTS);

		// Prepare to take a copy of each editable field.

		rowNum = -1;
		deleteFieldCopyByRowNum = new HashMap<Integer, FieldCopy>();
	}

	void onValidateFromDelete() {
		// Unfortunately, this method is never called because Checkbox doesn't bubble up VALIDATE. It's a shame because
		// this would be the perfect place to validate whether deleting is OK, or to put an entry in
		// deleteFieldCopyByRowNum.
		// Please vote for https://issues.apache.org/jira/browse/TAP5-2075 .
	}

	void onValidateFromDeletables() {

		// Error if any person to delete has a null id - it means toValue(...) found they are no longer in the database.

		for (IdVersion personToDelete : personsToDelete) {
			if (personToDelete.getId() == null) {
				form.recordError("The list of persons is out of date. Please refresh and try again.");
				return;
			}
		}

		// Populate our list of persons to delete with the submitted versions (see setDelete(...) for more).
		// Also, simulate a server-side validation error: return error if deleting a person with first name BAD_NAME.

		for (IdVersion personToDelete : personsToDelete) {
			rowNum = -1;

			for (Person personSubmitted : personsSubmitted) {
				rowNum++;

				if (personSubmitted.getId() != null && personSubmitted.getId().equals(personToDelete.getId())) {
					personToDelete.setVersion(personSubmitted.getVersion());

					// Unfortunately, at this point the deleteField is from the final row of the Loop.
					// Fortunately, we have a copy of the correct field, so we can record the error with that.

					if (personSubmitted.getFirstName() != null && personSubmitted.getFirstName().equals(BAD_NAME)) {
						Field field = deleteFieldCopyByRowNum.get(rowNum);
						form.recordError(field, "Cannot delete " + BAD_NAME + ".");
					}

					break;
				}
			}
		}

		if (form.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		try {
			System.out.println(">>> personsSubmitted = " + personsSubmitted);
			System.out.println(">>> personsToDelete = " + personsToDelete);
			// In a real application we would persist them to the database instead of printing them
			// personManagerService.bulkEditPersons(new ArrayList<Person>(), new ArrayList<Person>(), personsToDelete);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			form.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	Object onSuccess() {
		page2.set(personsToDelete);
		return page2;
	}

	void onFailure() {
		persons = new ArrayList<Person>(personsSubmitted);
	}

	void onRefresh() {
		// By doing nothing the page will be displayed afresh.
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;

		if (inFormSubmission) {
			personsSubmitted.add(person);
		}
	}

	// This encoder is used by our Loop:
	// - during render, to convert each person to an id (Loop then stores the ids in the form, hidden).
	// - during form submission, to convert each id back to a person which it puts in our person field.

	private class PersonEncoder implements ValueEncoder<Person> {

		@Override
		public String toClient(Person value) {
			Long id = person.getId();
			return id == null ? null : id.toString();
		}

		@Override
		public Person toValue(String idAsString) {
			Long id = idAsString == null ? null : new Long(idAsString);
			Person person = findPerson(id);

			// If person has since been deleted from the DB. Create a skeleton person.
			if (person == null) {
				person = new Person();
			}

			// Loop will overwrite the firstName of the person returned.
			return person;
		}

		private Person findPerson(Long id) {

			// We could find the person in the database, but it's cheaper to search the list we got in
			// onPrepareForSubmit().

			for (Person person : personsInDB) {
				if (person.getId().equals(id)) {
					return person;
				}
			}
			return null;
		}
	};

	// The Grid component will automatically call this for every row as it is rendered.
	
	public boolean isDelete() {
		return false;
	}

	// The Grid component will automatically call this for every row on submit.

	public void setDelete(boolean delete) {

		if (inFormSubmission) {
			rowNum++;
			deleteFieldCopyByRowNum.put(rowNum, new FieldCopy(this.deleteField));

			if (delete) {
				// Put the current person in our list of ones to delete. Record their id but not version - we shouldn't
				// assume person.version has been overwritten yet with the submitted value - it may still hold the
				// database value.
				personsToDelete.add(new IdVersion(person.getId(), null));
			}
		}

	}

	private class DateEncoder implements ValueEncoder<Date> {

		@Override
		public String toClient(Date date) {
			long timeMillis = date.getTime();
			return Long.toString(timeMillis);
		}

		@Override
		public Date toValue(String timeMillisAsString) {
			long timeMillis = Long.parseLong(timeMillisAsString);
			Date date = new Date(timeMillis);
			return date;
		}

	}

}
