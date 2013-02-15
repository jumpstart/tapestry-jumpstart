package jumpstart.web.pages.examples.select;

import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.web.model.examples.select.PersonIdSelectModel;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Property;

public class IdSelect {
	static private final int MAX_RESULTS = 30;

	// The activation context

	@Property
	private Long personId;

	// Screen fields

	@Property
	private SelectModel personIdsModel;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	// The code

	Long onPassivate() {
		return personId;
	}

	void onActivate(EventContext context) {
		if (context.getCount() > 0) {
			personId = context.get(Long.class, 0);
		}
	}

	void onPrepareForRender() {
		// Get all persons - ask business service to find them (from the database)
		List<Person> persons = personFinderService.findPersons(MAX_RESULTS);

		personIdsModel = new PersonIdSelectModel(persons);
	}

}
