package jumpstart.web.pages.examples.lang;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;

@Import(stylesheet = "css/examples/js.css")
public class Mixins {
	// Screen fields

	@Property
	@NotNull
	@Size(max = 10)
	private String firstName;

	@Property
	@NotNull
	@Size(max = 10)
	private String lastName;

	// Generally useful bits and pieces

	@Component(id = "names")
	private Form form;

	@Component(id = "firstName")
	private TextField firstNameField;

	// The code

	void onValidateFromNames() {
		// Note, this method is triggered even if server-side validation has already found error(s).

		if (firstName != null && firstName.equals("Acme")) {
			form.recordError(firstNameField, "First Name must not be Acme.");
		}
	}

	Object onSuccess() {
		// In a real application we would go to the next page.
		return null;
	}
}
