package jumpstart.web.components.together.smallercomponentscrud;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * This component will trigger the following events on its container (which in this example is the page):
 * {@link PersonUpdate#CANCELED}(Long personId), {@link PersonUpdate#UPDATED}(Long personId).
 */
// @Events is applied to a component solely to document what events it may trigger. It is not checked at runtime.
@Events({ PersonUpdate.CANCELED, PersonUpdate.UPDATED })
@Import(stylesheet = "css/together/filtercrud.css")
public class PersonUpdate {
	public static final String CANCELED = "canceled";
	public static final String UPDATED = "updated";

	// Parameters

	@Parameter(required = true)
	@Property
	private Long personId;

	// Screen fields

	@Property
	private Person person;

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

	boolean onCancel(Long personId) {
		// We want to tell our containing page explicitly what person we've updated, so we trigger a new event
		// with a parameter. It will bubble up because we don't have a handler method for it.
		componentResources.triggerEvent(CANCELED, new Object[] { personId }, null);
		// We don't want "success" to bubble up, so we return true to say we've handled it.
		return true;
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

	boolean onValidateFromForm() {

		if (form.getHasErrors()) {
			return true;
		}

		try {
			personManagerService.changePerson(person);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			form.recordError(ExceptionUtil.getRootCauseMessage(e));
		}

		return true;
	}

	boolean onSuccess() {
		// We want to tell our containing page explicitly what person we've updated, so we trigger a new event
		// with a parameter. It will bubble up because we don't have a handler method for it.
		componentResources.triggerEvent(UPDATED, new Object[] { personId }, null);
		// We don't want "success" to bubble up, so we return true to say we've handled it.
		return true;
	}

}
