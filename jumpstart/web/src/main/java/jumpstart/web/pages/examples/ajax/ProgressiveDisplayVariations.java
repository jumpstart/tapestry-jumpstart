package jumpstart.web.pages.examples.ajax;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ProgressiveDisplayVariations {

	// Screen fields
	
	@Property
	private int sleepTimeMillis;

	// Generally useful bits and pieces

	@Inject
	private Block resultSixBlock;

	// The code

	void onProgressiveDisplayFromShowOne() {
		// Sleep 1 seconds to simulate a long-running operation
		sleep(1000);
	}

	void onProgressiveDisplayFromShowTwo() {
		// Sleep 2 seconds to simulate a long-running operation
		sleep(2000);
	}

	void onProgressiveDisplayFromShowThree(int sleepTimeMillis) {
		this.sleepTimeMillis = sleepTimeMillis;
		// Sleep 3 seconds to simulate a long-running operation
		sleep(sleepTimeMillis);
	}

	void onProgressiveDisplayFromShowFour() {
		// Sleep 4 seconds to simulate a long-running operation
		sleep(4000);
	}

	void onProgressiveDisplayFromShowFive() {
		// Sleep 5 seconds to simulate a long-running operation
		sleep(5000);
	}

	Block onProgressiveDisplayFromShowSix() {
		// Sleep 6 seconds to simulate a long-running operation
		sleep(6000);
		return resultSixBlock;
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
