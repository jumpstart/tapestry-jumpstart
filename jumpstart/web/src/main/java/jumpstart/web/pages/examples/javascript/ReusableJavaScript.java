package jumpstart.web.pages.examples.javascript;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

// The @Import tells Tapestry to put a link to the file in the head of the page so that the browser will pull it in. 
@Import(library = "context:js/textbox_hint.js")
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

		// Add some JavaScript to the page to instantiate a TextboxHint, twice. It will run when the DOM has been fully
		// loaded.

		javaScriptSupport.addScript(String.format("new TextboxHint('%s', '%s', '%s');", "firstName",
				"Enter First Name", "#808080"));
		javaScriptSupport.addScript(String.format("new TextboxHint('%s', '%s', '%s');", "lastName", "Enter Last Name",
				"#808080"));
	}

}
