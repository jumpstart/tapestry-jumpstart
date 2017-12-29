package jumpstart.web.pages.examples.output;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet = "css/examples/plain.css")
public class EasyOutput {

	@Property
	private String name;

	@Property
	private Gender gender;

	@Property
	private Integer age;

	void setupRender() {

		name = "Jane Citizen";
		gender = Gender.FEMALE;
		age = 25;

	}

	private enum Gender {
		MALE, FEMALE;
	}

}
