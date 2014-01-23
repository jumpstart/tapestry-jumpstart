package jumpstart.web.pages.together.easycrud.person;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet = "css/examples/plain.css")
public class PersonReview {

	// The activation context

	@Property
	private Long personId;

	// Screen fields

	@Property
	private Person person;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	// The code

	void onActivate(Long personId) {
		this.personId = personId;
	}

	Long onPassivate() {
		return personId;
	}

	void setupRender() {
		person = personFinderService.findPerson(personId);
		// Handle null person in the template.
	}

}
