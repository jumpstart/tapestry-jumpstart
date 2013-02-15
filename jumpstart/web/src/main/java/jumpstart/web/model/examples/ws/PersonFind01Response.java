package jumpstart.web.model.examples.ws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "serial", "unused" })
public class PersonFind01Response implements Serializable {

	private Person01 person;

	private PersonFind01Response() {
	}

	public PersonFind01Response(Person01 person) {
		this.person = person;
	}

}
