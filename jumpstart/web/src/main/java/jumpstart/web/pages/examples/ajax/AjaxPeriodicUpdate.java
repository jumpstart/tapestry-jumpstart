package jumpstart.web.pages.examples.ajax;

import java.util.Date;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(stylesheet = "css/examples/js.css")
public class AjaxPeriodicUpdate {

	// Generally useful bits and pieces

	@InjectComponent
	private EventLink refreshTimeZone;

	@InjectComponent
	private Zone timeZone;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@Inject
	private Request request;

	// The code

	public void afterRender() {
		String eventURL = refreshTimeZone.getLink().toAbsoluteURI();

		javaScriptSupport.require("zone-periodic-updater").with(timeZone.getClientId(), eventURL, 3, 4);
	}

	Object onRefreshTimeZone() {
		// Here we can do whatever updates we want, then return the content we want rendered.
		return request.isXHR() ? timeZone.getBody() : null;
	}

	public Date getServerTime() {
		return new Date();
	}
}
