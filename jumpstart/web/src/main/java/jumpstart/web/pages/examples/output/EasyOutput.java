package jumpstart.web.pages.examples.output;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

@Import(stack = "core", stylesheet="css/examples/plain.css")
public class EasyOutput {
	
	@Property
	private String name;

	@Property
	private Integer age;

	@Property
	private Gender gender;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	void setupRender() {
		
		name = "Jane Citizen";
		age = 25;
		gender = Gender.FEMALE;

	}

	private enum Gender {
		MALE, FEMALE;
	}

}
