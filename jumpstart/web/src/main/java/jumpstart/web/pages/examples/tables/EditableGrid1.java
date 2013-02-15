package jumpstart.web.pages.examples.tables;

import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.util.StringUtil;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

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

	// Work fields

	private List<Person> personsToCreate;

	// Other pages

	@InjectPage
	private EditableGrid2 page2;

	// Generally useful bits and pieces

	@Component(id = "personsCreate")
	private Form form;

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

	void onValidateFromPersonsCreate() {

		if (form.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		personsToCreate = new ArrayList<Person>();

		// Error if any person has fields entered but not all of them.

		for (Person person : persons) {
			if (StringUtil.isNotEmpty(person.getFirstName()) || StringUtil.isNotEmpty(person.getLastName())
					|| person.getRegion() != null || person.getStartDate() != null) {

				if (StringUtil.isEmpty(person.getFirstName()) || StringUtil.isEmpty(person.getLastName())
						|| person.getRegion() == null || person.getStartDate() == null) {
					form.recordError("A person is missing one or more of its fields.");
					return;
				}

				personsToCreate.add(person);
			}
		}

		try {
			// In a real application you would persist them to the database instead of printing them
			System.out.println(">>> personsToCreate = " + personsToCreate);
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
