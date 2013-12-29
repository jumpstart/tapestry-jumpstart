package jumpstart.web.pages.together.easycrud.person;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.pages.together.easycrud.Persons;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;

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

	@Component(id = "personForm")
	private BeanEditForm personForm;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@EJB
	private IPersonManagerServiceLocal personManagerService;

	// The code

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(Long personId) {
		this.personId = personId;
	}

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	Long onPassivate() {
		return personId;
	}

	// setupRender() is called by Tapestry right before it starts rendering the page.

	void setupRender() {

		// We're doing this here instead of in onPrepareForRender() because person is used outside the form...

		// If fresh start, make sure there's a Person object available.

		if (personForm.isValid()) {
			person = personFinderService.findPerson(personId);
			// Handle null person in the template.
		}

	}

	// PersonForm bubbles up the PREPARE_FOR_SUBMIT event during form submission.

	void onPrepareForSubmit() {

		// Get Person object for the form fields to overlay.
		person = personFinderService.findPerson(personId);

		if (person == null) {
			personForm.recordError("Person has been deleted by another process.");
			// Instantiate an empty person to avoid NPE in the BeanEditForm.
			person = new Person();
		}
	}

	Object onCanceledFromPersonForm() {
		return indexPage;
	}

	// PersonForm bubbles up the VALIDATE event when it is submitted

	void onValidateFromPersonForm() {

		if (personForm.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		try {
			personManagerService.changePerson(person);
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
