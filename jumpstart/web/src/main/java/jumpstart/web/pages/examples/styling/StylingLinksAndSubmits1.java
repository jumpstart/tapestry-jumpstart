package jumpstart.web.pages.examples.styling;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

@Import(stylesheet="css/examples/links_and_submits.css")
public class StylingLinksAndSubmits1 {

	// Screen fields

	@Property
	private String firstName;
	
	@Inject
	@Path("images/next-button-48.png")
	@Property
	private Asset nextImageVar;

	// Other pages
	
	@InjectPage
	private StylingLinksAndSubmits2 page2;

	// The code
	
	Object onNextPage(String context) {
		page2.set(context);
		return page2;
	}
	
	Object onSuccess() {
		page2.set(firstName);
		return page2;
	}

}
