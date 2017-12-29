package jumpstart.web.pages.examples.javascript;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class Scriptaculous {

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

		// Add some JavaScript to the page to "grow" the element called "expando". It will run when the DOM has been
		// fully loaded.

		javaScriptSupport.addScript(InitializationPriority.LATE, String.format("Effect.Grow('%s');", "expando"));
	}

}
