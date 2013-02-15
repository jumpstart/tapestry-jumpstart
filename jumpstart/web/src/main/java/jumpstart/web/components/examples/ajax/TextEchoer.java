package jumpstart.web.components.examples.ajax;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

/**
 * TextEchoer contains a TextField and a Zone that "echoes" the value of the TextField.
 */
public class TextEchoer {

	@Parameter(required = true, principal = true, autoconnect = true)
	@Property
	private String value;

	@InjectComponent
	private Zone echoZone;

	@Inject
	private Request request;

	Object onValueChanged() {
		value = request.getParameter("param");
		if (value == null) {
			value = "";
		}
		return request.isXHR() ? echoZone.getBody() : null;
	}

	public String getEcho() {
		return value;
	}

}
