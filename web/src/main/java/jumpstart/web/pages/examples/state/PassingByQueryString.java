package jumpstart.web.pages.examples.state;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

/**
 * This page demonstrates using a query string to remember data through the redirect. The data will be appended to
 * the page render request URL as a query string.
 */
@Import(stylesheet="css/examples/olive.css")
public class PassingByQueryString {

	// Query string parameters - you are not limited to one.

	@ActivationRequestParameter(value = "partial")
	@Property
	private String partialName;

	// Generally useful bits and pieces

	@Inject
	private PageRenderLinkSource pageRenderLinkSource;

	// The code

	// set() is public so that other pages can use it to get a correct link to this page.

	public Link set(String partialName) {
		this.partialName = partialName;

		return pageRenderLinkSource.createPageRenderLink(this.getClass());
	}
}
