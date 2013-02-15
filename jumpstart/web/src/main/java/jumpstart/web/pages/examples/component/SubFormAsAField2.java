package jumpstart.web.pages.examples.component;

import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.web.model.examples.Invitation;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class SubFormAsAField2 {
	static private final int MAX_RESULTS = 30;

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Invitation invitation;

	@Property
	private List<Person> allPersons;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	// The code

	public void set(Invitation invitation) {
		this.invitation = invitation;
	}

	void setupRender() {
		allPersons = personFinderService.findPersons(MAX_RESULTS);
	}

}
