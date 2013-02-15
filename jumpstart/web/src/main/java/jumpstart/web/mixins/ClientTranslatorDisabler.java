/**
 * A simple mixin that can be applied to a field that has a Translator, eg. a TextField, PasswordField, or TextArea. 
 * It replaces the default client-side translator assigned by Tapestry with one that does nothing.
 */
package jumpstart.web.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

// The @Import tells Tapestry to put a link to the file in the head of the page so that the browser will pull it in. 
@Import(library = "ClientTranslatorDisabler.js")
public class ClientTranslatorDisabler {

	// Generally useful bits and pieces

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@InjectContainer
	private ClientElement clientElement;

	// The code

	public void afterRender() {

		// Tell the Tapestry.Initializer to do the initializing of a ClientTranslatorDisabler, which it will do when the
		// DOM has been fully loaded.

		JSONObject spec = new JSONObject();
		spec.put("fieldId", clientElement.getClientId());
		javaScriptSupport.addInitializerCall(InitializationPriority.LATE, "clientTranslatorDisabler", spec);
	}

}
