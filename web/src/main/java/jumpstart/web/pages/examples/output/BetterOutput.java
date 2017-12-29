package jumpstart.web.pages.examples.output;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet="css/examples/olive.css")
public class BetterOutput {

	@Property
	private String message;

	void setupRender() {
		message = "server time: " + new java.util.Date() + ".";
	}

}
