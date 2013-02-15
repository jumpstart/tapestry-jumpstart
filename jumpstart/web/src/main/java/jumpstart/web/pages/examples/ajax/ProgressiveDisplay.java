package jumpstart.web.pages.examples.ajax;

import org.apache.tapestry5.annotations.Property;


public class ProgressiveDisplay {
	static final private String[] ALL_THINGS = { "Sugar", "Spice", "All Things Nice" } ;

	// Screen fields
	
	@Property
	private String[] things;
	
	@Property
	private String thing;

	// The code

	void onProgressiveDisplay() {
		
		// Set up the list of things to display
		things = ALL_THINGS;

		// Sleep 4 seconds to simulate a long-running operation
		sleep(4000);
		
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			// Ignore
		}
	}

}
