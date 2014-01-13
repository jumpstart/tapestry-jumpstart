package jumpstart.web.pages.examples.state;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

/**
 * This page demonstrates using the activation context to remember data through the redirect.
 * The data will be appended to the page render request URL.
 */
@Import(stylesheet="css/examples/olive.css")
public class PassingByActivationContext {

	// The activation context

	@Property
	private long personId;

	// The code
	
	// set() is public so that other pages can use it to set up this page.
	
	public void set(Long personId) {
		this.personId = personId;
	}
	
	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(Long personId) {
		this.personId = personId;
	}

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.
	// To return more than one parameter, use Long[], or List<Long>, or Object[], or List<Object>.
	
	Long onPassivate() {
		return personId;
	}
}
