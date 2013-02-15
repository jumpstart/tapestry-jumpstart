package jumpstart.business.domain.person;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.business.domain.person.iface.IPersonFinderServiceRemote;
import jumpstart.util.query.SortCriterion;

@Stateless
@Local(IPersonFinderServiceLocal.class)
@Remote(IPersonFinderServiceRemote.class)
public class PersonFinderService implements IPersonFinderServiceLocal, IPersonFinderServiceRemote {

	@PersistenceContext(unitName = "jumpstart")
	private EntityManager em;

	public Person findPerson(Long id) {
		return em.find(Person.class, id);
	}

	public long countPersons() {
		return (Long) em.createQuery("select count(p) from Person p").getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Person> findPersons(int maxResults) {
		return em.createQuery("select p from Person p order by lower(p.firstName), lower(p.lastName)")
				.setMaxResults(maxResults).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Person> findPersons(String partialName, int maxResults) {
		String searchName = partialName == null ? "" : partialName.toLowerCase();

		StringBuilder buf = new StringBuilder();
		buf.append("select p from Person p");
		buf.append(" where lower(firstName) like :firstName");
		buf.append(" or lower(lastName) like :lastName");
		buf.append(" order by lower(p.firstName), lower(p.lastName)");

		Query q = em.createQuery(buf.toString());
		q.setParameter("firstName", "%" + searchName + "%");
		q.setParameter("lastName", "%" + searchName + "%");

		List<Person> l = q.setMaxResults(maxResults).getResultList();
		return l;
	}

	@SuppressWarnings("unchecked")
	public List<Person> findPersonsByFirstName(String firstName) {
		String searchName = firstName == null ? "" : firstName.trim().toLowerCase();

		StringBuilder buf = new StringBuilder();
		buf.append("select p from Person p");
		buf.append(" where lower(p.firstName) = :searchName");
		buf.append(" order by lower(p.firstName), lower(p.lastName)");

		Query q = em.createQuery(buf.toString());
		q.setParameter("searchName", searchName);

		List<Person> l = q.getResultList();
		return l;
	}

	@SuppressWarnings("unchecked")
	public List<Person> findPersonsByLastName(String lastName) {
		String searchName = lastName == null ? "" : lastName.trim().toLowerCase();

		StringBuilder buf = new StringBuilder();
		buf.append("select p from Person p");
		buf.append(" where lower(p.lastName) = :searchName");
		buf.append(" order by lower(p.lastName), lower(p.firstName)");

		Query q = em.createQuery(buf.toString());
		q.setParameter("searchName", searchName);

		List<Person> l = q.getResultList();
		return l;
	}

	public long countPersons(String partialName) {
		return (Long) findPersons(true, partialName, 0, 0);
	}

	@SuppressWarnings("unchecked")
	public List<Person> findPersons(String partialName, int startIndex, int maxResults) {
		return (List<Person>) findPersons(false, partialName, startIndex, maxResults);
	}

	@SuppressWarnings("unchecked")
	private Object findPersons(boolean counting, String partialName, int startIndex, int maxResults) {
		String searchName = partialName == null ? "" : partialName.toLowerCase();

		StringBuilder buf = new StringBuilder();

		if (counting) {
			buf.append("select count(p) from Person p");
		}
		else {
			buf.append("select p from Person p");
		}
		buf.append(" where lower(firstName) like :firstName");
		buf.append(" or lower(lastName) like :lastName");

		if (!counting) {
			buf.append(" order by lower(p.firstName), lower(p.lastName)");
		}

		Query q = em.createQuery(buf.toString());
		q.setParameter("firstName", "%" + searchName + "%");
		q.setParameter("lastName", "%" + searchName + "%");

		if (counting) {
			Long qty = (Long) q.getSingleResult();
			return qty;
		}
		else {
			List<Person> l = q.setFirstResult(startIndex).setMaxResults(maxResults).getResultList();
			return l;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Person> findPersons(int startIndex, int maxResults, List<SortCriterion> sortCriteria) {
		final List<String> PROPERTIES_TO_LOWER_FOR_SORT = Arrays.asList("firstName", "lastName");

		// Here we use JPQL. An alternative is to use javax.persistence.criteria.CriteriaQuery. For an example see
		// Tapestry's JpaGridDataSource.

		StringBuilder buf = new StringBuilder();
		buf.append("select p from Person p");
		buf.append(" order by ");

		boolean firstOrderByItem = true;
		boolean orderByIncludesId = false;

		for (SortCriterion sortCriterion : sortCriteria) {
			String propertyName = sortCriterion.getPropertyName();

			// Append an "order by" item, eg. "startDate", or ", lower(firstName) desc".

			if (!firstOrderByItem) {
				buf.append(", ");
			}
			if (PROPERTIES_TO_LOWER_FOR_SORT.contains(propertyName)) {
				buf.append("lower(").append(propertyName).append(")");
			}
			else {
				buf.append(propertyName);
			}
			buf.append(sortCriterion.getSortDirection().toStringForJpql());

			// We need to know later whether the "order by" includes id.

			if (propertyName.equals("id")) {
				orderByIncludesId = true;
			}
			firstOrderByItem = false;
		}

		// Ensure sequence is predictable by ensuring a unique property, id, is in the "order by".

		if (!orderByIncludesId) {
			if (!firstOrderByItem) {
				buf.append(", ");
			}
			buf.append("id");
		}

		Query q = em.createQuery(buf.toString());

		List<Person> l = q.setFirstResult(startIndex).setMaxResults(maxResults).getResultList();
		return l;
	}

	public long countPersons(String firstNameStartsWith, String lastNameStartsWith, Regions region) {
		return (Long) findPersons(true, firstNameStartsWith, lastNameStartsWith, region, 0, 0, null);
	}

	@SuppressWarnings("unchecked")
	public List<Person> findPersons(String firstNameStartsWith, String lastNameStartsWith, Regions region,
			int startIndex, int maxResults, List<SortCriterion> sortCriteria) {
		return (List<Person>) findPersons(false, firstNameStartsWith, lastNameStartsWith, region, startIndex,
				maxResults, sortCriteria);
	}

	@SuppressWarnings("unchecked")
	private Object findPersons(boolean counting, String firstNameStartsWith, String lastNameStartsWith, Regions region,
			int startIndex, int maxResults, List<SortCriterion> sortCriteria) {
		final List<String> PROPERTIES_TO_LOWER_FOR_SORT = Arrays.asList("firstName", "lastName");

		String searchFirstName = firstNameStartsWith == null ? "" : firstNameStartsWith.toLowerCase();
		String searchLastName = lastNameStartsWith == null ? "" : lastNameStartsWith.toLowerCase();

		StringBuilder buf = new StringBuilder();

		if (counting) {
			buf.append("select count(p) from Person p");
		}
		else {
			buf.append("select p from Person p");
		}

		buf.append(" where lower(firstName) like :firstName");
		buf.append(" and lower(lastName) like :lastName");
		if (region != null) {
			buf.append(" and region = :region");
		}

		if (!counting) {
			buf.append(" order by ");

			boolean firstOrderByItem = true;
			boolean orderByIncludesId = false;

			for (SortCriterion sortCriterion : sortCriteria) {
				String propertyName = sortCriterion.getPropertyName();

				// Append an "order by" item, eg. "startDate", or ", lower(firstName) desc".

				if (!firstOrderByItem) {
					buf.append(", ");
				}
				if (PROPERTIES_TO_LOWER_FOR_SORT.contains(propertyName)) {
					buf.append("lower(").append(propertyName).append(")");
				}
				else {
					buf.append(propertyName);
				}
				buf.append(sortCriterion.getSortDirection().toStringForJpql());

				// We need to know later whether the "order by" includes id.

				if (propertyName.equals("id")) {
					orderByIncludesId = true;
				}
				firstOrderByItem = false;
			}

			// Ensure sequence is predictable by ensuring a unique property, id, is in the "order by".

			if (!orderByIncludesId) {
				if (!firstOrderByItem) {
					buf.append(", ");
				}
				buf.append("id");
			}
		}

		Query q = em.createQuery(buf.toString());
		q.setParameter("firstName", searchFirstName + "%");
		q.setParameter("lastName", searchLastName + "%");
		if (region != null) {
			q.setParameter("region", region);
		}

		if (counting) {
			Long qty = (Long) q.getSingleResult();
			return qty;
		}
		else {
			List<Person> l = q.setFirstResult(startIndex).setMaxResults(maxResults).getResultList();
			return l;
		}
	}

}
