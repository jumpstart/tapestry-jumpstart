/**
 * A simple mixin that can be applied to a field that has a Translator, eg. a TextField, PasswordField, or TextArea. 
 * It replaces the default client-side translator assigned by Tapestry with one that does nothing.
 */
package jumpstart.web.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class ClientTranslatorDisabler {

	// Generally useful bits and pieces

	@InjectContainer
	private ClientElement clientElement;

	@Inject
	private JavaScriptSupport javaScriptSupport;

	// The code

	public void afterRender() {
		JSONObject spec = new JSONObject();
		spec.put("fieldId", clientElement.getClientId());
		javaScriptSupport.require("mixins/client-translator-disabler").with(spec);
	}

}
