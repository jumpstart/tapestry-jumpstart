package jumpstart.web.pages.examples.ajax;

import java.util.Date;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

@Import(stylesheet = "css/examples/js.css")
public class AjaxEventLink {

	// Generally useful bits and pieces

	@Inject
	private Request request;

	@InjectComponent
	private Zone time2Zone;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	// The code

	void onRefreshPage() {
		// Nothing to do - as the page renders the template it will call getServerTime1() and getServerTime2().
	}

	void onRefreshZone() {
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(time2Zone);
		}
	}

	public Date getServerTime1() {
		return new Date();
	}

	public Date getServerTime2() {
		return new Date();
	}
}
