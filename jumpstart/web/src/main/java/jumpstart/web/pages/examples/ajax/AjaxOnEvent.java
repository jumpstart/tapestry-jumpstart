package jumpstart.web.pages.examples.ajax;

import java.util.Date;

import jumpstart.web.pages.Index;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

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
	private AjaxResponseRenderer ajaxResponseRenderer;

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

	void onFirstNameChanged(@RequestParameter(value = "param", allowBlank = true) String firstName) {
		if (firstName == null) {
			firstName = "";
		}

		this.firstName = firstName;

		if (request.isXHR()) {
			// Here we can do whatever updates we want, then return the content we want rendered.
			ajaxResponseRenderer.addRender(nameZone);
		}
	}

	void onLastNameChanged(@RequestParameter(value = "param", allowBlank = true) String lastName) {
		if (lastName == null) {
			lastName = "";
		}

		this.lastName = lastName;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(nameZone);
		}
	}

	public String getName() {
		return firstName + " " + lastName;
	}

	public Date getServerTime() {
		return new Date();
	}

	Object onGoHome() {
		componentResources.discardPersistentFieldChanges();
		return Index.class;
	}
}
