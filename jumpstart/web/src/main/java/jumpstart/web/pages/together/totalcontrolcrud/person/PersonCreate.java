package jumpstart.web.pages.together.totalcontrolcrud.person;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.pages.together.totalcontrolcrud.Persons;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;

@Import(stylesheet = "css/examples/plain.css")
public class PersonCreate {

	private final String demoModeStr = System.getProperty("jumpstart.demo-mode");

	// Screen fields

	@Property
	private Person person;

	// Other pages

	@InjectPage
	private Persons indexPage;

	// Generally useful bits and pieces

	@Component
	private Form form;

	@InjectComponent("firstName")
	private TextField firstNameField;

	@EJB
	private IPersonManagerServiceLocal personManagerService;

	// The code

	void onPrepareForRender() throws Exception {

		// If fresh start, make sure there's a Person object available.

		if (form.isValid()) {
			person = new Person();
		}
	}

	void onPrepareForSubmit() throws Exception {
		// Instantiate a Person for the form data to overlay.
		person = new Person();
	}

	void onValidateFromForm() {

		if (person.getFirstName() != null && person.getFirstName().equals("Acme")) {
			form.recordError(firstNameField, firstNameField.getLabel() + " must not be Acme.");
		}

		if (demoModeStr != null && demoModeStr.equals("true")) {
			form.recordError("Sorry, but this function is not allowed in Demo mode.");
		}

		if (form.getHasErrors()) {
			return;
		}

		try {
			personManagerService.createPerson(person);
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
