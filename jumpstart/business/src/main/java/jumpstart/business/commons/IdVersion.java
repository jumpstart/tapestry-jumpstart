package jumpstart.business.commons;

import java.io.Serializable;

// A handy holder for an (id, version).

public class IdVersion implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer version;

	public String toString() {
		final String DIVIDER = ", ";
		
		StringBuilder buf = new StringBuilder();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("id=" + id + DIVIDER);
		buf.append("version=" + version);
		buf.append("]");
		return buf.toString();
	}

	public IdVersion(Long id, Integer version) {
		super();
		this.id = id;
		this.version = version;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
