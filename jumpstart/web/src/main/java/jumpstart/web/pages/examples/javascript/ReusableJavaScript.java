package jumpstart.web.pages.examples.javascript;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(stylesheet = "css/examples/js.css")
public class ReusableJavaScript {

	// Screen fields

	@Property
	private String firstName;

	@Property
	private String lastName;

	// Generally useful bits and pieces

	@Inject
	private JavaScriptSupport javaScriptSupport;

	// The code

	public void afterRender() {

		// Give "textbox hints" to the first name and last name fields.

		javaScriptSupport.require("textbox-hint").with("firstName", "Enter First Name", "#808080");
		javaScriptSupport.require("textbox-hint").with("lastName", "Enter Last Name", "#808080");
	}

}
