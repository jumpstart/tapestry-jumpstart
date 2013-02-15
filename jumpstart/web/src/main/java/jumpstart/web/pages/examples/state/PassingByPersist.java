package jumpstart.web.pages.examples.state;

import jumpstart.web.pages.Index;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * This page demonstrates using page persistence to remember data through the redirect. The default strategy for Persist is 
 * to save the data in the session, but this can be overridden, eg. we override it to the FLASH strategy.
 */
public class PassingByPersist {

	// Work fields

	@Persist(PersistenceConstants.FLASH)
	private String firstName;

	@Persist(PersistenceConstants.FLASH)
	private String lastName;

	// Generally useful bits and pieces

	@Inject
	private ComponentResources componentResources;

	// The code

	// set() is public so that other pages can use it to set up this page.

	public void set(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getName() {
		return firstName + " " + lastName;
	}

	Object onReturn() {
		componentResources.discardPersistentFieldChanges();
		return PassingDataBetweenPages.class;
	}

	Object onGoHome() {
		componentResources.discardPersistentFieldChanges();
		return Index.class;
	}
}
