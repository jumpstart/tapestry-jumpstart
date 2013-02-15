package jumpstart.web.pages.examples.lang;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

public class Methods {

	private String message;
	
	@Inject
	private Logger logger;
	
	void onActivate() {
		message = "onActivate() called... ";
		logger.info("onActivate() called... ");
	}
	
	void setupRender() {
		message += "setupRender() called... ";
		logger.info("setupRender() called...");
	}
	
	public String getMessage() {
		message += "getMessage() called... ";
		logger.info("getMessage() called...");
		return message;
	}

	void cleanupRender() {
		message += "cleanupRender() called... ";
		logger.info("cleanupRender() called...");
	}
	
}
