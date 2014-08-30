package jumpstart.web.pages.examples.component;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet = "css/examples/plain.css")
public class EventBubblingWithContext {

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String pageMessage;

	// The code

	void onEventA(String s) {
		pageMessage = "EventA bubbled up to the page. Context = \"" + s + "\".";
	}

	void onEventB(EventContext eventContext) {
		throw new IllegalStateException("Cannot happen because MyComponent aborts handling of EventB.");
	}

	void onEventC(EventContext eventContext) {
		throw new IllegalStateException("Cannot happen because MyComponent aborts handling of EventC.");
	}

	void onEventD(EventContext eventContext) {
		pageMessage = "EventD bubbled up to the page. Context = '" + eventContext.get(String.class, 0) + "'.";
	}

	void onEventE(EventContext eventContext) {
		throw new IllegalStateException("Cannot happen because there is no EventE.");
	}

	void onEventX(EventContext eventContext) {
		pageMessage = "EventX bubbled up to the page. Context = '" + eventContext.get(String.class, 0) + "'.";
	}
}
