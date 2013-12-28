package jumpstart.web.models.examples;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jumpstart.business.domain.person.Person;

public class Invitation {

	@NotNull
	@Size(max = 50)
	private String eventDescription;

	private Set<Person> invitedPersons;

	public String toString() {
		final String DIVIDER = ", ";

		StringBuilder buf = new StringBuilder();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("eventDescription=" + eventDescription + DIVIDER);
		buf.append("invitedPersons=" + invitedPersons);
		buf.append("]");
		return buf.toString();
	}

	public Invitation() {
		eventDescription = null;
		invitedPersons = new HashSet<Person>();
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public Set<Person> getInvitedPersons() {
		return invitedPersons;
	}

	public void setInvitedPersons(Set<Person> invitedPersons) {
		this.invitedPersons = invitedPersons;
	}

}
