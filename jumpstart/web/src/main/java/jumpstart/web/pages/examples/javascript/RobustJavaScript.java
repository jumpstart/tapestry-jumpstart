package jumpstart.web.pages.examples.javascript;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
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

	@Component(id = "firstName")
	private TextField firstNameField;

	@Component(id = "lastName")
	private TextField lastNameField;

	// The code

	public void afterRender() {
		
		// Give "textbox hints" to the first name and last name fields.

		JSONObject params1 = new JSONObject();
		params1.put("textboxId", firstNameField.getClientId());
		params1.put("hintText", "Enter First Name");
		params1.put("hintColor", "#808080");
		javaScriptSupport.require("textbox-hint").with(params1);

		JSONObject params2 = new JSONObject();
		params2.put("textboxId", lastNameField.getClientId());
		params2.put("hintText", "Enter Last Name");
		params2.put("hintColor", "#808080");
		javaScriptSupport.require("textbox-hint").with(params2);

	}

}
