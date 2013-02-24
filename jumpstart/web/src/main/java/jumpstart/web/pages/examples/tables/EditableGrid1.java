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

public class EditableGrid1 {
	private static final String REQUIRED_MSG_KEY = "required";

	// Screen fields

	@Property
	private List<Person> persons;

	@Property
	private Person person;

	@Property
	private BeanModel<Person> model;

	@Property
	private final int LIST_SIZE = 5;

	// Work fields

	private int rowNum;
	private Map<Integer, FieldCopy> firstNameCopyByRowNum;
	private Map<Integer, FieldCopy> lastNameCopyByRowNum;
	private Map<Integer, FieldCopy> regionCopyByRowNum;
	private Map<Integer, FieldCopy> startDateCopyByRowNum;

	private List<Person> personsToCreate;

	// Other pages

	@InjectPage
	private EditableGrid2 page2;

	// Generally useful bits and pieces

	@Component(id = "personsCreate")
	private Form form;

	@InjectComponent
	private TextField firstName;

	@InjectComponent
	private TextField lastName;

	@InjectComponent
	private Select region;

	@InjectComponent
	private DateField startDate;

	@Inject
	private BeanModelSource beanModelSource;

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
		firstNameCopyByRowNum = new HashMap<Integer, FieldCopy>();
		lastNameCopyByRowNum = new HashMap<Integer, FieldCopy>();
		regionCopyByRowNum = new HashMap<Integer, FieldCopy>();
		startDateCopyByRowNum = new HashMap<Integer, FieldCopy>();
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
		firstNameCopyByRowNum.put(rowNum, new FieldCopy(firstName));
	}

	void onValidateFromLastName() {
		lastNameCopyByRowNum.put(rowNum, new FieldCopy(lastName));
	}

	void onValidateFromRegion() {
		regionCopyByRowNum.put(rowNum, new FieldCopy(region));
	}

	void onValidateFromStartDate() {
		startDateCopyByRowNum.put(rowNum, new FieldCopy(startDate));
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

				// Unfortunately, at this point the fields firstName, lastName, etc. are from the final row of the Grid.
				// Fortunately, we have a copy of the correct fields, so we can record the error with those.

				if (StringUtil.isEmpty(person.getFirstName())) {
					Field field = firstNameCopyByRowNum.get(rowNum);
					form.recordError(field, messages.format(REQUIRED_MSG_KEY, field.getLabel()));
					return;
				}
				else if (StringUtil.isEmpty(person.getLastName())) {
					Field field = lastNameCopyByRowNum.get(rowNum);
					form.recordError(field, messages.format(REQUIRED_MSG_KEY, field.getLabel()));
					return;
				}
				else if (person.getRegion() == null) {
					Field field = regionCopyByRowNum.get(rowNum);
					form.recordError(field, messages.format(REQUIRED_MSG_KEY, field.getLabel()));
					return;
				}
				else if (person.getStartDate() == null) {
					Field field = startDateCopyByRowNum.get(rowNum);
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
