package jumpstart.web.encoders.examples;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;

import org.apache.tapestry5.ValueEncoder;

public class PersonEncoder implements ValueEncoder<Person> {

	private IPersonFinderServiceLocal personFinderService;

	public PersonEncoder(IPersonFinderServiceLocal personFinderService) {
		this.personFinderService = personFinderService;
	}

	@Override
	public String toClient(Person value) {
		return String.valueOf(value.getId());
	}

	@Override
	public Person toValue(String id) {
		return personFinderService.findPerson(Long.parseLong(id));
	}

}
