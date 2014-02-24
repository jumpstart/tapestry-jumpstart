package jumpstart.web.components.together.ajaxcomponentscrud;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.web.models.together.PersonFilteredDataSource;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Submit;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * This component will trigger the following events on its container (which in this example is the page):
 * {@link jumpstart.web.components.examples.component.crud.PersonList#PERSON_SELECTED}(Long personId).
 */
// @Events is applied to a component solely to document what events it may trigger. It is not checked at runtime.
@Events({ PersonList.PERSON_SELECTED })
@Import(stylesheet = "css/together/filtercrud.css")
public class PersonList {
	public static final String PERSON_SELECTED = "personSelected";

	// Parameters

	@Parameter(required = true)
	@Property
	private Long selectedPersonId;

	// Screen fields

	@Property
	private String partialName;

	@Property
	private Person person;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@InjectComponent
	private Submit submitFilter;

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@InjectComponent
	private Form filterForm;

	@InjectComponent
	private Zone personsZone;

	@Inject
	private Request request;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	// The code

	void afterRender() {
		javaScriptSupport.require("components/together/ajaxcomponentscrud/person-list").invoke("init")
				.with(submitFilter.getClientId());
	}

	void onPrepareForSubmit(Long selectedPersonId) {
		this.selectedPersonId = selectedPersonId;
	}

	boolean onSuccess() {

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(personsZone);
		}

		// We don't want the event to bubble up, so we return true to say we've handled it.
		return true;
	}

	boolean onPersonSelected(Long oldSelectedPersonId, Long newSelectedPersonId) {
		this.selectedPersonId = newSelectedPersonId;
		// Let it bubble up.
		return false;
	}

	boolean onPaging(String partialName, Long selectedPersonId) {
		this.partialName = partialName;
		this.selectedPersonId = selectedPersonId;
		return true;
	}

	public void doChangeOfSelectedPerson() {

		if (request.isXHR()) {

			// At this point only the client-side filterForm knows the latest partialName. So we'll refresh the list by
			// asking JavaScript to submit the filterForm.
			// But also at this point the client-side filterForm does not know the latest selectedPersonId. So we'll
			// ask JavaScript to update the filterForm's context before submitting it.

			// Updating the context is a bit messy: client-side, a Form's context is within an ActionLink URI. We'll
			// generate a replacement ActionLink URI and give it to the JavaScript.

			String actionURI = componentResources.createFormEventLink(EventConstants.ACTION, (Object[]) null).toURI();

			String actionWithSelectedPersonURI = componentResources.createFormEventLink(EventConstants.ACTION,
					new Object[] { selectedPersonId }).toURI();

			String filterFormActionWithSelectedPersonURI = actionWithSelectedPersonURI.replace(actionURI, actionURI
					+ ".filterForm");

			ajaxResponseRenderer.addCallback(makeScriptToSubmitFilter(filterFormActionWithSelectedPersonURI));
		}

	}

	private JavaScriptCallback makeScriptToSubmitFilter(final String updatedFilterFormAction) {

		return new JavaScriptCallback() {
			public void run(JavaScriptSupport javascriptSupport) {
				javascriptSupport.require("components/together/ajaxcomponentscrud/person-list").invoke("submit")
						.with(updatedFilterFormAction);
			}
		};

	}

	public GridDataSource getPersons() {
		return new PersonFilteredDataSource(personFinderService, partialName);
	}

	public String getLinkCSSClass() {
		if (person != null && person.getId().equals(selectedPersonId)) {
			return "active";
		}
		else {
			return "";
		}
	}
}
