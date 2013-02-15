package jumpstart.web.model.examples.ws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "persons")
@XmlAccessorType(XmlAccessType.FIELD)
public class Persons01 {

	@XmlElement(name = "person")
	private List<Person01> persons = new ArrayList<Person01>();

	public Persons01() {
	}

	public void add(Person01 person) {
		persons.add(person);
	}

}