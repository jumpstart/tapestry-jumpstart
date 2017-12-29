package jumpstart.web.pages.examples.lang;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.corelib.components.PageLink;

@Import(stylesheet="css/examples/olive.css")
public class AtComponent {

	// This provides parameter bindings for the "home" component.
	
	@Component(id = "home", parameters = {"page=Index"})
	private PageLink index;
	
}
