package jumpstart.web.pages.together.easycrud.person;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.pages.together.easycrud.Persons;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;

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

	@InjectComponent
	private BeanEditForm form;

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

	Object onCanceled() {
		return indexPage;
	}

	void onValidateFromForm() {

		if (person.getFirstName() != null && person.getFirstName().equals("Acme")) {
			form.recordError("First Name must not be Acme.");
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
