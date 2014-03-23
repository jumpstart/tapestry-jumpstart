package jumpstart.web.components.examples.component;

import java.util.Set;

import jumpstart.business.domain.person.Person;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.services.FormSupport;

@Events(EventConstants.VALIDATE)
public class SelectPersonsField extends AbstractField {

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

	// Screen fields

	@Property
	private Person person;

	// Generally useful bits and pieces

	@Environmental
	private FormSupport formSupport;

	@Environmental
	private ValidationTracker tracker;

	private static final ProcessSubmissionAfter PROCESS_SUBMISSION_AFTER = new ProcessSubmissionAfter();

	// The code

	// Tapestry calls afterRender() AFTER it renders any components I contain (ie. Loop).

	final void afterRender() {

		// If we are inside a form, ask FormSupport to store PROCESS_SUBMISSION in its list of actions to do on submit.
		// If I contain other components (which I do), their actions will already be in the list, before
		// PROCESS_SUBMISSION. That is because this method, afterRender(), is late in the sequence. This guarantees
		// PROCESS_SUBMISSION will be executed on submit AFTER the components I contain are processed (which includes
		// their validation).

		if (formSupport != null) {
			formSupport.store(this, PROCESS_SUBMISSION_AFTER);
		}
	}

	private static class ProcessSubmissionAfter implements ComponentAction<SelectPersonsField> {
		private static final long serialVersionUID = 1L;

		public void execute(SelectPersonsField component) {
			component.processSubmissionAfter();
		}

		@Override
		public String toString() {
			return this.getClass().getSimpleName() + ".ProcessSubmissionAfter";
		}
	};

	@Override
	protected void processSubmission(final String controlName) {
		// Nothing to do yet, because it's before my components are handled.
	}

	protected void processSubmissionAfter() {

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
	public boolean isRequired() {
		return true;
	}

}
