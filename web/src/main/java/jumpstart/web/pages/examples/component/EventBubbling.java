package jumpstart.web.pages.examples.component;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet = "css/examples/plain.css")
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
		throw new IllegalStateException("Cannot happen because there is no EventE.");
	}

	void onEventX() {
		pageMessage = "EventX bubbled up to the page.";
	}
}
