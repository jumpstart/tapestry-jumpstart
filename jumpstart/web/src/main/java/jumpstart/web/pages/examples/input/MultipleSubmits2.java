package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.annotations.Import;

import jumpstart.web.pages.examples.input.MultipleSubmits1.SearchType;

@Import(stylesheet = "css/examples/plain.css")
public class MultipleSubmits2 {

	// The activation context

	private SearchType searchType;

	private String name;

	// The code

	// set() is public so that other pages can use it to set up this page.

	public void set(SearchType searchType, String lastName) {
		this.searchType = searchType;
		this.name = lastName;
	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(SearchType searchType, String name) {
		this.searchType = searchType;
		this.name = name;
	}

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	Object[] onPassivate() {
		return new Object[] { searchType, name };
	}

	public String getYourSearch() {
		switch (searchType) {
		case CUSTOMERS:
			return "You chose to search Customers with \"" + name + "\".";
		case SUPPLIERS:
			return "You chose to search Suppliers with \"" + name + "\".";
		case CANCEL:
			return "You chose to cancel the search.";
		default:
			throw new IllegalStateException();
		}
	}
}
