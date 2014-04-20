package jumpstart.web.pages.examples.ajax;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

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

	@InjectComponent
	private Zone formZone;

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

	void onSuccess() {
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(formZone);
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

	public Date getServerTime() {
		return new Date();
	}

}
