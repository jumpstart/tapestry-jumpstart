package jumpstart.web.pages.examples.select;

import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.web.services.SelectIdModelFactory;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class EasyIdSelect {
	static private final int MAX_RESULTS = 30;

	// The activation context

	@Property
	private Long personId;

	// Screen fields

	@Property
	private SelectModel personIdsModel;

	// Generally useful bits and pieces

	@Inject
	private SelectIdModelFactory selectIdModelFactory;

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

		personIdsModel = selectIdModelFactory.create(persons, "firstName", "id");
	}

}
