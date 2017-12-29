/**
 * A simple mixin that uses JavaScript to observe an element, detecting whether it has been clicked. The click will be 
 * ignored if any element using this mixin has already been clicked.
 */
package jumpstart.web.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class ClickOnce {

	// Generally useful bits and pieces

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@InjectContainer
	private ClientElement attachedTo;

	// The code

	public void afterRender() {
		JSONObject spec = new JSONObject();
		spec.put("elementId", attachedTo.getClientId());
		javaScriptSupport.require("click-once").invoke("init").with(spec);
	}

}
