package jumpstart.web.pages.examples.select;

import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.web.services.SelectIdModelFactory;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

@Import(stylesheet = "css/examples/select.css")
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

	void onActivate(EventContext context) {
		if (context.getCount() > 0) {
			personId = context.get(Long.class, 0);
		}
	}

	Long onPassivate() {	
		return personId;
	}

	void onPrepare() {
		// Get all persons - ask business service to find them (from the database)
		List<Person> persons = personFinderService.findPersons(MAX_RESULTS);

		personIdsModel = selectIdModelFactory.create(persons, "firstName", "id");
	}

}
