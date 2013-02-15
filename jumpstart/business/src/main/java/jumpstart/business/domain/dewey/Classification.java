// Based on an example kindly provided by George Christman and Lance Java.

package jumpstart.business.domain.dewey;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

/**
 * Represents a Dewey Decimal Classification. See http://en.wikipedia.org/wiki/List_of_Dewey_Decimal_classes.
 */
@Entity
@SuppressWarnings("serial")
public class Classification implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Integer id;

	@Version
	@Column(nullable = false)
	private Integer version;

	private String label;

	/**
	 * Classification is in an AGGREGATION relationship with itself. Here are its child classifications and parent
	 * Classification. Be careful: cycles are not allowed.
	 */

	// Do not cascade REMOVE because this is only an AGGREGATION relationship, not a COMPOSITION relationship.
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	private Set<Classification> children = new HashSet<Classification>();

	@ManyToOne
	@JoinColumn(name = "parentId")
	private Classification parent;

	public String toString() {
		final String DIVIDER = ", ";

		StringBuilder buf = new StringBuilder();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("id=" + id + DIVIDER);
		buf.append("version=" + version + DIVIDER);
		buf.append("label=" + label);
		buf.append("]");
		return buf.toString();
	}

	// The need for an equals() method is discussed at http://www.hibernate.org/109.html

	@Override
	public boolean equals(Object obj) {
		return (obj == this) || (obj instanceof Classification) && id != null
				&& id.equals(((Classification) obj).getId());
	}

	// The need for a hashCode() method is discussed at http://www.hibernate.org/109.html

	@Override
	public int hashCode() {
		return id == null ? super.hashCode() : id.hashCode();
	}

	public Integer getId() {
		return id;
	}

	public Integer getVersion() {
		return version;
	}

	public Set<Classification> getChildren() {
		return children;
	}

	public void setChildren(Set<Classification> children) {
		this.children = children;
	}

	public Classification getParent() {
		return parent;
	}

	public void setParent(Classification parent) {
		this.parent = parent;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
