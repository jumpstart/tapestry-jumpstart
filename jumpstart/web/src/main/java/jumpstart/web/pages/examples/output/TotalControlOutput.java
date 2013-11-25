package jumpstart.web.pages.examples.output;

import java.text.Format;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

@Import(stylesheet = "css/examples/totalcontroloutput.css")
public class TotalControlOutput {

	@Property
	private Person person;

	@Property
	private String regionName;

	@Property
	private Format startDateFormat;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@Inject
	private Messages messages;

	void setupRender() {

		// Get person with id 1 - ask business service to find it (from the database)
		person = personFinderService.findPerson(1L);

		if (person == null) {
			throw new IllegalStateException("Database data has not been set up!");
		}

		regionName = messages.get(Regions.class.getSimpleName() + "." + person.getRegion().name());

		startDateFormat = new SimpleDateFormat("dd MMMM yyyy G");
	}

}
