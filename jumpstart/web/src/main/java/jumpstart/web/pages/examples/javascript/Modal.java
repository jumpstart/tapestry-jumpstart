package jumpstart.web.pages.examples.javascript;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(stylesheet = "css/examples/modal.css")
public class Modal {

	public enum Function {
		REVIEW, UPDATE;
	}

	// The activation context

	@Property
	private Long personId;

	// Screen fields

	@Property
	private Person person;

	@Property
	private String personUpdateModalId = "personUpdateModal";

	// Work fields

	private Function function;

	// Generally useful bits and pieces

	@InjectComponent
	private Zone paneZone;

	@InjectComponent
	private Zone modalZone;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@Inject
	private Request request;

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	// The code

	void onActivate(Long personId) {
		this.personId = personId;
	}

	Long onPassivate() {
		return personId;
	}

	public void setupRender() {
		function = Function.REVIEW;
		populateBody();
	}

	void onToUpdate(Long personId) {
		this.personId = personId;
		function = Function.UPDATE;

		if (request.isXHR()) {
			ajaxResponseRenderer.addCallback(makeScriptToShowModal());
			ajaxResponseRenderer.addRender(modalZone);
		}
	}

	void onCanceledFromPersonUpdate(Long personId) {
		this.personId = personId;
		function = Function.REVIEW;

		populateBody();

		if (request.isXHR()) {
			ajaxResponseRenderer.addCallback(makeScriptToHideModal());
			ajaxResponseRenderer.addRender(paneZone);
		}
	}

	void onUpdatedFromPersonUpdate(Person person) {
		this.personId = person.getId();
		function = Function.REVIEW;

		populateBody();

		if (request.isXHR()) {
			ajaxResponseRenderer.addCallback(makeScriptToHideModal());
			ajaxResponseRenderer.addRender(paneZone);
		}
	}

	public void populateBody() {

		// Get person with id 1 - ask business service to find it (from the
		// database).

		person = personFinderService.findPerson(1L);

		if (person == null) {
			throw new IllegalStateException("Database data has not been set up!");
		}
	}

	private JavaScriptCallback makeScriptToShowModal() {
		return new JavaScriptCallback() {
			public void run(JavaScriptSupport javascriptSupport) {
				javaScriptSupport.require("simple-modal").invoke("activate")
						.with(personUpdateModalId, new JSONObject());
			}
		};
	}

	private JavaScriptCallback makeScriptToHideModal() {
		return new JavaScriptCallback() {
			public void run(JavaScriptSupport javascriptSupport) {
				javaScriptSupport.require("simple-modal").invoke("hide").with(personUpdateModalId);
			}
		};
	}

	public boolean isFunction(Function function) {
		return function == this.function;
	}

}
