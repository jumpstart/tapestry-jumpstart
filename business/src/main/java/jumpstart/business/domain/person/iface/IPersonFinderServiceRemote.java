package jumpstart.business.domain.person.iface;

import java.util.List;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.util.query.SortCriterion;

/**
 * The <code>IPersonFinderServiceRemote</code> bean exposes the business methods in the interface.
 */
public interface IPersonFinderServiceRemote {

	// Person

	Person findPerson(Long id);

	long countPersons();
	
	List<Person> findPersons(int maxResults);

	List<Person> findPersons(String partialName, int maxResults);

	List<Person> findPersonsByFirstName(String firstName);

	List<Person> findPersonsByLastName(String lastName);

	long countPersons(String partialName);
	
	List<Person> findPersons(String partialName, int startIndex, int maxResults);

	List<Person> findPersons(int startIndex, int maxResults, List<SortCriterion> sortCriteria);

	long countPersons(String firstNameStartsWith, String lastNameStartsWith, Regions region);
	
	List<Person> findPersons(String firstNameStartsWith, String lastNameStartsWith, Regions region, int startIndex,
			int maxResults, List<SortCriterion> sortCriteria);
	
}
