package jumpstart.business.domain.security.iface;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserRoleSearchFields implements Serializable {

	private Long userId = null;
	private Long roleId = null;
	private Integer version = null;

	public UserRoleSearchFields() {
	}

	public UserRoleSearchFields(Long userId, Long roleId, Integer version) {
		this.userId = userId;
		this.roleId = roleId;
		this.version = version;
	}

	public UserRoleSearchFields(UserRoleSearchFields copyFrom) {
		// No defensive copies are created here because there are no mutable object fields (String is immutable)
		this(copyFrom.userId, copyFrom.roleId, copyFrom.version);
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("UserRoleSearchFields: [");
		buf.append("userId=" + userId + ", ");
		buf.append("roleId=" + roleId + ", ");
		buf.append("version=" + version);
		buf.append("]");
		return buf.toString();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
