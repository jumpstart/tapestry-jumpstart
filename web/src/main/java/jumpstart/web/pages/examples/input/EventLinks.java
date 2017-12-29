package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet="css/examples/olive.css")
public class EventLinks {

	// Screen fields
	
	@Property
	private int count;
	
	// The code

	void onActivate(int count) {
		this.count = count;
	}

	int onPassivate() {
		return count;
	}

	void onAdd(int amount) {
		count += amount;
	}
	
	void onClear() {
		count = 0;
	}

}