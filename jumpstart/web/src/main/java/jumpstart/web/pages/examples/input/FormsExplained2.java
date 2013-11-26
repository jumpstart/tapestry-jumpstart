package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.annotations.Import;

@Import(stylesheet="css/examples/olive.css")
public class FormsExplained2 {

	private String firstName;

	private String lastName;

	// set() is public so that other pages can use it to set up this page.

	public void set(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	Object[] onPassivate() {
		return new String[] { firstName, lastName };
	}

	public String getName() {
		return firstName + " " + lastName;
	}
}
