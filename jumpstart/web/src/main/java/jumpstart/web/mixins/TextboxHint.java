/**
 * A simple mixin that uses JavaScript to observe a field, giving it a "textbox hint" when it is empty and does not have focus.
 */
package jumpstart.web.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

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

		// Give a "textbox hint" to the field we're being mixed into.

		javaScriptSupport.require("textbox-hint").with(clientElement.getClientId(), hintText, hintColor);

	}

}
