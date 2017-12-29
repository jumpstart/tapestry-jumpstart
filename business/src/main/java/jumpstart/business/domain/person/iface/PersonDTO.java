package jumpstart.business.domain.person.iface;

import java.util.Date;

import jumpstart.business.domain.person.Regions;

public class PersonDTO {
	private Long id;
	private Integer version;
	private String firstName;
	private String lastName;
	private Regions region;
	private Date startDate;
	private boolean delete;

	public String toString() {
		final String DIVIDER = ", ";

		StringBuilder buf = new StringBuilder();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("id=" + id + DIVIDER);
		buf.append("version=" + version + DIVIDER);
		buf.append("firstName=" + firstName + DIVIDER);
		buf.append("lastName=" + lastName + DIVIDER);
		buf.append("region=" + region + DIVIDER);
		buf.append("startDate=" + startDate + DIVIDER);
		buf.append("delete=" + delete);
		buf.append("]");
		return buf.toString();
	}

	public PersonDTO() {
	}

	public PersonDTO(Long id) {
		this.id = id;
	}

	public PersonDTO(Long id, Integer version, String firstName, String lastName, Regions region, Date startDate) {
		super();
		this.id = id;
		this.version = version;
		this.firstName = firstName;
		this.lastName = lastName;
		this.region = region;
		this.startDate = startDate;
		this.delete = false;
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

	public Regions getRegion() {
		return region;
	}

	public void setRegion(Regions region) {
		this.region = region;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
