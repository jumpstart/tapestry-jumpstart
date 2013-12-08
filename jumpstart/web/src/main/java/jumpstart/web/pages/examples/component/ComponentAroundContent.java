package jumpstart.web.pages.examples.component;

import org.apache.tapestry5.annotations.Import;

@Import(stylesheet = "css/examples/plain.css")
public class ComponentAroundContent {

	public String getMessage() {
		return "server time: " + new java.util.Date() + ".";
	}

}
