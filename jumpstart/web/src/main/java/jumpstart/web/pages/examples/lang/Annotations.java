package jumpstart.web.pages.examples.lang;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

@Import(stylesheet = "css/examples/olive.css")
public class Annotations {

	@Property
	private String message;
	
	@Inject
	private Logger logger;

	@OnEvent(value = EventConstants.ACTIVATE)
	void thisPageHasBeenRequested() {
		message = "thisPageHasBeenRequested() called... ";
		logger.info("thisPageHasBeenRequested() called... ");
	}

	@SetupRender
	void beforeWeDoAnyRendering() {
		message += "beforeWeDoAnyRendering() called... ";
		logger.info("beforeWeDoAnyRendering() called...");
	}

	@CleanupRender
	void tidyUp() {
		message += "tidyUp() called... ";
		logger.info("tidyUp() called...");
	}

}
