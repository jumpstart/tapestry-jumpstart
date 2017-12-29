/**
 * A simple mixin for attaching javascript to a Zone that periodically issues an AJAX request to update the Zone.
 * Loosely based on Tapestry's ZoneRefresh.
 */
package jumpstart.web.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

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
	private Zone zone;

	// The code

	void afterRender() {
		String eventURL = componentResources.createEventLink(event, context).toAbsoluteURI();

		javaScriptSupport.require("zone-periodic-updater")
				.with(zone.getClientId(), eventURL, frequencySecs, maxUpdates);
	}

}
