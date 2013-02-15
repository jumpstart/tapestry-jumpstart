/**
 * A simple mixin that uses JavaScript to observe an element, detecting whether it has been clicked. The click will be 
 * ignored if any element using this mixin has already been clicked.
 */
package jumpstart.web.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

// The @Import tells Tapestry to put a link to the file in the head of the page so that the browser will pull it in. 
@Import(library = "ClickOnce.js")
public class ClickOnce {

	// Generally useful bits and pieces

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@InjectContainer
	private ClientElement clientElement;

	// The code

	public void afterRender() {

		// Tell the Tapestry.Initializer to do the initializing of a ClickOnce, which it will do when the DOM has been
		// fully loaded.

		JSONObject spec = new JSONObject();
		spec.put("elementId", clientElement.getClientId());
		javaScriptSupport.addInitializerCall("clickOnce", spec);
	}

}
