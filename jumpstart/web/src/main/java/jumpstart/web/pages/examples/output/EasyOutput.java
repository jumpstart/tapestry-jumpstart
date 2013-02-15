package jumpstart.web.pages.examples.output;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;

import org.apache.tapestry5.annotations.Property;

public class EasyOutput {

	@Property
	private Person person;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	void setupRender() {
		Long personId = 1L;
		// Get person - ask business service to find it (from the database)
		person = personFinderService.findPerson(personId);

		if (person == null) {
			throw new IllegalStateException("Database data has not been set up!");
		}
	}

}
