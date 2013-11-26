package jumpstart.web.pages.examples.navigation;

import org.apache.tapestry5.annotations.Import;

@Import(stylesheet="css/examples/olive.css")
public class ReturnTypesPageObject {

	private String parameter;

	// set() is public so that other pages can use it to set up this page.
	
	public void set(String parameter) {
		this.parameter = parameter;
	}
	
	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(String parameter) {
		this.parameter = parameter;
	}

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.
	
	String onPassivate() {
		return parameter;
	}

	public String getParameter() {
		return parameter;
	}
}
