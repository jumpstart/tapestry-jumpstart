package jumpstart.web.pages.together.totalcontrolcrud;

import java.text.DateFormat;
import java.text.Format;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.commons.EvenOdd;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Persons {

	private final String demoModeStr = System.getProperty("jumpstart.demo-mode");
	static private final int MAX_RESULTS = 30;

	// Screen fields

	@Property
	private List<Person> persons;

	@Property
	private Person person;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String errorMessage;

	@Property
	private EvenOdd evenOdd;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@EJB
	private IPersonManagerServiceLocal personManagerService;

	@Inject
	private Messages messages;

	@Inject
	private Locale currentLocale;

	// The code

	// setupRender() is called by Tapestry right before it starts rendering the page.

	void setupRender() {
		persons = personFinderService.findPersons(MAX_RESULTS);
		evenOdd = new EvenOdd();
	}

	// Handle event "delete"

	void onDelete(Long id, Integer version) {
		if (demoModeStr != null && demoModeStr.equals("true")) {
			errorMessage = "Sorry, but this function is not allowed in Demo mode.";
			return;
		}

		try {
			personManagerService.deletePerson(id, version);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			errorMessage = ExceptionUtil.getRootCauseMessage(e);
		}
	}

	public String getPersonRegion() {
		// Follow the same naming convention that the Select component uses
		return messages.get(Regions.class.getSimpleName() + "." + person.getRegion().name());
	}

	public Format getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.MEDIUM, currentLocale);
	}
}
