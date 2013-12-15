package jumpstart.web.pages.examples.tables;

import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.util.StringUtil;
import jumpstart.web.commons.FieldCopy;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

@Import(stylesheet = "css/examples/editableloop.css")
public class EditableLoop1 {
	private static final String REQUIRED_MSG_KEY = "required";

	// Screen fields

	@Property
	private List<Person> persons;

	@Property
	private Person person;

	@Property
	private final int LIST_SIZE = 5;

	// Work fields

	private int rowNum;
	private Map<Integer, FieldCopy> firstNameFieldCopyByRowNum;
	private Map<Integer, FieldCopy> lastNameFieldCopyByRowNum;
	private Map<Integer, FieldCopy> regionFieldCopyByRowNum;
	private Map<Integer, FieldCopy> startDateFieldCopyByRowNum;

	private List<Person> personsToCreate;

	// Other pages

	@InjectPage
	private EditableLoop2 page2;

	// Generally useful bits and pieces

	@Component(id = "personsCreate")
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
	private Messages messages;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@Inject
	private Locale currentLocale;

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

		rowNum = 0;
		firstNameFieldCopyByRowNum = new HashMap<Integer, FieldCopy>();
		lastNameFieldCopyByRowNum = new HashMap<Integer, FieldCopy>();
		regionFieldCopyByRowNum = new HashMap<Integer, FieldCopy>();
		startDateFieldCopyByRowNum = new HashMap<Integer, FieldCopy>();
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

		if (form.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		personsToCreate = new ArrayList<Person>();

		// Error if any person has fields entered but not all of them.

		rowNum = 0;

		for (Person person : persons) {
			rowNum++;

			if (StringUtil.isNotEmpty(person.getFirstName()) || StringUtil.isNotEmpty(person.getLastName())
					|| person.getRegion() != null || person.getStartDate() != null) {

				// Unfortunately, at this point the fields firstNameField, lastNameField, etc. are from the final row of the Loop.
				// Fortunately, we have a copy of the correct fields, so we can record the error with those.

				if (StringUtil.isEmpty(person.getFirstName())) {
					Field field = firstNameFieldCopyByRowNum.get(rowNum);
					form.recordError(field, messages.format(REQUIRED_MSG_KEY, field.getLabel()));
					return;
				}
				else if (StringUtil.isEmpty(person.getLastName())) {
					Field field = lastNameFieldCopyByRowNum.get(rowNum);
					form.recordError(field, messages.format(REQUIRED_MSG_KEY, field.getLabel()));
					return;
				}
				else if (person.getRegion() == null) {
					Field field = regionFieldCopyByRowNum.get(rowNum);
					form.recordError(field, messages.format(REQUIRED_MSG_KEY, field.getLabel()));
					return;
				}
				else if (person.getStartDate() == null) {
					Field field = startDateFieldCopyByRowNum.get(rowNum);
					form.recordError(field, messages.format(REQUIRED_MSG_KEY, field.getLabel()));
					return;
				}

				personsToCreate.add(person);
			}
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
