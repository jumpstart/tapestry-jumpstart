package jumpstart.web.pages.examples.state;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;

import org.apache.tapestry5.annotations.Property;

public class AtEjb {

	// Screen fields

	@Property
	private Person person;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	// The code
	
	void setupRender() throws Exception {
		person = personFinderService.findPerson(1L);

		if (person == null) {
			throw new IllegalStateException("Database data has not been set up!");
		}
	}

}
