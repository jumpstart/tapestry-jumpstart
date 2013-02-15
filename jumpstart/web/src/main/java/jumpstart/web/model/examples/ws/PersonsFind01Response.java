package jumpstart.web.model.examples.ws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonsFind01Response {

	@XmlElementWrapper(name = "persons")
	@XmlElement(name = "person")
	private List<Person01> persons = new ArrayList<Person01>();

	public PersonsFind01Response() {
	}

	public void add(Person01 person) {
		persons.add(person);
	}

}