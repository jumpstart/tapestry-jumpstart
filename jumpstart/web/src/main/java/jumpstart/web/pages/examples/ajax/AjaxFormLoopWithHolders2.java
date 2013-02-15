package jumpstart.web.pages.examples.ajax;

import java.text.DateFormat;
import java.text.Format;
import java.util.Collection;
import java.util.Locale;

import jumpstart.business.commons.IdVersion;
import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class AjaxFormLoopWithHolders2 {

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Collection<Person> persons;

	@Property
	private Person person;

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

	public void set(Collection<Person> personsToCreate, Collection<Person> personsToChange,
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

