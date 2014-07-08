package jumpstart.web.pages.examples.tables;

import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.util.StringUtil;
import jumpstart.web.commons.FieldCopy;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

@Import(stylesheet = "css/examples/editablegrid.css")
public class EditableGrid1 {

	// Screen fields

	@Property
	private List<Person> persons;

	@Property
	private Person person;

	@Property
	private BeanModel<Person> model;

	@Property
	private final int LIST_SIZE = 5;

	@Property
	private final String BAD_NAME = "Acme";

	// Work fields

	private int rowNum;
	private Map<Integer, FieldCopy> firstNameFieldCopyByRowNum;
	private Map<Integer, FieldCopy> lastNameFieldCopyByRowNum;
	private Map<Integer, FieldCopy> regionFieldCopyByRowNum;
	private Map<Integer, FieldCopy> startDateFieldCopyByRowNum;

	private List<Person> personsToCreate;

	// Other pages

	@InjectPage
	private EditableGrid2 page2;

	// Generally useful bits and pieces

	@InjectComponent("personsCreate")
	private Form form;

	@InjectComponent("firstName")
	private TextField firstNameField;

	@InjectComponent("lastName")
	private TextField lastNameField;

	@InjectComponent("region")
	private Select regionField;

	@InjectComponent("startDate")
	private DateField startDateField;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private Messages messages;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@Inject
	private Locale currentLocale;

	@Inject
	private ValidatorFactory validatorFactory;

	// The code

	// Form bubbles up the PREPARE_FOR_RENDER event during form render.

	void onPrepareForRender() {
		createPersonsList();

		// If fresh start (ie. not rendering after a redirect), add an example person.

		if (form.isValid()) {
			persons.set(0, new Person("Example", "Person", Regions.EAST_COAST, getTodayDate()));
		}
	}

	// Form bubbles up the PREPARE_FOR_SUBMIT event during form submission.

	void onPrepareForSubmit() {
		// Create the same list as was rendered.
		// Loop will write its input field values into the list's objects.

		createPersonsList();

		// Prepare to take a copy of each field.

		rowNum = -1;
		firstNameFieldCopyByRowNum = new HashMap<Integer, FieldCopy>();
		lastNameFieldCopyByRowNum = new HashMap<Integer, FieldCopy>();
		regionFieldCopyByRowNum = new HashMap<Integer, FieldCopy>();
		startDateFieldCopyByRowNum = new HashMap<Integer, FieldCopy>();
	}

	// Form bubbles up the PREPARE event during form render and form submission.

	void onPrepare() {

		// Configure the Grid to be unsortable

		model = beanModelSource.createDisplayModel(Person.class, messages);

		for (String propertyName : model.getPropertyNames()) {
			model.get(propertyName).sortable(false);
		}
	}

	void createPersonsList() {
		persons = new ArrayList<Person>();

		// Populate the list with as many empty objects as you want displayed.

		for (int i = 0; i < LIST_SIZE; i++) {
			persons.add(new Person());
		}
	}

	void onValidateFromFirstName() {
		rowNum++;
		firstNameFieldCopyByRowNum.put(rowNum, new FieldCopy(firstNameField));
	}

	void onValidateFromLastName() {
		lastNameFieldCopyByRowNum.put(rowNum, new FieldCopy(lastNameField));
	}

	void onValidateFromRegion() {
		regionFieldCopyByRowNum.put(rowNum, new FieldCopy(regionField));
	}

	void onValidateFromStartDate() {
		startDateFieldCopyByRowNum.put(rowNum, new FieldCopy(startDateField));
	}

	void onValidateFromPersonsCreate() {

		personsToCreate = new ArrayList<Person>();

		// Error if any person has fields entered but not all of them.

		rowNum = -1;

		for (Person person : persons) {
			rowNum++;

			if (StringUtil.isNotEmpty(person.getFirstName()) || StringUtil.isNotEmpty(person.getLastName())
					|| person.getRegion() != null || person.getStartDate() != null) {

				// Unfortunately, at this point the fields firstNameField, lastNameField, etc. are from the final row of
				// the Grid. Fortunately, we have a copy of the correct fields, so we can record the error with those.

				validate(person, "firstName", firstNameFieldCopyByRowNum.get(rowNum), form);
				validate(person, "lastName", lastNameFieldCopyByRowNum.get(rowNum), form);
				validate(person, "region", regionFieldCopyByRowNum.get(rowNum), form);
				validate(person, "startDate", startDateFieldCopyByRowNum.get(rowNum), form);

				if (person.getFirstName() != null && person.getFirstName().equals(BAD_NAME)) {
					Field field = firstNameFieldCopyByRowNum.get(rowNum);
					form.recordError(field, "First name cannot be " + BAD_NAME + ".");
				}

				personsToCreate.add(person);
			}
		}

		if (form.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		try {
			System.out.println(">>> personsToCreate = " + personsToCreate);
			// In a real application we would persist them to the database instead of printing them
			// personManagerService.createPersons(personsToCreate);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			form.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	Object onSuccess() {
		page2.set(personsToCreate);
		return page2;
	}

	void onFailure() {
		// Unnecessary method. Loop will carry the submitted input field values through the redirect.
	}

	void onRefresh() {
		// By doing nothing the page will be displayed afresh.
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

	public Format getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT, currentLocale);
	}

	private Date getTodayDate() {
		Calendar now = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		today.clear();
		today.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
		return today.getTime();
	}

}
