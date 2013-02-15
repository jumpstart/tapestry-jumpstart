package jumpstart.web.pages.examples.component;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class EventBubbling {

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String pageMessage;

	// The code
	
	void onEventA() {
		pageMessage = "EventA bubbled up to the page.";
	}

	void onEventB() {
		throw new IllegalStateException("Cannot happen because MyComponent aborts handling of EventB.");
	}

	void onEventC() {
		throw new IllegalStateException("Cannot happen because MyComponent aborts handling of EventC.");
	}

	void onEventD() {
		pageMessage = "EventD bubbled up to the page.";
	}

	void onEventE() {
		pageMessage = "EventE bubbled up to the page.";
	}

	void onEventX() {
		pageMessage = "EventX bubbled up to the page.";
	}
}
