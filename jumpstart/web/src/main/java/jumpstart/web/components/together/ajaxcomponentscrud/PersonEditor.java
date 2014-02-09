package jumpstart.web.components.together.ajaxcomponentscrud;

import java.text.Format;
import java.text.SimpleDateFormat;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;

import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.FormSupport;

public class PersonEditor {

	// Parameters

	@Parameter(required = true, allowNull = false)
	@Property
	private Person person;

	@Parameter(value = "false")
	@Property
	private boolean disabled;

	// Generally useful bits and pieces

	@InjectComponent("firstName")
	private TextField firstNameField;
	
	@Inject
	private Messages messages;

	@Environmental
	private FormSupport formSupport;

	@Environmental
	private ValidationTracker tracker;

	private static final ProcessSubmission PROCESS_SUBMISSION = new ProcessSubmission();

	// The code

	void afterRender() {

		// If we are inside a form, ask FormSupport to store PROCESS_SUBMISSION in its list of actions to do on submit.
		// If I contain other components, their actions will already be in the list, before PROCESS_SUBMISSION. That is
		// because this method, afterRender(), is late in the sequence. This guarantees PROCESS_SUBMISSION will be
		// executed on submit AFTER the components I contain are processed (which includes their validation).

		if (formSupport != null) {
			formSupport.store(this, PROCESS_SUBMISSION);
		}

	}

	private static class ProcessSubmission implements ComponentAction<PersonEditor> {
		private static final long serialVersionUID = 1L;

		public void execute(PersonEditor component) {
			component.processSubmission();
		}

		@Override
		public String toString() {
			return this.getClass().getSimpleName() + ".ProcessSubmission";
		}
	};

	private void processSubmission() {

		// Validate. We ensured in afterRender() that the components I contain have already been validated.

		if (person.getFirstName() != null && person.getFirstName().equals("Acme")) {
			tracker.recordError(firstNameField, firstNameField.getLabel() + " must not be Acme.");
		}

		if (person.getId() != null && person.getId() == 2 && !person.getFirstName().equals("Mary")) {
			tracker.recordError(firstNameField, firstNameField.getLabel() + " for this person must be Mary.");
		}

	}

	// ////////////////////////////////////////////////////////////////////////
	// GETTERS ETC.
	// ////////////////////////////////////////////////////////////////////////

	public String getPersonRegion() {
		return messages.get(Regions.class.getSimpleName() + "." + person.getRegion().name());
	}

	public String getDatePattern() {
		return "dd/MM/yyyy";
	}

	public Format getDateFormat() {
		return new SimpleDateFormat(getDatePattern());
	}
}
