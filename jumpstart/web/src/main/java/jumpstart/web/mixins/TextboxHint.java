/**
 * A simple mixin that uses JavaScript to observe a field, giving it a "textbox hint" when it is empty and does not have focus.
 */
package jumpstart.web.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

// The @Import tells Tapestry to put a link to the file in the head of the page so that the browser will pull it in. 
@Import(library = "TextboxHint.js")
public class TextboxHint {

	// Parameters

	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String hintText;

	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String hintColor;

	// Generally useful bits and pieces

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@InjectContainer
	private ClientElement clientElement;

	// The code

	public void afterRender() {

		// Tell the Tapestry.Initializer to do the initializing of a TextboxHint, which it will do when the DOM has
		// been fully loaded.

		JSONObject spec = new JSONObject();
		spec.put("textboxId", clientElement.getClientId());
		spec.put("hintText", hintText);
		spec.put("hintColor", hintColor);
		javaScriptSupport.addInitializerCall("textboxHint", spec);
	}

}
