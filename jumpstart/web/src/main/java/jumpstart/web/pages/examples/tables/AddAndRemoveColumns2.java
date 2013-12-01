package jumpstart.web.pages.examples.tables;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet = "css/examples/plain.css")
public class AddAndRemoveColumns2 {

	// Screen fields

	@Property
	private String firstName;
	
	// The code

	void onActivate(String firstName) {
		this.firstName = firstName;
	}
	
	String onPassivate() {
		return firstName;
	}
}