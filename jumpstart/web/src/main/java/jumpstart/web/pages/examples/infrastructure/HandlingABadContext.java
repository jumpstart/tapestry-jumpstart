package jumpstart.web.pages.examples.infrastructure;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;

import org.apache.tapestry5.annotations.Property;

public class HandlingABadContext {

	// The activation context

	@Property
	private Long personId;

	// Screen fields

	@Property
	private Person person;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	// The code

	Long onPassivate() {
		return personId;
	}

	void onActivate(Long personId) {
		this.personId = personId;
	}

	void setupRender() {
		// Get person - ask business service to find it (from the database)
		person = personFinderService.findPerson(personId);
		// Handle null person in the template (with an If component).
	}
}
