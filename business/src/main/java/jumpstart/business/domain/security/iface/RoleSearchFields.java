package jumpstart.business.domain.security.iface;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RoleSearchFields implements Serializable {

	private String name = "";
	private Integer version = null;

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("RoleSearchFields: [");
		buf.append("name=" + name + ", ");
		buf.append("version=" + version);
		buf.append("]");
		return buf.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
