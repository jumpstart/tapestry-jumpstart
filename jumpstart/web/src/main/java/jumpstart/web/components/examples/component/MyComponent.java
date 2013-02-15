package jumpstart.web.components.examples.component;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * MyComponent contains an EventLink and a message that is set up by whichever event handler method is triggered.
 */
public class MyComponent {

	// Parameters
	
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String eventName; 
	
	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String message;

	// Generally useful bits and pieces

	@Inject
	private ComponentResources componentResources;

	// The code

	boolean onEventA() {
		message = "MyComponent handles EventA then bubbles it up.";
		
		// Let EventA bubble up.
		return false;
	}

	boolean onEventB() {
		message = "MyComponent handles EventB then aborts its handling.";
		
		// Abort handling of EventB.
		return true;
	}

	boolean onEventC() {
		message = "MyComponent handles EventC and triggers EventX in its place.";

		// Trigger the event "eventX" on myself which will then bubble up.
		componentResources.triggerEvent("eventX", null, null);

		// Abort handling of EventC.
		return true;
	}
	
	// We deliberately don't handle EventD, so it will bubble straight up.

}
