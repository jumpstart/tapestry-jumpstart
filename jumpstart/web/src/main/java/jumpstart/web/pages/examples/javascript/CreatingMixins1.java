package jumpstart.web.pages.examples.javascript;

import jumpstart.web.state.examples.javascript.MyOrder;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

public class CreatingMixins1 {

	// Work fields

	@SessionState
	@Property
	private MyOrder myOrder;

	// The code

	void setupRender() {
		myOrder.setApplesQuantity(0);
		myOrder.setOrangesQuantity(0);
		myOrder.setBananasQuantity(0);
	}

	Object onSuccessFromPlainForm() {
		orderOneApple();
		return CreatingMixins2.class;
	}

	Object onOrderOneOrange() {
		orderOneOrange();
		return CreatingMixins2.class;
	}

	Object onActionFromOrderOneBanana() {
		orderOneBanana();
		return CreatingMixins2.class;
	}

	Object onSuccessFromMixinForm() {
		orderOneApple();
		return CreatingMixins2.class;
	}

	Object onActionFromOrderOneBananaWithMixin() {
		orderOneBanana();
		return CreatingMixins2.class;
	}

	void orderOneApple() {
		sleep(1500); // Sleep 1.5 seconds to simulate busy system
		myOrder.setApplesQuantity(myOrder.getApplesQuantity() + 1);
	}

	void orderOneOrange() {
		sleep(1500); // Sleep 1.5 seconds to simulate busy system
		myOrder.setOrangesQuantity(myOrder.getOrangesQuantity() + 1);
	}

	void orderOneBanana() {
		sleep(1500); // Sleep 1.5 seconds to simulate busy system
		myOrder.setBananasQuantity(myOrder.getBananasQuantity() + 1);
	}

	private void sleep(long duration) {
		try {
			Thread.sleep(duration);
		}
		catch (InterruptedException e) {
		}
	}

}
