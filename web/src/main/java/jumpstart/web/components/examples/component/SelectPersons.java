package jumpstart.web.components.examples.component;

import java.util.Set;

import jumpstart.business.domain.person.Person;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class SelectPersons {

	// Parameters

	@Parameter(required = true, allowNull = false)
	@Property
	private Iterable<Person> persons;

	@Parameter(required = true, allowNull = false, name = "chosen")
	@Property
	private Set<Person> chosenPersons;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "false")
	@Property
	private boolean disabled;

	// Screen fields

	@Property
	private Person person;

	// The code

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
