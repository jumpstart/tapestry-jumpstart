package jumpstart.web.pages.examples.output;

import org.apache.tapestry5.annotations.Import;

@Import(stylesheet="css/examples/olive.css")
public class Output {

	public String getMessage() {
		return "I am a message from the server, where the time is " + new java.util.Date() + ".";
	}

}