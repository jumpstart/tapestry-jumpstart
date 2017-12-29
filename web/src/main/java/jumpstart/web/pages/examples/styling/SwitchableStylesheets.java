package jumpstart.web.pages.examples.styling;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class SwitchableStylesheets {

	// The activation context

	private int styleNum;

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String firstName;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String lastName;

	// Generally useful bits and pieces

	@Inject
	@Path("css/examples/plain.css")
	private Asset stylesheet0;

	@Inject
	@Path("css/examples/switched.css")
	private Asset stylesheet1;

	@Inject
	private JavaScriptSupport javaScriptSupport;

	// The code

	void onActivate(int styleNum) {
		this.styleNum = styleNum;
	}

	int onPassivate() {
		return styleNum;
	}
	
	void setupRender() {
		javaScriptSupport.importStylesheet(getStylesheet());
	}

	void onSuccess() {
		styleNum = (styleNum + 1) % 2;
	}

	public Asset getStylesheet() {
		switch (styleNum) {
		case 0:
			return stylesheet0;
		case 1:
			return stylesheet1;
		default:
			return stylesheet0;
		}
	}
}
