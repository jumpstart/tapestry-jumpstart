package jumpstart.web.pages.examples.input;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jumpstart.web.pages.examples.input.MultipleForms2.SearchType;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet = "css/examples/multipleforms1.css")
public class MultipleForms1 {

	// Screen fields

	@Property
	@NotNull
	@Size(max = 10)
	private String customerName;

	@Property
	@NotNull
	@Size(max = 10)
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
