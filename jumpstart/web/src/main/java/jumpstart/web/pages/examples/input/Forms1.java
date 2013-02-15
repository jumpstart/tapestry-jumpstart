package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;

public class Forms1 {

	// Screen fields

	@Property
	private String firstName;

	@Property
	private String lastName;

	// Other pages

	@InjectPage
	private Forms2 page2;

	// Generally useful bits and pieces

	@Component(id = "names")
	private Form form;

	@Component(id = "firstName")
	private TextField firstNameField;

	@Component(id = "lastName")
	private TextField lastNameField;

	// The code

	void onValidateFromNames() {
		if (firstName == null || firstName.trim().equals("")) {
			form.recordError(firstNameField, "First Name is required.");
		}
		if (lastName == null || lastName.trim().equals("")) {
			form.recordError(lastNameField, "Last Name is required.");
		}
	}

	Object onSuccess() {
		page2.set(firstName, lastName);
		return page2;
	}
}
