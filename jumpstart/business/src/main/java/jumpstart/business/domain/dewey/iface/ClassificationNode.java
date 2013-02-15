// Based on an example kindly provided by George Christman and Lance Java.

package jumpstart.business.domain.dewey.iface;

import java.io.Serializable;

import jumpstart.business.domain.dewey.Classification;

/**
 * ClassificationNode is a convenience wrapper around a Classification, summarising whether it has children or is a leaf.
 */
@SuppressWarnings("serial")
public class ClassificationNode implements Serializable {

	private Classification classification;
	private boolean isLeaf;
	private boolean hasChildren;
	
	@Override
	public String toString() {
		return "ClassificationNode [classification=" + classification + ", isLeaf=" + isLeaf + ", hasChildren=" + hasChildren + "]";
	}

	public Classification getClassification() {
		return classification;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	public boolean hasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

}
