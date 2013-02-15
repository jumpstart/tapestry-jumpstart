package jumpstart.web.pages.examples.javascript;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

// The @Import tells Tapestry to put a link to the file in the head of the page so that the browser will pull it in. 
@Import(library = "context:js/jq_robust_textbox_hint.js")
public class JQuery {

	// Screen fields

	@Property
	private String firstName;

	@Property
	private String lastName;

	@Property
	private String firstNameId;

	@Property
	private String lastNameId;

	// Generally useful bits and pieces

	@Inject
	private JavaScriptSupport javaScriptSupport;

	// The code

	public void setupRender() {
		firstNameId = "firstName";
		lastNameId = "lastName";

		// Tell the Tapestry.Initializer to do the initializing of our 2 TextboxHints, which it will do when the DOM has
		// been fully loaded.

		JSONObject spec = new JSONObject();
		spec.put("textboxId", firstNameId);
		spec.put("hintText", "Enter First Name");
		spec.put("hintColor", "#808080");
		javaScriptSupport.addInitializerCall("textboxHint", spec);

		JSONObject spec2 = new JSONObject();
		spec2.put("textboxId", lastNameId);
		spec2.put("hintText", "Enter Last Name");
		spec2.put("hintColor", "#808080");
		javaScriptSupport.addInitializerCall("textboxHint", spec2);
	}

}
