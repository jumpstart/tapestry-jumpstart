package jumpstart.web.pages.examples.navigation;

import javax.validation.constraints.NotNull;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

@Import(stylesheet = "css/examples/returntopage1.css")
public class ReturnToPage1 {

	// The activation context

	@Property
	@NotNull
	private String arg1;

	// Other pages

	@InjectPage
	private ReturnToPage2 page2;

	// Generally useful bits and pieces

	@Inject
	private PageRenderLinkSource pageRenderLinkSource;

	// The code

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(String arg1) throws Exception {
		this.arg1 = arg1;
	}

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	String onPassivate() {
		return arg1;
	}

	Object onSuccess() {
		Link thisPage = pageRenderLinkSource.createPageRenderLinkWithContext(this.getClass(), onPassivate());
		page2.set("Hello", thisPage);
		return page2;
	}

}
