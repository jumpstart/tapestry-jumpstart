package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.annotations.Property;

public class ActionLinks {

	// Screen fields
	
	@Property
	private int count;
	
	// The code

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	int onPassivate() {
		return count;
	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(int count) {
		this.count = count;
	}

	void onActionFromAddX(int amount) {
		count += amount;
	}
	
	void onActionFromAddY(int amount) {
		count += amount;
	}
	
	void onActionFromClear() {
		count = 0;
	}
	
}