package jumpstart.web.pages.examples.ajax;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.web.model.examples.ajax.PersonFilteredDataSource;

import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

public class AjaxFilteredGrid {
	static private final int MAX_RESULTS = 30;

	// Screen fields

	@Property
	private List<String> firstInitials;

	@Property
	@ActivationRequestParameter("first")
	private String firstInitial;

	@Property
	private List<String> lastInitials;

	@Property
	@ActivationRequestParameter("last")
	private String lastInitial;

	@Property
	private List<Regions> regions;

	@Property
	@ActivationRequestParameter("region")
	private Regions region;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@InjectComponent
	private Zone personsZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	// The code

	void setupRender() {

		// Build 2 of the filters from the names found in a list of all persons.

		TreeSet<String> firstInitialsSet = new TreeSet<String>();
		TreeSet<String> lastInitialsSet = new TreeSet<String>();

		List<Person> allPersons = personFinderService.findPersons(MAX_RESULTS);

		for (Person person : allPersons) {
			if (person.getFirstName().length() >= 1) {
				firstInitialsSet.add(person.getFirstName().substring(0, 1).toUpperCase());
			}
			if (person.getLastName().length() >= 1) {
				lastInitialsSet.add(person.getLastName().substring(0, 1).toUpperCase());
			}
		}

		firstInitials = new ArrayList<String>(firstInitialsSet);
		lastInitials = new ArrayList<String>(lastInitialsSet);
	}

	void onValidateFromFilterCriteria() {
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(personsZone);
		}
	}

	public GridDataSource getPersons() {
		return new PersonFilteredDataSource(personFinderService, firstInitial, lastInitial, region);
	}

}
