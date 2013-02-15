package jumpstart.web.components.together.ajaxcomponentscrud;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.web.model.together.PersonFilteredDataSource;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * This component will trigger the following events on its container (which in this example is the page):
 * {@link jumpstart.web.components.examples.ajax.componentscrud.PersonList#SELECTED}(Long selectedPersonId).
 */
// @Events is applied to a component solely to document what events it may trigger. It is not checked at runtime.
@Events({ PersonList.SELECTED })
public class PersonList {
	public static final String SELECTED = "selected";

	// Parameters

	@Parameter(required = true)
	@Property
	private String partialName;

	@Parameter(required = true)
	@Property
	private Long selectedPersonId;

	// Screen fields

	@Property
	private Person person;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@Inject
	private ComponentResources componentResources;

	// The code

	boolean onSuccessFromFilterForm() {
		// Trigger new event "filter" which will bubble up.
		componentResources.triggerEvent("filter", null, null);
		// We don't want "success" to bubble up, so we return true to say we've handled it.
		return true;
	}

	// Handle event "selected"

	boolean onSelected(Long personId) {
		// Return false, which means we haven't handled the event so bubble it up.
		// This method is here solely as documentation, because without this method the event would bubble up anyway.
		return false;
	}

	// Getters

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
