package jumpstart.business.domain.person.iface;

import java.util.List;

import jumpstart.business.commons.IdVersion;
import jumpstart.business.domain.person.Person;

/**
 * The <code>IPersonManagerServiceRemote</code> bean exposes the business methods in the interface.
 */
public interface IPersonManagerServiceRemote {

	// Person

	Person createPerson(Person person);

	void createPersons(List<Person> persons);

	void changePerson(Person person);

	void changePersons(List<Person> persons);

	void changePersonsByDTOs(List<PersonDTO> persons);

	void bulkEditPersons(List<Person> personsToCreate, List<Person> personsToChange, List<IdVersion> personsToDelete);

	void bulkEditPersonsByDTOs(List<PersonDTO> personsToCreate, List<PersonDTO> personsToChange,
			List<IdVersion> personsToDelete);

	void deletePerson(Long id, Integer version);

}
