package jumpstart.web.components.examples.component;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class BoldItalicDisplay {
	
	// Parameters

	@Parameter(required = true)
	@Property
	private String value;

}
