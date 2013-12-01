package jumpstart.web.pages.examples.javascript;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet = "css/examples/js.css")
public class JavaScriptMixin {

	// Screen fields

	@Property
	private String firstName;

	@Property
	private String lastName;

}
