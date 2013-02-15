package jumpstart.web.pages.examples.ajax;

import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.web.commons.EvenOdd;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

public class AjaxEventLinksInALoop {
	static private final int MAX_RESULTS = 30;

	// Screen fields

	@Property
	private List<Person> persons;

	@Property
	private Person person;

	@Property
	private boolean started;

	@Property
	private EvenOdd evenOdd;

	@Property
	@Persist
	private boolean highlightZoneUpdates;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@InjectComponent
	private Zone rowZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	// The code

	void setupRender() {
		// Get all persons - ask business service to find them (from the database)
		persons = personFinderService.findPersons(MAX_RESULTS);
		started = false;
		evenOdd = new EvenOdd();
	}

	void onStart(Long personId) {
		person = personFinderService.findPerson(personId);

		// In a real app we would probably update the person in some way, but we'll just switch a boolean...

		started = true;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(rowZone);
		}
	}

	void onStop(Long personId) {
		person = personFinderService.findPerson(personId);

		// In a real app we would probably update the person in some way, but we'll just switch a boolean...

		started = false;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(rowZone);
		}
	}

	public String getCurrentRowZoneId() {
		// The id attribute of a row must be the same every time that row asks for it and unique on the page.
		return "rowZone_" + person.getId();
	}

	public String getZoneUpdateFunction() {
		return highlightZoneUpdates ? "highlight" : "show";
	}
}
