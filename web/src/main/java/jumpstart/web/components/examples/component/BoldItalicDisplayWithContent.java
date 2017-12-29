package jumpstart.web.components.examples.component;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet = "css/examples/bolditalicdisplay.css")
public class BoldItalicDisplayWithContent {

	// Parameters

	@Parameter(required = true)
	@Property
	private String value;

}
