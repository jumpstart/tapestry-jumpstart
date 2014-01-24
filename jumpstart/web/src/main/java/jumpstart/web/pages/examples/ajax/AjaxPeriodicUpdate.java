package jumpstart.web.pages.examples.ajax;

import java.util.Date;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(stylesheet = "css/examples/js.css")
public class AjaxPeriodicUpdate {

	// Generally useful bits and pieces

	@InjectComponent
	private EventLink refreshTimeZone;

	@InjectComponent
	private Zone timeZone;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

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

	void onRefreshTimeZone() {
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(timeZone);
		}
	}

	public Date getServerTime() {
		return new Date();
	}
}
