package jumpstart.web.pages.examples.ajax;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.services.javascript.StylesheetLink;
import org.apache.tapestry5.services.javascript.StylesheetOptions;

// The @Import tells Tapestry to put a link to the file in the head of the page so that the browser will pull it in. 
@Import(library = "context:js/zone-overlay.js", stylesheet = "context:css/zone-overlay.css")
public class AjaxLoadingSpinner {
	static final private String[] ALL_THINGS = { "Sugar", "Spice", "All Things Nice" };

	// Screen fields

	@Property
	private String[] things;

	@Property
	private String thing;

	// Generally useful bits and pieces

	@Inject
	private Request request;

	@InjectComponent
	private Zone thingsZone;

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@Inject
	@Path("context:css/zone-overlay-ie.css")
	private Asset ieCSS;

	// The code

	void afterRender() {

		// Add a stylesheet that is conditional on whether the browser is Internet Explorer. We would do this in the
		// @Import above except that it doesn't have the syntax to specify a condition.

		javaScriptSupport.importStylesheet(new StylesheetLink(ieCSS, new StylesheetOptions().withCondition("IE")));
	}

	Object onShowThings() {

		// Set up the list of things to display
		things = ALL_THINGS;

		// Sleep 4 seconds to simulate a long-running operation
		sleep(4000);

		return request.isXHR() ? thingsZone.getBody() : null;
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			// Ignore
		}
	}

}
