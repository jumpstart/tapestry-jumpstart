package jumpstart.web.pages.examples.tables;

import java.text.DateFormat;
import java.text.Format;
import java.util.List;
import java.util.Locale;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

@Import(stylesheet = "css/examples/plain.css")
public class EditableLoop2 {

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private List<Person> persons;

	@Property
	private Person person;

	// Generally useful bits and pieces

	@Inject
	private Messages messages;

	@Inject
	private Locale currentLocale;

	// The code

	public void set(List<Person> persons) {
		this.persons = persons;
	}

	public String getPersonRegion() {
		return messages.get(Regions.class.getSimpleName() + "." + person.getRegion().name());
	}

	public Format getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.MEDIUM, currentLocale);
	}
}
