package jumpstart.web.pages.examples.input;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;

import org.apache.tapestry5.annotations.Property;

public class Create2 {

	// The activation context

	private Long personId;

	// Screen fields

	@Property
	private Person person;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	// The code

	// set() is public so that other pages can use it to set up this page.

	public void set(Long personId) {
		this.personId = personId;
	}

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	Long onPassivate() {
		return personId;
	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	boolean onActivate(Long personId) throws Exception {
		this.personId = personId;
		// Abort event bubbling (see Event Bubbling example) by returning true.
		return true;
	}

	// This one's for first time in.

	void onActivate() {
	}

	// setupRender() is called by tapestry at the start of rendering - it's good for things that are display only.

	void setupRender() throws Exception {
		if (personId != null) {
			person = personFinderService.findPerson(personId);

			if (person == null) {
				if (personId < 4) {
					throw new IllegalStateException("Database data has not been set up!");
				}
				else {
					throw new Exception("Person " + personId + " does not exist.");
				}
			}
		}
	}
}
