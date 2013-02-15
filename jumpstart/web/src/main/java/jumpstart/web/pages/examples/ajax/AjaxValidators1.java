package jumpstart.web.pages.examples.ajax;

import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;

public class AjaxValidators1 {
	static private final int MAX_RESULTS = 30;

	// Screen fields

	@Property
	private String firstName;

	@Property
	private String lastName;

	@Property
	private List<Person> persons;

	// Other pages

	@InjectPage
	private AjaxValidators2 page2;

	// Useful bits and pieces

	@Inject
	private Request request;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	// The code

	void setupRender() {
		persons = personFinderService.findPersons(MAX_RESULTS);
	}

	JSONObject onAjaxValidateFromFirstName() {
		String firstName = request.getParameter("param");

		try {
			validateFirstNameIsUnique(firstName);
		}
		catch (Exception e) {
			return new JSONObject().put("error", e.getMessage());
		}

		return new JSONObject();
	}

	JSONObject onAjaxValidateFromLastName() {
		String lastName = request.getParameter("param");

		try {
			validateLastNameIsUnique(lastName);
		}
		catch (Exception e) {
			return new JSONObject().put("error", e.getMessage());
		}

		return new JSONObject();
	}

	Object onSuccess() {
		page2.set(firstName, lastName);
		return page2;
	}

	void validateFirstNameIsUnique(String firstName) throws Exception {
		if (firstName != null) {
			List<Person> persons = personFinderService.findPersonsByFirstName(firstName);

			if (persons.size() > 0) {
				throw new Exception("The name is not available.");
			}
		}
	}

	void validateLastNameIsUnique(String lastName) throws Exception {
		if (lastName != null) {
			List<Person> persons = personFinderService.findPersonsByLastName(lastName);

			if (persons.size() > 0) {
				throw new Exception("The name is not available.");
			}
		}
	}
}
