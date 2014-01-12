package jumpstart.web.pages.examples.navigation;

import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet = "css/examples/plain.css")
public class ActivationRequestParameters2 {

	// Activation request parameters

	@ActivationRequestParameter
	@Property
	private String partialName;

}
