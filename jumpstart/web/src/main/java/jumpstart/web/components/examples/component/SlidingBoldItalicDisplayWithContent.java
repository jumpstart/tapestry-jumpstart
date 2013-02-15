package jumpstart.web.components.examples.component;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.chenillekit.tapestry.core.components.SlidingPanel;

public class SlidingBoldItalicDisplayWithContent {

	// Parameters

	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String panelTitle;

	@Parameter
	@Property
	private String value;

	// Generally useful bits and pieces

	@Component(parameters={"subject=prop:panelTitle", "closed=true"}, publishParameters = "options, closed")
	private SlidingPanel slidingPanel;

}
