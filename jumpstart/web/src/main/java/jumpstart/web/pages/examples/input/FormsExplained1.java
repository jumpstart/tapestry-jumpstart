package jumpstart.web.pages.examples.input;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;

@Import(stylesheet = "css/examples/plain.css")
public class FormsExplained1 {

	// Screen fields

	@Property
	@NotNull
	@Size(max=10)
	private String firstName;

	@Property
	@NotNull
	@Size(max=10)
	private String lastName;

	// Other pages

	@InjectPage
	private FormsExplained2 page2;

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
		page2.set(firstName, lastName);
		return page2;
	}
}
