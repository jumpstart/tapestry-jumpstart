package jumpstart.web.pages.examples.input;

public class MultipleForms2 {

	// The activation context

	private SearchType searchType;

	private String name;

	// Generally useful bits and pieces

	public enum SearchType {
		CUSTOMERS, SUPPLIERS;
	}

	// The code

	// set() is public so that other pages can use it to set up this page.

	public void set(SearchType searchType, String lastName) {
		this.searchType = searchType;
		this.name = lastName;
	}

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	Object[] onPassivate() {
		return new Object[] { searchType, name };
	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(SearchType searchType, String lastName) {
		this.searchType = searchType;
		this.name = lastName;
	}

	public String getYourSearch() {
		if (searchType == SearchType.CUSTOMERS) {
			return "You chose to search Customers with \"" + name + "\".";
		}
		else {
			return "You chose to search Suppliers with \"" + name + "\".";
		}
	}
}
