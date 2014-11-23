/**
 * A simple mixin that can be applied to a field that has a Translator, eg. a TextField, PasswordField, or TextArea. 
 * It disables the field's client-side translator assigned by Tapestry.
 */
package jumpstart.web.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class ClientTranslatorDisabler {

	// Generally useful bits and pieces

	@InjectContainer
	private ClientElement attachedTo;

	@Inject
	private JavaScriptSupport javaScriptSupport;

	// The code

	public void afterRender() {
		javaScriptSupport.require("mixins/client-translator-disabler").with(attachedTo.getClientId());
	}

}
