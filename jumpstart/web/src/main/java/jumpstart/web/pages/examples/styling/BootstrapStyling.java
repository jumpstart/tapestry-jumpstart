package jumpstart.web.pages.examples.styling;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

import com.trsvax.bootstrap.annotations.Exclude;

// Use @Exclude if you do not want Tapestry CSS.
@Exclude(stylesheet = { "core" })
@Import(stylesheet = { "classpath:/com/trsvax/bootstrap/assets/bootstrap/css/bootstrap.css",
		"classpath:/com/trsvax/bootstrap/assets/bootstrap/css/bootstrap-responsive.css",
		"context:css/examples/bootstrap_styling.css" }, library = { "classpath:/com/trsvax/bootstrap/assets/bootstrap/js/bootstrap.js" })
public class BootstrapStyling {

	// The activation context

	private Long personId;

	// Screen fields

	@Property
	private Person person;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	Long onPassivate() {
		return personId;
	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(Long personId) {
		this.personId = personId;
	}

	// Form bubbles up the PREPARE_FOR_RENDER event during form render.

	void onPrepareForRender() throws Exception {
		person = findPerson(personId);
	}

	// Form bubbles up the PREPARE_FOR_SUBMIT event during form submission.

	void onPrepareForSubmit() throws Exception {
		// Get objects for the form fields to overlay.
		person = findPerson(personId);
	}

	void onRefresh() {
		// By doing nothing the page will be displayed afresh.
	}

	private Person findPerson(Long personId) throws Exception {
		Person person = personFinderService.findPerson(personId);

		if (person == null) {
			if (personId < 4) {
				throw new IllegalStateException("Database data has not been set up!");
			}
			else {
				throw new Exception("Person " + personId + " does not exist.");
			}
		}

		return person;
	}

}
