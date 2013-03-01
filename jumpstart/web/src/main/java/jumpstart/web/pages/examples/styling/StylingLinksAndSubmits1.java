package jumpstart.web.pages.examples.styling;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class StylingLinksAndSubmits1 {

	// Screen fields

	@Property
	private String firstName;
	
	@Inject
	@Path("context:images/next-button-48.png")
	@Property
	private Asset nextImage;

	// Other pages
	
	@InjectPage
	private StylingLinksAndSubmits2 page2;

	// The code
	
	Object onNextPage(String context) {
		page2.set(context);
		return page2;
	}
	
	Object onActionFromAL1(String context) {
		page2.set(context);
		return page2;
	}

	Object onActionFromAL2(String context) {
		page2.set(context);
		return page2;
	}

	Object onSuccess() {
		page2.set(firstName);
		return page2;
	}

}
