package jumpstart.web.pages.together.totalcontrolcrud.person;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.pages.together.totalcontrolcrud.Persons;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;

@Import(stylesheet = "css/examples/plain.css")
public class PersonUpdate {

	// The activation context

	@Property
	private Long personId;

	// Screen fields

	@Property
	private Person person;

	// Other pages

	@InjectPage
	private Persons indexPage;

	// Generally useful bits and pieces

	@Component
	private Form form;

	@Component(id = "firstName")
	private TextField firstNameField;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@EJB
	private IPersonManagerServiceLocal personManagerService;

	// The code

	void onActivate(Long personId) {
		this.personId = personId;
	}

	Long onPassivate() {
		return personId;
	}

	void onPrepareForRender() {

		// If fresh start, make sure there's a Person object available.

		if (form.isValid()) {
			person = personFinderService.findPerson(personId);
			// Handle null person in the template.
		}

	}

	void onPrepareForSubmit() {

		// Get objects for the form fields to overlay.
		person = personFinderService.findPerson(personId);

		if (person == null) {
			form.recordError("Person has been deleted by another process.");
			// Instantiate an empty person to avoid NPE in the Form.
			person = new Person();
		}
	}

	void onValidateFromForm() {

		if (personId == 2 && !person.getFirstName().equals("Mary")) {
			form.recordError(firstNameField, firstNameField.getLabel() + " for this person must be Mary.");
		}

		if (form.getHasErrors()) {
			return;
		}

		try {
			personManagerService.changePerson(person);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			form.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	Object onSuccess() {
		return indexPage;
	}

}
