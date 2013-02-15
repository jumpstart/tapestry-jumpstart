package jumpstart.web.model.examples.ws;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import jumpstart.business.domain.person.Regions;

@XmlRootElement(name = "person")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "serial", "unused" })
public class Person01 implements Serializable {

	@XmlAttribute(name = "id")
	private Long id;

	private String firstName;
	private String lastName;
	private Regions region;
	private Date startDate;

	private Person01() {
	}

	public Person01(Long id, String firstName, String lastName, Regions region, Date startDate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.region = region;
		this.startDate = startDate;
	}

}
