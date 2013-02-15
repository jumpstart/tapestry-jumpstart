package jumpstart.web.pages.examples.state;

import jumpstart.web.pages.Index;
import jumpstart.web.state.examples.state.ShoppingBasket;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

public class SharingAcrossMultiplePages2 {

	// Screen fields

	@SessionState
	@Property
	private ShoppingBasket myBasket;
	
	// The code

	Object onGoHome() {
		// Delete the SSO from the session
		myBasket = null;
		return Index.class;
	}

}
