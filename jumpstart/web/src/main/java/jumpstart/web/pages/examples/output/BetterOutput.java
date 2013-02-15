package jumpstart.web.pages.examples.output;

import org.apache.tapestry5.annotations.Property;

public class BetterOutput {

	@Property
	private String message;

	void setupRender() {
		message = "server time: " + new java.util.Date() + ".";
	}

}
