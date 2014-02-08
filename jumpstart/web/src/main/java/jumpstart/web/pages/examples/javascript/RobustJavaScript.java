package jumpstart.web.pages.examples.javascript;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(stylesheet = "css/examples/js.css")
public class RobustJavaScript {

	// Screen fields

	@Property
	private String firstName;

	@Property
	private String lastName;

	// Generally useful bits and pieces

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@InjectComponent("firstName")
	private TextField firstNameField;

	@InjectComponent("lastName")
	private TextField lastNameField;

	// The code

	public void afterRender() {

		// Give "textbox hints" to the first name and last name fields.

		javaScriptSupport.require("textbox-hint").with(firstNameField.getClientId(), "Enter First Name", "#808080");
		javaScriptSupport.require("textbox-hint").with(lastNameField.getClientId(), "Enter Last Name", "#808080");
	}

}
