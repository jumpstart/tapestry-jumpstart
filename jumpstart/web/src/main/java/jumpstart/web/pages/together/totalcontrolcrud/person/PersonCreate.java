package jumpstart.web.pages.together.totalcontrolcrud.person;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.pages.together.totalcontrolcrud.Persons;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;

@Import(stylesheet="css/examples/plain.css")
public class PersonCreate {

	private final String demoModeStr = System.getProperty("jumpstart.demo-mode");

	// Screen fields

	@Property
	private Person person;

	// Other pages

	@InjectPage
	private Persons indexPage;

	// Generally useful bits and pieces

	@Component(id = "personForm")
	private Form personForm;

	@Component(id = "firstName")
	private TextField firstNameField;

	@EJB
	private IPersonManagerServiceLocal personManagerService;

	// The code

	// PersonForm bubbles up the PREPARE_FOR_RENDER event when it is rendered

	void onPrepareForRender() throws Exception {

		// If fresh start, make sure there's a Person object available.

		if (personForm.isValid()) {
			person = new Person();
		}
	}

	// PersonForm bubbles up the PREPARE_FOR_SUBMIT event when it is submitted

	void onPrepareForSubmit() throws Exception {
		// Instantiate a Person for the form data to overlay.
		person = new Person();
	}

	// PersonForm bubbles up the VALIDATE event when it is submitted

	void onValidateFromPersonForm() {

		if (person.getFirstName() != null && person.getFirstName().equals("Acme")) {
			personForm.recordError(firstNameField, firstNameField.getLabel() + " must not be Acme.");
		}

		if (demoModeStr != null && demoModeStr.equals("true")) {
			personForm.recordError("Sorry, but this function is not allowed in Demo mode.");
		}

		if (personForm.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		try {
			personManagerService.createPerson(person);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			personForm.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	// PersonForm bubbles up SUCCESS or FAILURE when it is submitted, depending on whether VALIDATE records an error

	Object onSuccess() {
		return indexPage;
	}
}
