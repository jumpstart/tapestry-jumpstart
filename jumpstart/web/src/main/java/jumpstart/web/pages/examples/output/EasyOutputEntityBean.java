package jumpstart.web.pages.examples.output;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet="css/examples/plain.css")
public class EasyOutputEntityBean {

	@Property
	private Person person;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	void setupRender() {
		
		// Get person with id 1 - ask business service to find it (from the database).
		
		person = personFinderService.findPerson(1L);

		if (person == null) {
			throw new IllegalStateException("Database data has not been set up!");
		}
	}

}
