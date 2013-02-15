package jumpstart.web.pages.examples.tables;

import org.apache.tapestry5.annotations.Property;

public class LinkingGrid2 {
	
	// Screen fields

	@Property
	private String firstName;
	
	// The code
	
	String onPassivate() {
		return firstName;
	}

	void onActivate(String firstName) {
		this.firstName = firstName;
	}
}