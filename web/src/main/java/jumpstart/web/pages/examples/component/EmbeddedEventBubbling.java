package jumpstart.web.pages.examples.component;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class EmbeddedEventBubbling {
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private String message;
	
	void onBubbleUp() {
		message = "The page handled the event with onBubbleUp()";
	}

	// This method will not be called.
	void onNoBubbleUp() {
		message = "The page handled the event with onNoBubbleUp().";
	}
}