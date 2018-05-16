package jumpstart.web.pages.examples.ajax;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

@Import(stylesheet = "css/examples/js.css")
public class AjaxFormMultipleZoneUpdate {

	// Screen fields

	@Property
	@NotNull
	@Size(max = 10)
	private String firstName;

	@Property
	@NotNull
	@Size(max = 10)
	private String lastName;

	@Property
	@NotNull
	@Past
	private Date birthday;

	// Generally useful bits and pieces

	@Inject
	private Request request;

	@InjectComponent("ajaxForm")
	private Form form;

	@InjectComponent("firstName")
	private TextField firstNameField;

	@InjectComponent
	private Zone formZone;

	@InjectComponent
	private Zone outZone;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	// The code

	void setupRender() {
		if (firstName == null && lastName == null && birthday == null) {
			firstName = "Humpty";
			lastName = "Dumpty";
			birthday = new Date(0);
		}
	}

	void onValidateFromAjaxForm() {

		// Note, this method is triggered even if server-side validation has already found error(s).

		if (firstName != null && firstName.equals("Acme")) {
			form.recordError(firstNameField, "First Name must not be Acme.");
		}

	}

	void onSuccess() {
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(formZone).addRender(outZone);
		}
	}

	void onFailure() {
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(formZone);
		}
	}

	public String getName() {
		return firstName + " " + lastName;
	}

}
