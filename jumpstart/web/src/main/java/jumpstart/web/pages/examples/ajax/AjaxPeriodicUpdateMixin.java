package jumpstart.web.pages.examples.ajax;

import java.util.Date;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

public class AjaxPeriodicUpdateMixin {

	// Generally useful bits and pieces

	@InjectComponent
	private Zone timeZone;

	@Inject
	private Request request;

	// The code

	Object onRefreshTimeZone() {
		// Here we can do whatever updates we want, then return the content we want rendered.
		return request.isXHR() ? timeZone.getBody() : null;
	}

	public Date getServerTime() {
		return new Date();
	}
}
