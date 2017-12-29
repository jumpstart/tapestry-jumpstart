package jumpstart.web.pages.examples.input;

import jumpstart.web.pages.examples.input.MultipleSubmits1.SearchType;

import org.apache.tapestry5.annotations.Import;

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
		if (searchType == null) {
			return "You did not choose a search type.";
		}
		else {
			switch (searchType) {
			case CUSTOMERS:
				return "You chose to search Customers with \"" + name + "\".";
			case SUPPLIERS:
				return "You chose to search Suppliers with \"" + name + "\".";
			default:
				throw new IllegalStateException();
			}
		}
	}
}
