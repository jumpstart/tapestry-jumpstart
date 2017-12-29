package jumpstart.web.components.examples.component;

import java.util.Set;

import jumpstart.business.domain.person.Person;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.FormSupport;

public class SelectPersonsValidated {

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

	// Screen fields

	@Property
	private Person person;

	// Generally useful bits and pieces

	@Environmental
	private FormSupport formSupport;

	@Environmental
	private ValidationTracker tracker;

	private static final ProcessSubmission PROCESS_SUBMISSION = new ProcessSubmission();

	// The code

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

	private static class ProcessSubmission implements ComponentAction<SelectPersonsValidated> {
		private static final long serialVersionUID = 1L;

		public void execute(SelectPersonsValidated component) {
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
			tracker.recordError("You must choose at least " + min + " person(s).");
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

}
