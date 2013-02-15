/**
 * A simple mixin for attaching javascript to a Zone that periodically issues an AJAX request to update the Zone
 * Based on a solution by Taha Hafeez at
 * http://tapestry.1045711.n5.nabble.com/T5-Complexity-for-simple-things-where-is-the-documentation-tt3214893.html.
 */
package jumpstart.web.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

// The @Import tells Tapestry to put a link to the file in the head of the page so that the browser will pull it in. 
@Import(library = "ZonePeriodicUpdater.js")
public class ZonePeriodicUpdater {
	
	// Parameters

	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String event;

	@Parameter
	private Object[] context;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, required = true)
	private int frequencySecs;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, required = true)
	private int maxUpdates;

	// Useful bits and pieces

	@Inject
	private ComponentResources componentResources;

	@Inject
	private JavaScriptSupport javaScriptSupport;

	/**
	 * The element we attach ourselves to, which must be a Zone 
	 */
	@InjectContainer
	private ClientElement clientElement;

	// The code

	void afterRender() {

		// Tell the Tapestry.Initializer to do the initializing of a ZonePeriodicUpdater, which it will do when the DOM has
		// been fully loaded.

		JSONObject spec = new JSONObject();
		spec.put("zoneElementId", clientElement.getClientId());
		spec.put("eventURL", componentResources.createEventLink(event, context).toAbsoluteURI());
		spec.put("frequencySecs", frequencySecs);
		spec.put("maxUpdates", maxUpdates);
		javaScriptSupport.addInitializerCall("zonePeriodicUpdater", spec);
	}

}
