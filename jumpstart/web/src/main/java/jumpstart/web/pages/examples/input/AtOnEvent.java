package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;

public class AtOnEvent {

	// Screen fields

	@Property
	private int count;

	// The code

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	int onPassivate() {
		return count;
	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	@OnEvent(value = EventConstants.ACTIVATE)
	void startMeUp(int count) {
		this.count = count;
	}

	@OnEvent(value = "add")
	void doSomeAdding(int amount) {
		count += amount;
	}

	@OnEvent(value = "clear")
	void resetTheCount() {
		count = 0;
	}

}
