package jumpstart.web.pages.examples.ajax;

import java.util.Date;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

@Import(stylesheet = "css/examples/js.css")
public class AjaxPeriodicUpdateMixin {

	// Generally useful bits and pieces

	@InjectComponent
	private Zone timeZone;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@Inject
	private Request request;

	// The code

	void onRefreshTimeZone() {
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(timeZone);
		}
	}

	public Date getServerTime() {
		return new Date();
	}
}
