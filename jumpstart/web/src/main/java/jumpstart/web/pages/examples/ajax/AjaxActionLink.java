package jumpstart.web.pages.examples.ajax;

import java.util.Date;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

@Import(stylesheet = "css/examples/js.css")
public class AjaxActionLink {

	// Generally useful bits and pieces

	@Inject
	private Request request;

	@InjectComponent
	private Zone time2Zone;

	// The code

	void onActionFromRefreshPage() {
		// This method isn't needed, because there's nothing to do - when the page renders it will call getServerTime1()
		// and getServerTime2().
	}

	// Isn't called if the link is clicked before the DOM is fully loaded. See
	// https://issues.apache.org/jira/browse/TAP5-1 .
	Object onActionFromRefreshZone() {
		// Here we can do whatever updates we want, then return the content we want rendered.
		return request.isXHR() ? time2Zone.getBody() : null;
	}

	public Date getServerTime1() {
		return new Date();
	}

	public Date getServerTime2() {
		return new Date();
	}
}
