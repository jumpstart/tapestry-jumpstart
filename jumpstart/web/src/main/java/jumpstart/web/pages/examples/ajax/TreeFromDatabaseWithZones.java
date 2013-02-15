// Based on an example kindly provided by George Christman and Lance Java.

package jumpstart.web.pages.examples.ajax;

import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.domain.dewey.Classification;
import jumpstart.business.domain.dewey.iface.ClassificationNode;
import jumpstart.business.domain.dewey.iface.IDeweyFinderServiceLocal;
import jumpstart.web.model.examples.tree.ClassificationTreeModelAdapter;
import jumpstart.web.pages.Index;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Tree;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.tree.DefaultTreeModel;
import org.apache.tapestry5.tree.TreeModel;
import org.apache.tapestry5.tree.TreeNode;

public class TreeFromDatabaseWithZones {

	// Screen fields

	private TreeModel<ClassificationNode> treeModel;

	@Property
	private TreeNode<ClassificationNode> treeNode;

	@Property
	private ClassificationNode classificationNode;

	@Property
	private Classification selectedClassification;

	// Generally useful bits and pieces

	@InjectComponent
	private Tree tree;

	@InjectComponent
	private Zone treeZone;

	@InjectComponent
	private Zone selectedZone;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Request request;

	@EJB
	private IDeweyFinderServiceLocal deweyFinderService;

	@Inject
	private ComponentResources componentResources;

	// The code

	void onClearExpansions() {
		tree.clearExpansions();

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(treeZone).addRender(selectedZone);
		}
	}

	void onRefresh() {
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(treeZone).addRender(selectedZone);
		}
	}

	void onLeafSelected(Integer classificationId) {
		ClassificationNode classificationNode = deweyFinderService.findClassificationInfo(classificationId);
		selectedClassification = classificationNode.getClassification();
		
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(treeZone).addRender(selectedZone);
		}
	}

	Object onHome() {
		componentResources.discardPersistentFieldChanges();
		return Index.class;
	}

	// Getters and setters

	public TreeModel<ClassificationNode> getTreeModel() {
		if (treeModel == null) {
			ValueEncoder<ClassificationNode> encoder = new ValueEncoder<ClassificationNode>() {

				@Override
				public String toClient(ClassificationNode node) {
					return node.getClassification().getId().toString();
				}

				@Override
				public ClassificationNode toValue(String node) {
					return deweyFinderService.findClassificationInfo(new Integer(node));
				}
			};

			treeModel = new DefaultTreeModel<ClassificationNode>(encoder, new ClassificationTreeModelAdapter(deweyFinderService),
					deweyFinderService.findRoots());
		}
		return treeModel;
	}

	public List<ClassificationNode> getHasResults() {
		return deweyFinderService.findRoots();
	}

	public String getLeafClass() {
		if (selectedClassification != null && classificationNode.getClassification().equals(selectedClassification)) {
			return "selected";
		}
		else {
			return "";
		}
	}

}
