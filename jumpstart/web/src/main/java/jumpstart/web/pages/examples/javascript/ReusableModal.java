package jumpstart.web.pages.examples.javascript;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.web.components.SimpleModal;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

@Import(stylesheet = "css/examples/reusablemodal.css")
public class ReusableModal {

	public enum Function {
		REVIEW, UPDATE;
	}

	// The activation context

	@Property
	private Long personId;

	// Screen fields

	@Property
	private Person person;

	// Work fields

	private Function function;

	// Generally useful bits and pieces

	@InjectComponent
	private Zone paneZone;

	@InjectComponent
	private Zone modalZone;

	@InjectComponent
	private SimpleModal personUpdateModal;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@Inject
	private Request request;

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
			ajaxResponseRenderer.addRender(modalZone);
		}
	}

	void onCanceledFromPersonUpdate(Long personId) {
		this.personId = personId;
		function = Function.REVIEW;

		personUpdateModal.hide();

		populateBody();

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(paneZone);
		}
	}

	void onUpdatedFromPersonUpdate(Long personId) {
		this.personId = personId;
		function = Function.REVIEW;

		personUpdateModal.hide();

		populateBody();

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(paneZone);
		}
	}

	public void populateBody() {

		// Get person with id 1 - ask business service to find it (from the
		// database).

		person = personFinderService.findPerson(1L);

		if (person == null) {
			throw new IllegalStateException(
					"Database data has not been set up!");
		}
	}

	public boolean isFunction(Function function) {
		return function == this.function;
	}

}
