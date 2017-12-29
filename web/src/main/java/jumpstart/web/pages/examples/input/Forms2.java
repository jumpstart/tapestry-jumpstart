package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.annotations.Import;

@Import(stylesheet="css/examples/olive.css")
public class Forms2 {

	private String firstName;

	private String lastName;

	public void set(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	void onActivate(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	Object[] onPassivate() {
		return new String[] { firstName, lastName };
	}

	public String getName() {
		return firstName + " " + lastName;
	}
}
