package jumpstart.web.model.examples;

import jumpstart.business.domain.person.Person;

public class PersonModification {

	private String reason;
	private Person person;

	public String toString() {
		final String DIVIDER = ", ";

		StringBuilder buf = new StringBuilder();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("reason=" + reason + DIVIDER);
		buf.append("person=" + person);
		buf.append("]");
		return buf.toString();
	}

	public PersonModification(String reason, Person person) {
		this.reason = reason;
		this.person = person;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}
