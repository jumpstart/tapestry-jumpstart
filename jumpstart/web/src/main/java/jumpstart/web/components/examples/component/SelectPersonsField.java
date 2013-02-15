package jumpstart.web.components.examples.component;

import java.util.Set;

import jumpstart.business.domain.person.Person;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.FormSupport;

@Events(EventConstants.VALIDATE)
public class SelectPersonsField implements Field {

	// Parameters

	@Parameter(required = true, allowNull = false)
	@Property
	private Iterable<Person> persons;

	@Parameter(required = true, allowNull = false, name = "chosen")
	@Property
	private Set<Person> chosenPersons;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "0")
	@Property
	private int min;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "false")
	@Property
	private boolean disabled;

	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String label;

	// Screen fields

	@Property
	private Person person;

	// Generally useful bits and pieces

	private String controlName;

	@Environmental
	private FormSupport formSupport;

	@Environmental
	private ValidationTracker tracker;

	private static final ProcessSubmission PROCESS_SUBMISSION = new ProcessSubmission();

	// The code

	// Tapestry calls setupRender() before it renders me and BEFORE it renders any components I contain (ie. Loop).

	final void setupRender() {

		// If we are inside a form, ask FormSupport to execute "setup" now AND store it in its list of actions to do on
		// submit.
		// If I contain other components, their actions will be stored later in the list, after "setup". That is because
		// this method, setupRender(), is early in the sequence. This guarantees "setup" will be executed on submit
		// BEFORE the components I contain are processed (including their validation).

		if (formSupport != null) {
			String controlName = formSupport.allocateControlName(clientId);
			ComponentAction<SelectPersonsField> setup = new Setup(controlName);
			formSupport.storeAndExecute(this, setup);
		}

	}

	private static class Setup implements ComponentAction<SelectPersonsField> {
		private static final long serialVersionUID = 1L;

		private final String controlName;

		Setup(String controlName) {
			this.controlName = controlName;
		}

		public void execute(SelectPersonsField component) {
			component.setup(controlName);
		}

		@Override
		public String toString() {
			return String.format(this.getClass().getSimpleName() + ".Setup[%s]", controlName);
		}
	}

	private void setup(String controlName) {
		this.controlName = controlName;
	}

	// Tapestry calls afterRender() AFTER it renders any components I contain (ie. Loop).

	final void afterRender() {

		// If we are inside a form, ask FormSupport to store PROCESS_SUBMISSION in its list of actions to do on submit.
		// If I contain other components, their actions will already be in the list, before PROCESS_SUBMISSION. That is
		// because this method, afterRender(), is late in the sequence. This guarantees PROCESS_SUBMISSION will be
		// executed on submit AFTER the components I contain are processed (which includes their validation).

		if (formSupport != null) {
			formSupport.store(this, PROCESS_SUBMISSION);
		}
	}

	private static class ProcessSubmission implements ComponentAction<SelectPersonsField> {
		private static final long serialVersionUID = 1L;

		public void execute(SelectPersonsField component) {
			component.processSubmission();
		}

		@Override
		public String toString() {
			return this.getClass().getSimpleName() + ".ProcessSubmission";
		}
	};

	private void processSubmission() {

		// Validate. We ensured in afterRender() that the components I contain have already been validated.

		// Error if the number of persons chosen is less than specified by the min parameter.

		if (chosenPersons.size() < min) {
			tracker.recordError(this, "You must choose at least " + min + " person(s).");
			return;
		}

	}

	// The Loop component will automatically call this for every row as it is rendered.
	public boolean isPersonChosen() {
		return chosenPersons.contains(person);
	}

	// The Loop component will automatically call this for every row on submit.
	public void setPersonChosen(boolean personChosen) {
		if (personChosen) {
			chosenPersons.add(person);
		}
		else {
			chosenPersons.remove(person);
		}
	}

	@Override
	public String getClientId() {
		return null;
	}

	@Override
	public String getControlName() {
		return controlName;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public boolean isDisabled() {
		return disabled;
	}

	@Override
	public boolean isRequired() {
		return true;
	}

}
