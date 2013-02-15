package jumpstart.web.pages.examples.ajax;

import java.util.Date;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

public class ZoneWithoutYellowHighlight {

	// Generally useful bits and pieces

	@Inject
	private Request request;

	@InjectComponent
	private Zone time2Zone;

	// The code

	void onRefreshPage() {
		// Nothing to do - the page will call getTime1() and getTime2() as it renders.
	}

	// Isn't called if the link is clicked before the DOM is fully loaded. See
	// https://issues.apache.org/jira/browse/TAP5-1 .
	Object onRefreshZone() {

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
