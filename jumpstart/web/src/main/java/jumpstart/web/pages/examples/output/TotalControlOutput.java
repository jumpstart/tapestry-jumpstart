package jumpstart.web.pages.examples.output;

import java.text.Format;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class TotalControlOutput {

	@Property
	private Person person;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@Inject
	private Messages messages;

	void setupRender() {
		Long personId = 1L;
		// Get person - ask business service to find it (from the database)
		person = personFinderService.findPerson(personId);

		if (person == null) {
			throw new IllegalStateException("Database data has not been set up!");
		}
	}

	public String getPersonRegion() {
		// Follow the same naming convention that the Select component uses
		return messages.get(Regions.class.getSimpleName() + "." + person.getRegion().name());
	}

	public Format getStartDateFormat() {
		final Format f = new SimpleDateFormat("dd MMMM yyyy G");
		return f;
	}
}
