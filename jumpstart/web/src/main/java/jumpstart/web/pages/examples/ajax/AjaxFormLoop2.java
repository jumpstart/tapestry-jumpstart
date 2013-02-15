package jumpstart.web.pages.examples.ajax;

import java.text.DateFormat;
import java.text.Format;
import java.util.Collection;
import java.util.Locale;

import jumpstart.business.commons.IdVersion;
import jumpstart.business.domain.person.Regions;
import jumpstart.business.domain.person.iface.PersonDTO;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class AjaxFormLoop2 {

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Collection<PersonDTO> persons;

	@Property
	private PersonDTO person;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Collection<IdVersion> personsDeleted;

	// The (id, version) of a person deleted
	@Property
	private IdVersion personDeleted;

	// Generally useful bits and pieces

	@Inject
	private Messages messages;

	@Inject
	private Locale currentLocale;

	// The code

	public void set(Collection<PersonDTO> personsToCreate, Collection<PersonDTO> personsToChange,
			Collection<IdVersion> personsDeleted) {
		this.persons = personsToChange;
		this.persons.addAll(personsToCreate);
		this.personsDeleted = personsDeleted;
	}

	public String getPersonRegion() {
		// Follow the same naming convention that the Select component uses
		return messages.get(Regions.class.getSimpleName() + "." + person.getRegion().name());
	}

	public Format getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.MEDIUM, currentLocale);
	}
}

