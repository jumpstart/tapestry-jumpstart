package jumpstart.web.pages.examples.output;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

@Import(stack = "core", stylesheet = "css/examples/plain.css")
public class EasyOutput {

	@Property
	private Employee employee;

	void setupRender() {
		employee = new Employee();
		employee.setName("Jane Citizen");
		employee.setAge(32);
		employee.setGender(Gender.FEMALE);
	}

	public class Employee {
		private String name;
		private Integer age;
		private Gender gender;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public Gender getGender() {
			return gender;
		}

		public void setGender(Gender gender) {
			this.gender = gender;
		}
	}

	private enum Gender {
		MALE, FEMALE;
	}

}
