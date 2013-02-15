package jumpstart.web.pages.examples.infrastructure;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

public class ExceptionReportPage {

	// Screen fields

	@Inject
	@Symbol(SymbolConstants.PRODUCTION_MODE)
	@Property
	private boolean productionMode;

	// The code

	void onThrowException() {
		throw new RuntimeException(
				"JumpStart threw this exception deliberately to make Tapestry display the current exception report page."
						+ " It's the page that displays exceptions you didn't catch.");
	}
}
