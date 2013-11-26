package jumpstart.web.pages.examples.tables;

import org.apache.tapestry5.annotations.Property;

public class GridBeanModel2 {

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