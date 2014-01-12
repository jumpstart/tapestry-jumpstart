package jumpstart.web.pages.examples.navigation;

import jumpstart.web.models.Mode;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet = "css/examples/plain.css")
public class VariableParameters2 {

	// The activation context

	@Property
	private Long personId;

	@Property
	private Mode mode;
	
	@Property
	private String message;

	// The code

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(EventContext eventContext) {
		
		int parameterCount = eventContext.getCount();
		
		if (parameterCount == 1) {
			mode = eventContext.get(Mode.class, 0);
		}
		else if (parameterCount == 2) {
			mode = eventContext.get(Mode.class, 0);
			personId = eventContext.get(Long.class, 1);
		}
		else {
			message = "Wrong number of parameters received. Expected 1 or 2, found " + parameterCount + ".";
		}
		
	}

}
