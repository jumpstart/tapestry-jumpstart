package jumpstart.web.pages.examples.component;

import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.components.CustomForm;
import jumpstart.web.model.examples.Invitation;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

public class SubFormValidation1 {
	static private final int MAX_RESULTS = 30;

	// Screen fields

	@Property
	@SessionState(create = false)
	private Invitation invitation;

	@Property
	private List<Person> allPersons;

	// Other pages

	@InjectPage
	private SubFormValidation2 page2;

	// Generally useful bits and pieces.

	@Component(id = "form")
	private CustomForm form;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	// The code

	// Form bubbles up the PREPARE event during form render and form submission.

	void onPrepare() {
		invitation = new Invitation();
		allPersons = personFinderService.findPersons(MAX_RESULTS);
	}

	void onValidateFromForm() {

		if (form.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		// Create the invitation

		try {
			// In a real application you would persist the invitation to the database
			// personManagerService.createInvitation(invitation.eventDescription, invitation.invitedPersons);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			form.recordError(ExceptionUtil.getRootCauseMessage(e));
		}

	}

	Object onSuccess() {
		// In a real application you would pass the invitation id instead of the invitation.
		page2.set(invitation);
		return page2;
	}

}
