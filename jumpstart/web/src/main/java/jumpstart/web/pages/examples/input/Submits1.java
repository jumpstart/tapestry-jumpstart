package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

@Import(stylesheet = "css/examples/plain.css")
public class Submits1 {

	// Screen fields

	@Property
	private String name;

	// Other pages

	@InjectPage
	private Submits2 page2;

	// Generally useful bits and pieces

	@Component
	private Form search;

	public enum SearchType {
		CUSTOMERS, SUPPLIERS, CANCEL;
	}

	private SearchType searchType;

	// The code

	void onActivate() {
		searchType = SearchType.CUSTOMERS;
	}

	void onSelectedFromSuppliers() {
		searchType = SearchType.SUPPLIERS;
	}

	void onSelectedFromCancel() {
		searchType = SearchType.CANCEL;
	}
	
	void onValidateFromSearch() {
		if (searchType == SearchType.CANCEL) {
			search.clearErrors();
		}
	}
	
	Object onSuccess() {
		page2.set(searchType, name);
		return page2;
	}
}
