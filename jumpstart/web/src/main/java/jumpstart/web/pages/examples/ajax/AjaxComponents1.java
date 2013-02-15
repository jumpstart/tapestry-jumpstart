package jumpstart.web.pages.examples.ajax;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;

public class AjaxComponents1 {

	// Screen fields

	@Property
	private String firstName;

	@Property
	private String lastName;

	// Other pages

	@InjectPage
	private AjaxComponents2 page2;

	// The code

	void setupRender() {
		if (firstName == null && lastName == null) {
			firstName = "Humpty";
			lastName = "Dumpty";
		}
	}

	Object onSuccess() {
		page2.set(firstName, lastName);
		return page2;
	}

}
