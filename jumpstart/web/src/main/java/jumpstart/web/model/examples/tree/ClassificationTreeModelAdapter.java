// Based on an example kindly provided by George Christman and Lance Java.

package jumpstart.web.model.examples.tree;

import java.util.List;

import jumpstart.business.domain.dewey.iface.ClassificationNode;
import jumpstart.business.domain.dewey.iface.IDeweyFinderServiceLocal;

import org.apache.tapestry5.tree.TreeModelAdapter;

public class ClassificationTreeModelAdapter implements TreeModelAdapter<ClassificationNode> {

	private IDeweyFinderServiceLocal deweyFinderService;

	public ClassificationTreeModelAdapter(IDeweyFinderServiceLocal deweyFinderService) {
		this.deweyFinderService = deweyFinderService;
	}

	public List<ClassificationNode> getChildren(ClassificationNode node) {
		return deweyFinderService.getChildren(node);
	}

	public boolean isLeaf(ClassificationNode node) {
		return node.isLeaf();
	}

	public boolean hasChildren(ClassificationNode node) {
		return node.hasChildren();
	}

	public String getLabel(ClassificationNode node) {
		return node.getClassification().getLabel();
	}
}
