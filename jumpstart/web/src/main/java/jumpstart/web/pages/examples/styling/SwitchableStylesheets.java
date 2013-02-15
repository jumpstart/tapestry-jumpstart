package jumpstart.web.pages.examples.styling;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

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
	@Path("context:css/examples/examples.css")
	private Asset stylesheet0;

	@Inject
	@Path("context:css/examples/switched.css")
	private Asset stylesheet1;
	
	// The code
	
	int onPassivate() {
		return styleNum;
	}
	
	void onActivate(int styleNum) {
		this.styleNum = styleNum;
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
