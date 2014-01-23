package jumpstart.web.components.together.componentscrud2;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * This component will trigger the following events on its container (which in this example is the page):
 * {@link jumpstart.web.components.examples.component.PersonEditorForm.PersonEditor#CANCEL_UPDATE}(Long personId),
 * {@link jumpstart.web.components.examples.component.PersonEditorForm.PersonEditor#SUCCESSFUL_UPDATE}(Long personId),
 */
// @Events is applied to a component solely to document what events it may trigger. It is not checked at runtime.
@Events({ PersonUpdateForm.CANCEL_UPDATE, PersonUpdateForm.SUCCESSFUL_UPDATE })
@Import(stylesheet = "css/together/filtercrud.css")
public class PersonUpdateForm {
	public static final String CANCEL_UPDATE = "cancelUpdate";
	public static final String SUCCESSFUL_UPDATE = "successfulUpdate";

	// Parameters

	@Parameter(required = true)
	@Property
	private Long personId;

	// Screen fields

	@Property
	private Person person;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String deleteMessage;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@EJB
	private IPersonManagerServiceLocal personManagerService;

	@Component
	private Form form;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private Messages messages;

	// The code

	// Handle event "cancelUpdate"

	boolean onCancel(Long personId) {
		// We want to tell our containing page explicitly what person we've updated, so we trigger new event
		// "successfulUpdate" with a parameter. It will bubble up because we don't have a handler method for it.
		componentResources.triggerEvent(CANCEL_UPDATE, new Object[] { personId }, null);
		// We don't want "success" to bubble up, so we return true to say we've handled it.
		return true;
	}

	// Component "updateForm" bubbles up the PREPARE_FOR_RENDER event during form render

	void onPrepareForRenderFromForm() {

		// If fresh start, make sure there's a Person object available.

		if (form.isValid()) {
			person = personFinderService.findPerson(personId);
			// Handle null person in the template.
		}
	}

	// Component "updateForm" bubbles up the PREPARE_FOR_SUBMIT event during for submission

	void onPrepareForSubmitFromForm() {
		// Get objects for the form fields to overlay.
		person = personFinderService.findPerson(personId);

		if (person == null) {
			form.recordError("Person has been deleted by another process.");
			// Instantiate an empty person to avoid NPE in the Form.
			person = new Person();
		}
	}

	// Component "updateForm" bubbles up the VALIDATE event when it is submitted

	void onValidateFromForm() {

		if (form.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
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

	// Component "updateForm" bubbles up SUCCESS or FAILURE when it is submitted, depending on whether VALIDATE
	// records an error

	boolean onSuccessFromForm() {
		// We want to tell our containing page explicitly what person we've updated, so we trigger new event
		// "successfulUpdate" with a parameter. It will bubble up because we don't have a handler method for it.
		componentResources.triggerEvent(SUCCESSFUL_UPDATE, new Object[] { personId }, null);
		// We don't want "success" to bubble up, so we return true to say we've handled it.
		return true;
	}

}
