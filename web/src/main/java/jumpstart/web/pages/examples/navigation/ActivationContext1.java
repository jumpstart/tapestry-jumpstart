package jumpstart.web.pages.examples.navigation;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet = "css/examples/olive.css")
public class ActivationContext1 {

	@Property
	private Long personId;

	void setupRender() {
		personId = new Long(1);
	}

}
