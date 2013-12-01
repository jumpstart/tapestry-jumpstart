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
public class AjaxFormMultipleZoneUpdate {

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

	@Component
	private Form ajaxForm;

	@InjectComponent
	private Zone formZone;

	@InjectComponent
	private Zone outZone;

	@Inject
	private Request request;

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

	public Date getTime() {
		return new Date();
	}

}
