package jumpstart.business.domain.security.iface;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class UserSearchFields implements Serializable {

	private String loginId = null;
	private String salutation = null;
	private String firstName = null;
	private String lastName = null;
	private String emailAddress = null;
	private Date expiryDate = null;
	private Boolean active = null;
	private Integer version = null;

	public UserSearchFields() {
	}

	public UserSearchFields(String loginId, String salutation, String firstName, String lastName, String emailAddress,
			Date expiryDate, Boolean active, Integer version) {
		super();
		this.loginId = loginId;
		this.salutation = salutation;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.expiryDate = expiryDate;
		this.active = active;
		this.version = version;
	}

	public UserSearchFields(UserSearchFields copyFrom) {
		// No defensive copies are created here, since there are no mutable object fields (String is immutable)
		this(copyFrom.loginId, copyFrom.salutation, copyFrom.firstName, copyFrom.lastName, copyFrom.emailAddress,
				copyFrom.expiryDate, copyFrom.active, copyFrom.version);
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("UserSearchFields: [");
		buf.append("loginId=" + loginId + ", ");
		buf.append("salutation=" + salutation + ", ");
		buf.append("firstName=" + firstName + ", ");
		buf.append("lastName=" + lastName + ", ");
		buf.append("emailAddress=" + emailAddress + ", ");
		buf.append("expiryDate=" + expiryDate + ", ");
		buf.append("active=" + active + ", ");
		buf.append("version=" + version);
		buf.append("]");
		return buf.toString();
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
