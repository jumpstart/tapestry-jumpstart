package jumpstart.web.pages.examples.tables;

import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.web.commons.EvenOdd;

import org.apache.tapestry5.annotations.Property;

public class AlternatingLoop {
	static private final int MAX_RESULTS = 30;

	// Screen fields

	@Property
	private List<Person> persons;
	
	@Property
	private Person person;
	
	@Property
	private EvenOdd evenOdd;
	
	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	// The code
	
	void setupRender() {
		// Get all persons - ask business service to find them (from the database)
		persons = personFinderService.findPersons(MAX_RESULTS);
		evenOdd = new EvenOdd();
	}
}
