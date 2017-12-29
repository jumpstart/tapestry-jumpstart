package jumpstart.web.pages.examples.navigation;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(stylesheet = "css/examples/js.css")
public class Tabs {

	// Useful bits and pieces

	@Inject
	private JavaScriptSupport javaScriptSupport;

	// The code

	void afterRender() {
		javaScriptSupport.require("bootstrap/tab");
	}

}
