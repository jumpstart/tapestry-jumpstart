package jumpstart.business.domain.dewey.iface;

import java.util.List;

import jumpstart.business.domain.dewey.Classification;


/**
 * The <code>IDeweyFinderServiceRemote</code> bean exposes the business methods in the interface.
 */
public interface IDeweyFinderServiceRemote {

	// Classification 

	Classification findClassification(Integer id);

	ClassificationNode findClassificationInfo(Integer id);

	List<ClassificationNode> getChildren(ClassificationNode node);

	List<ClassificationNode> findRoots();

}
