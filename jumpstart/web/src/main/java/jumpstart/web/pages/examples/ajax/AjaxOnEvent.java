package jumpstart.web.pages.examples.ajax;

import java.util.Date;

import jumpstart.web.pages.Index;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

@Import(stylesheet = "css/examples/js.css")
public class AjaxOnEvent {

	// Screen fields

	@Property
	@Persist
	private String firstName;

	@Property
	@Persist
	private String lastName;

	// Generally useful bits and pieces

	@InjectComponent
	private Zone nameZone;

	@Inject
	private Request request;

	@Inject
	private ComponentResources componentResources;

	// The code

	// Life-cycle stuff. Fields that are marked @Persist MUST be initialized here rather than where they are declared.

	void setupRender() {
		if (firstName == null && lastName == null) {
			firstName = "Humpty";
			lastName = "Dumpty";
		}
	}

	Object onFirstNameChanged() {
		firstName = request.getParameter("param");
		if (firstName == null) {
			firstName = "";
		}
		return request.isXHR() ? nameZone.getBody() : null;
	}

	Object onLastNameChanged() {
		lastName = request.getParameter("param");
		if (lastName == null) {
			lastName = "";
		}
		return request.isXHR() ? nameZone.getBody() : null;
	}

	public String getName() {
		return firstName + " " + lastName;
	}

	public Date getTime() {
		return new Date();
	}

	Object onGoHome() {
		componentResources.discardPersistentFieldChanges();
		return Index.class;
	}
}
