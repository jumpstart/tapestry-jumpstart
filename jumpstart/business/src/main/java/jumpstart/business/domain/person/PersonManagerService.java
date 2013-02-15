package jumpstart.business.domain.person;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;

import jumpstart.business.commons.IdVersion;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.business.domain.person.iface.IPersonManagerServiceRemote;
import jumpstart.business.domain.person.iface.PersonDTO;

@Stateless
@Local(IPersonManagerServiceLocal.class)
@Remote(IPersonManagerServiceRemote.class)
public class PersonManagerService implements IPersonManagerServiceLocal, IPersonManagerServiceRemote {

	@PersistenceContext(unitName = "jumpstart")
	public EntityManager em;

	public Person createPerson(Person person) {
		em.persist(person);
		return person;
	}

	public void createPersons(List<Person> persons) {
		for (Person person : persons) {
			em.persist(person);
		}
	}

	public void changePerson(Person person) {
		Person p = em.merge(person);
		// Flush to work around OPENEJB issue https://issues.apache.org/jira/browse/OPENEJB-782
		em.flush();

		// If id is different it means the person did not exist so merge has created a new one.
		if (!p.getId().equals(person.getId())) {
			throw new EntityNotFoundException("Person no longer exists.");
		}
	}

	public void changePersons(List<Person> persons) {
		for (Person person : persons) {
			Person p = em.merge(person);

			// If id is different it means the person did not exist so merge has created a new one.
			if (!p.getId().equals(person.getId())) {
				throw new EntityNotFoundException("Person no longer exists.");
			}
		}
	}

	public void changePersonsByDTOs(List<PersonDTO> persons) {
		for (PersonDTO person : persons) {
			Person p = em.find(Person.class, person.getId());

			if (p == null) {
				throw new EntityNotFoundException("Person no longer exists.");
			}

			if (!p.getVersion().equals(person.getVersion())) {
				throw new OptimisticLockException();
			}

			p.setFirstName(person.getFirstName());
		}
	}

	public void bulkEditPersons(List<Person> personsToCreate, List<Person> personsToChange,
			List<IdVersion> personsToDelete) {
		for (Person person : personsToCreate) {
			em.persist(person);
		}
		for (Person person : personsToChange) {
			Person p = em.merge(person);

			// If id is different it means the person did not exist so merge has created a new one.
			if (!p.getId().equals(person.getId())) {
				throw new EntityNotFoundException("Person no longer exists.");
			}
		}
		for (IdVersion idVersion : personsToDelete) {
			Person p = em.find(Person.class, idVersion.getId());

			if (p == null) {
				throw new EntityNotFoundException("Person no longer exists.");
			}

			if (!p.getVersion().equals(idVersion.getVersion())) {
				throw new OptimisticLockException();
			}

			em.remove(p);
		}
	}

	public void bulkEditPersonsByDTOs(List<PersonDTO> personsToCreate, List<PersonDTO> personsToChange,
			List<IdVersion> personsToDelete) {
		for (PersonDTO p : personsToCreate) {
			Person person = new Person(p.getFirstName(), p.getLastName(), p.getRegion(), p.getStartDate());
			em.persist(person);
		}
		for (PersonDTO person : personsToChange) {
			Person p = em.find(Person.class, person.getId());

			if (p == null) {
				throw new EntityNotFoundException("Person no longer exists.");
			}

			if (!p.getVersion().equals(person.getVersion())) {
				throw new OptimisticLockException();
			}

			p.setFirstName(person.getFirstName());
			p.setLastName(person.getLastName());
			p.setRegion(person.getRegion());
			p.setStartDate(person.getStartDate());
		}
		for (IdVersion idVersion : personsToDelete) {
			Person p = em.find(Person.class, idVersion.getId());

			if (p == null) {
				throw new EntityNotFoundException("Person no longer exists.");
			}

			if (!p.getVersion().equals(idVersion.getVersion())) {
				throw new OptimisticLockException();
			}

			em.remove(p);
		}
	}

	public void deletePerson(Long id, Integer version) {
		Person p = em.find(Person.class, id);

		if (p == null) {
			throw new EntityNotFoundException("Person no longer exists.");
		}

		if (!p.getVersion().equals(version)) {
			throw new OptimisticLockException();
		}

		em.remove(p);
		// Flush to work around OPENEJB issue https://issues.apache.org/jira/browse/OPENEJB-782
		em.flush();
	}

}
