package jumpstart.web.pages;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.ExceptionReporter;

/**
 * A user-friendly page to display when an uncaught exception occurs. If the system property productionMode=true,
 * it displays a simple non-technical message. If the system property productionMode=false, it displays the 
 * same comprehensive exception and session information as Tapestry's default ExceptionReport page. 
 * For more information see http://wiki.apache.org/tapestry/Tapestry5ExceptionPage.
 */

public class ExceptionReport_parked implements ExceptionReporter {
	
	// Screen fields

	@Property
	private Throwable exception;

	@Property
	@Inject
	@Symbol(SymbolConstants.PRODUCTION_MODE)
	private boolean productionMode;
	
	// The code

	public void reportException(Throwable exception) {
		this.exception = exception;
	}

}