package jumpstart.web.pages.together.ajaxcomponentscrud;

import jumpstart.web.components.together.ajaxcomponentscrud.PersonEditor.Mode;

import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

public class Persons {

	// Screen fields

	@Property
	@Persist
	private boolean highlightZoneUpdates;

	@Property
	// If we use @ActivationRequestParameter instead of @Persist, then our handler for filter form success would have
	// to render more than just the listZone, it would have to render all other links and forms: it would need a zone
	// around the "Create..." link so it could render it; and it would render the editorZone, which would be destructive
	// if the user has been typing into Create or Update. Alternatively, it could use a custom JavaScript callback to
	// update the partialName in all other links and forms - see AjaxResponseRenderer#addCallback(JavaScriptCallback).
	@Persist
	private String partialName;

	@Property
	private Long listPersonId;

	@Property
	@ActivationRequestParameter
	private Mode editorMode;

	@Property
	@ActivationRequestParameter
	private Long editorPersonId;

	// Generally useful bits and pieces

	@InjectComponent
	private Zone listZone;

	@InjectComponent
	private Zone editorZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	// The code

	void onActivate() {
		listPersonId = editorPersonId;
	}

	// /////////////////////////////////////////////////////////////////////
	// FILTER
	// /////////////////////////////////////////////////////////////////////

	// Handle event "filter" from component "list"

	void onFilterFromList() {
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(listZone);
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// CREATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toCreate" from this page

	void onToCreate() {
		editorMode = Mode.CREATE;
		editorPersonId = null;
		listPersonId = null;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(listZone).addRender(editorZone);
		}
	}

	// Handle event "cancelCreate" from component "editor"

	void onCancelCreateFromEditor() {
		editorMode = null;
		editorPersonId = null;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	// Handle event "successfulCreate" from component "editor"

	void onSuccessfulCreateFromEditor(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
		listPersonId = personId;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(listZone).addRender(editorZone);
		}
	}

	// Handle event "failedCreate" from component "editor"

	void onFailedCreateFromEditor() {
		editorMode = Mode.CREATE;
		editorPersonId = null;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// REVIEW
	// /////////////////////////////////////////////////////////////////////

	// Handle event "selected" from component "list"

	void onSelectedFromList(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
		listPersonId = personId;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(listZone).addRender(editorZone);
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// UPDATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toUpdate" from component "editor"

	void onToUpdateFromEditor(Long personId) {
		editorMode = Mode.UPDATE;
		editorPersonId = personId;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	// Handle event "cancelUpdate" from component "editor"

	void onCancelUpdateFromEditor(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	// Handle event "successfulUpdate" from component "editor"

	void onSuccessfulUpdateFromEditor(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
		listPersonId = personId;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(listZone).addRender(editorZone);
		}
	}

	// Handle event "failedUpdate" from component "editor"

	void onFailedUpdateFromEditor(Long personId) {
		editorMode = Mode.UPDATE;
		editorPersonId = personId;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// DELETE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "successfulDelete" from component "editor"

	void onSuccessfulDeleteFromEditor(Long personId) {
		editorMode = null;
		editorPersonId = null;
		listPersonId = null;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(listZone).addRender(editorZone);
		}
	}

	// Handle event "failedDelete" from component "editor"

	void onFailedDeleteFromEditor(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// GETTERS
	// /////////////////////////////////////////////////////////////////////

	public String getZoneUpdateFunction() {
		return highlightZoneUpdates ? "highlight" : "show";
	}

}
