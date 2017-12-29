package jumpstart.web.pages.examples.input;

import jumpstart.business.domain.person.Person;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

@Import(stylesheet = "css/examples/plain.css")
public class BeanValidators {

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Person person;

	// Generally useful bits and pieces

	@InjectComponent
	private Form personForm;

	// The code

	// PersonForm bubbles up the PREPARE_FOR_RENDER event during form render.

	void onPrepareForRender() {

		// If fresh start, make sure there's a Person object available.

		if (personForm.isValid()) {
			if (person == null) {
				// Create object for the form fields to overlay.
				person = new Person();
			}
		}
	}

	// PersonForm bubbles up the PREPARE_FOR_SUBMIT event when it is submitted

	void onPrepareForSubmit() {
		// Create object for the form fields to overlay.
		person = new Person();
	}

	// PersonForm bubbles up the VALIDATE event when it is submitted

	void onValidateFromPersonForm() {

		if (personForm.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		try {
			// In a real application we might do something like create a Person here.
			// personManagerService.createPerson(person);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			personForm.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

}
