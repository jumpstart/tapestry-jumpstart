package jumpstart.web.pages.examples.tables;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;

@Import(stylesheet = "css/examples/plain.css")
public class EditableGridForUpdate1 {
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
	private final String BAD_NAME = "Acme";

	// Work fields

	private List<Person> personsInDB;

	private boolean inFormSubmission;

	private List<Person> personsSubmitted;

	private int rowNum;
	private Map<Integer, FieldCopy> firstNameFieldCopyByRowNum;

	// Other pages

	@InjectPage
	private EditableGridForUpdate2 page2;

	// Generally useful bits and pieces

	@Component(id = "personsEdit")
	private Form form;

	@InjectComponent("firstName")
	private TextField firstNameField;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@Inject
	private ValidatorFactory validatorFactory;

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

			// Populate the persons to be edited.
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

		// Get all persons - ask business service to find them (from the database)
		personsInDB = personFinderService.findPersons(MAX_RESULTS);

		// Prepare to take a copy of each editable field.

		rowNum = -1;
		firstNameFieldCopyByRowNum = new HashMap<Integer, FieldCopy>();
	}

	void onValidateFromFirstName() {
		rowNum++;
		firstNameFieldCopyByRowNum.put(rowNum, new FieldCopy(firstNameField));
	}

	void onValidateFromPersonsEdit() {

		// Error if any person submitted has a null id - it means toValue(...) found they are no longer in the database.

		for (Person personSubmitted : personsSubmitted) {
			if (personSubmitted.getId() == null) {
				form.recordError("The list of persons is out of date. Please refresh and try again.");
				return;
			}
		}

		rowNum = -1;

		for (Person personSubmitted : personsSubmitted) {
			rowNum++;

			// Unfortunately, at this point the field firstNameField is from the final row of the Grid.
			// Fortunately, we have a copy of the correct field, so we can record the error with that.

			validate(personSubmitted, "firstName", firstNameFieldCopyByRowNum.get(rowNum), form);

			if (personSubmitted.getFirstName() != null && personSubmitted.getFirstName().equals(BAD_NAME)) {
				Field field = firstNameFieldCopyByRowNum.get(rowNum);
				form.recordError(field, "First name cannot be " + BAD_NAME + ".");
			}
		}

		if (form.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		try {
			System.out.println(">>> personsSubmitted = " + personsSubmitted);
			// In a real application we would persist them to the database instead of printing them
			// personManagerService.changePersons(personsSubmitted);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			form.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	Object onSuccess() {
		page2.set(personsSubmitted);
		return page2;
	}

	void onFailure() {
		persons = personsSubmitted;
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

	// This encoder is used by our Grid:
	// - during render, to convert each person to an id (Grid then stores the ids in the form, hidden).
	// - during form submission, to convert each id back to a person which it puts in our person field.
	// Grid will overwrite the firstName of the person returned.

	private class PersonEncoder implements ValueEncoder<Person> {

		@Override
		public String toClient(Person person) {
			Long id = person.getId();
			return id == null ? null : id.toString();
		}

		@Override
		public Person toValue(String idAsString) {
			Person person = null;

			if (idAsString == null) {
				person = new Person();
			}
			else {
				Long id = new Long(idAsString);
				person = findPerson(id);

				// If person has since been deleted from the DB. Create a skeleton person.
				if (person == null) {
					person = new Person();
				}
			}

			// Loop will overwrite the firstName of the person returned.
			return person;
		}

		private Person findPerson(Long id) {

			// We could find the person in the database, but it's cheaper to search the list we got in
			// onPrepareForSubmit().

			for (Person personInDB : personsInDB) {
				if (personInDB.getId().equals(id)) {
					return personInDB;
				}
			}
			return null;
		}

	};

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

	private void validate(Object bean, String propertyName, Field field, Form form) {
		String errorMessage = validate(bean, propertyName, field);

		if (errorMessage != null) {
			form.recordError(field, errorMessage);
		}
	}

	/**
	 * Use this method to validate fields that aren't being validated elsewhere, eg. derived fields, or fields that are
	 * disabled in screen (because disabled input fields are not submitted or validated). Based on Tapestry's
	 * BeanFieldValidator#validate(Object).
	 * 
	 * @param bean
	 * @param propertyName
	 * @param field
	 * @return Error message string to use in Form#recordError or Tracker#recordError.
	 */
	private <T> String validate(T bean, String propertyName, Field field) {
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<T>> constraintViolations = validator.validateProperty(bean, propertyName);

		if (constraintViolations.isEmpty()) {
			return null;
		}

		final StringBuilder builder = new StringBuilder();

		for (Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator(); iterator.hasNext();) {
			ConstraintViolation<T> violation = (ConstraintViolation<T>) iterator.next();

			builder.append(String.format("%s %s", field.getLabel(), violation.getMessage()));

			if (iterator.hasNext()) {
				builder.append(", ");
			}
		}

		return builder.toString();
	}

}
