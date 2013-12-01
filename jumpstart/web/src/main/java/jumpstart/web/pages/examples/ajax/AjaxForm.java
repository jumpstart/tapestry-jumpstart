package jumpstart.web.pages.examples.ajax;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

@Import(stylesheet = "css/examples/js.css")
public class AjaxForm {

	// Screen fields

	@Property
	@NotNull
	private String firstName;

	@Property
	@NotNull
	private String lastName;

	@Property
	@NotNull
	@Past
	private Date birthday;

	// Generally useful bits and pieces

	@Inject
	private Request request;

	@Component
	private Form ajaxForm;

	@Component(id = "birthday")
	private DateField birthdayField;

	@Component(id = "firstName")
	private TextField firstNameField;

	@Component(id = "lastName")
	private TextField lastNameField;

	@InjectComponent
	private Zone formZone;

	// The code

	void setupRender() {
		if (firstName == null && lastName == null && birthday == null) {
			firstName = "Humpty";
			lastName = "Dumpty";
			birthday = new Date(0);
		}
	}

	Object onSuccess() {
		return request.isXHR() ? formZone.getBody() : null;
	}

	Object onFailure() {
		return request.isXHR() ? formZone.getBody() : null;
	}

	public String getName() {
		return firstName + " " + lastName;
	}

	public Date getTime() {
		return new Date();
	}

}
