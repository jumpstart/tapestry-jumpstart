package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

@Import(stylesheet = "css/examples/plain.css")
public class MultipleSubmits1 {

	// Screen fields

	@Property
	private String name;

	// Other pages

	@InjectPage
	private MultipleSubmits2 page2;

	// Generally useful bits and pieces

	@Component
	private Form search;

	public enum SearchType {
		CUSTOMERS, SUPPLIERS;
	}

	private SearchType searchType;

	// The code

	void onActivate() {
		searchType = SearchType.CUSTOMERS;
	}

	void onSelectedFromSuppliers() {
		searchType = SearchType.SUPPLIERS;
	}

	Object onCanceled() {
		page2.set(null, name);
		return page2;
	}

	void onValidateFromSearch() {
		if (searchType == SearchType.CUSTOMERS) {
			// Validate the customer search here.
		}
		else if (searchType == SearchType.SUPPLIERS) {
			// Validate the supplier search here.
		}
		else {
			throw new IllegalStateException(searchType.name());
		}
	}

	Object onSuccess() {
		page2.set(searchType, name);
		return page2;
	}

}
