package jumpstart.web.pages.examples.javascript;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

// The @Import tells Tapestry to put a link to the file in the head of the page so that the browser will pull it in.
@Import(library = "JavaScript.js")
public class JavaScript {

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

		// Add some JavaScript to the page to instantiate a ColorSwitcher. It will run when the DOM has been fully
		// loaded.

		javaScriptSupport.addScript("new ColorSwitcher();");
	}

}
