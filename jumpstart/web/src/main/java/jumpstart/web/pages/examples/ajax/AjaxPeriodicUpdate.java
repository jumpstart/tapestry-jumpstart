package jumpstart.web.pages.examples.ajax;

import java.util.Date;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

// The @Import tells Tapestry to put a link to the file in the head of the page so that the browser will pull it in. 
@Import(library = "AjaxPeriodicUpdate.js")
public class AjaxPeriodicUpdate {

	// Generally useful bits and pieces

	@InjectComponent
	private Zone timeZone;

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@Inject
	private Request request;

	// The code

	public void afterRender() {
		javaScriptSupport.addInitializerCall("periodicTimeZoneUpdater", new JSONObject());
	}

	Object onRefreshTimeZone() {
		// Here we can do whatever updates we want, then return the content we want rendered.
		return request.isXHR() ? timeZone.getBody() : null;
	}

	public Date getServerTime() {
		return new Date();
	}
}
