// Based on an example kindly provided by George Christman and Lance Java.

package jumpstart.business.domain.dewey;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jumpstart.business.domain.dewey.iface.ClassificationNode;
import jumpstart.business.domain.dewey.iface.IDeweyFinderServiceLocal;
import jumpstart.business.domain.dewey.iface.IDeweyFinderServiceRemote;

@Stateless
@Local(IDeweyFinderServiceLocal.class)
@Remote(IDeweyFinderServiceRemote.class)
public class DeweyFinderService implements IDeweyFinderServiceLocal, IDeweyFinderServiceRemote {

	@PersistenceContext(unitName = "jumpstart")
	private EntityManager em;

	public Classification findClassification(Integer id) {
		return em.find(Classification.class, id);
	}

	public ClassificationNode findClassificationInfo(Integer id) {
		List<Classification> classifications = new ArrayList<Classification>();
		classifications.add(findClassification(id));
		return getClassificationInfo(classifications).iterator().next();
	}

	public List<ClassificationNode> getChildren(ClassificationNode node) {
		List<Classification> classifications = findChildren(node.getClassification().getId());
		return getClassificationInfo(classifications);
	}

	public List<ClassificationNode> findRoots() {
		List<Classification> classifications = findClassificationsWithNoParent();
		if (classifications == null) {
			return new ArrayList<ClassificationNode>();
		}
		return getClassificationInfo(classifications);
	}

	@SuppressWarnings("unchecked")
	private List<Classification> findChildren(Integer id) {
		return em.createQuery("select c from Classification c where c.parent.id = :id").setParameter("id", id)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	private List<Classification> findClassificationsWithNoParent() {
		return em.createQuery("select c from Classification c where c.parent is null").getResultList();
	}

	/**
	 * Given a list of Classifications, returns a list of ClassificationNodes. Each one represents a Classification and some info about
	 * the Classification: specifically whether it has children or is a leaf.
	 */
	@SuppressWarnings("unchecked")
	private List<ClassificationNode> getClassificationInfo(List<Classification> classifications) {
		Map<Integer, ClassificationNode> classificationNodesById = new LinkedHashMap<Integer, ClassificationNode>();

		// Build a map of skeleton ClassificationNodes by Classification id - one entry per given Classification.

		for (Classification classification : classifications) {
			ClassificationNode classificationNode = new ClassificationNode();
			classificationNode.setClassification(classification);
			classificationNodesById.put(classification.getId(), classificationNode);
		}

		if (!classificationNodesById.isEmpty()) {

			// Query whether each Classification has children.

			StringBuilder buf = new StringBuilder();

			// This JPQL query should have worked but Hibernate translates the count to "count(.)" which is invalid SQL
			// (a Hibernate bug?)...
			// buf.append("select c1.id, count(c1.children) from Classification c1");
			// buf.append(" where c1.id in (:catIds) ");
			// buf.append(" group by c1.id");
			// Query q = em.createQuery(buf.toString());

			// ...so we use a native query instead
			buf.append("select c1.id, count(c2.id) from Classification c1");
			buf.append(" left join Classification c2 on c2.parentId = c1.id");
			buf.append(" where c1.id in (:catIds) ");
			buf.append(" group by c1.id");
			Query q = em.createNativeQuery(buf.toString());

			q.setParameter("catIds", classificationNodesById.keySet());
			List<Object[]> l = q.getResultList();

			// Update each Classification's corresponding ClassificationNode in the map with whether it has children or is a leaf.

			for (Object[] result : l) {
				Integer classificationId = (Integer) result[0];
				int childCount = ((Number) result[1]).intValue();

				ClassificationNode classificationNode = classificationNodesById.get(classificationId);
				classificationNode.setHasChildren(childCount != 0);
				classificationNode.setIsLeaf(childCount == 0);
			}
		}

		return new ArrayList<ClassificationNode>(classificationNodesById.values());
	}

}
