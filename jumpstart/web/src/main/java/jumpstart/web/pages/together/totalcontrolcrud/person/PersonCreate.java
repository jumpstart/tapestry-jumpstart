package jumpstart.web.pages.together.totalcontrolcrud.person;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.components.CustomForm;
import jumpstart.web.pages.together.totalcontrolcrud.Persons;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;

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
	private CustomForm personForm;

	@EJB
	private IPersonManagerServiceLocal personManagerService;

	// The code

	// PersonForm bubbles up the PREPARE event when it is rendered or submitted

	void onPrepare() throws Exception {
		// Instantiate a Person for the form data to overlay.
		person = new Person();
	}

	// PersonForm bubbles up the VALIDATE event when it is submitted

	void onValidateFromPersonForm() {

		if (personForm.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		if (demoModeStr != null && demoModeStr.equals("true")) {
			personForm.recordError("Sorry, but this function is not allowed in Demo mode.");
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
