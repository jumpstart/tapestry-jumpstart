package jumpstart.web.pages.examples.input;

import jumpstart.web.pages.examples.input.MultipleForms2.SearchType;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;

public class MultipleForms1 {

	// Screen fields

	@Property
	private String customerName;

	@Property
	private String supplierName;

	// Other pages

	@InjectPage
	private MultipleForms2 page2;

	// The code

	void onPrepareFromSearchCustomers() {
		// Any setting up of editable objects or fields on this form should be done in here.
	}

	void onPrepareFromSearchSuppliers() {
		// Any setting up of editable objects or fields on this form should be done in here.
	}

	Object onSuccessFromSearchCustomers() {
		page2.set(SearchType.CUSTOMERS, customerName);
		return page2;
	}

	Object onSuccessFromSearchSuppliers() {
		page2.set(SearchType.SUPPLIERS, supplierName);
		return page2;
	}
}
