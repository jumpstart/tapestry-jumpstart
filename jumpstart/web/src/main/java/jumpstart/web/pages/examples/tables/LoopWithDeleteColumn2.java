package jumpstart.web.pages.examples.tables;

import java.util.Collection;

import jumpstart.business.commons.IdVersion;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class LoopWithDeleteColumn2 {

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Collection<IdVersion> persons;

	@Property
	private IdVersion person;

	// The code

	public void set(Collection<IdVersion> personsDeleted) {
		this.persons = personsDeleted;
	}
	
}
