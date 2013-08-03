package jumpstart.web.pages.examples.javascript;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

// The @Import tells Tapestry to put a link to the file in the head of the page so that the browser will pull it in. 
@Import(library = "context:js/robust_textbox_hint.js")
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

		// Tell the Tapestry.Initializer to do the initializing of our 2 TextboxHints, which it will do when the DOM has
		// been fully loaded.

		JSONObject spec = new JSONObject();
		spec.put("textboxId", firstNameField.getClientId());
		spec.put("hintText", "Enter First Name");
		spec.put("hintColor", "#808080");
		javaScriptSupport.addInitializerCall("textboxHint", spec);

		JSONObject spec2 = new JSONObject();
		spec2.put("textboxId", lastNameField.getClientId());
		spec2.put("hintText", "Enter Last Name");
		spec2.put("hintColor", "#808080");
		javaScriptSupport.addInitializerCall("textboxHint", spec2);
	}

}
