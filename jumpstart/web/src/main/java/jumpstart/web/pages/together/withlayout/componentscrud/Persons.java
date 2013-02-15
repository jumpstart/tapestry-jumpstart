package jumpstart.web.pages.together.withlayout.componentscrud;

import jumpstart.web.components.together.componentscrud.PersonEditor.Mode;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.Property;

public class Persons {

	// The activation context

	@Property
	private Mode editorMode;

	@Property
	private Long editorPersonId;

	// Screen fields

	@Property
	@ActivationRequestParameter
	private String partialName;

	@Property
	private Long listPersonId;

	// The code

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	Object[] onPassivate() {

		if (editorMode == null) {
			return null;
		}
		else if (editorMode == Mode.CREATE) {
			return new Object[] { editorMode };
		}
		else if (editorMode == Mode.REVIEW || editorMode == Mode.UPDATE) {
			return new Object[] { editorMode, editorPersonId };
		}
		else {
			throw new IllegalStateException(editorMode.toString());
		}

	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(EventContext ec) {

		if (ec.getCount() == 0) {
			editorMode = null;
			editorPersonId = null;
		}
		else if (ec.getCount() == 1) {
			editorMode = ec.get(Mode.class, 0);
			editorPersonId = null;
		}
		else {
			editorMode = ec.get(Mode.class, 0);
			editorPersonId = ec.get(Long.class, 1);
		}

	}

	// setupRender() is called by Tapestry right before it starts rendering the page.

	void setupRender() {
		listPersonId = editorPersonId;
	}

	// /////////////////////////////////////////////////////////////////////
	// CREATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toCreate" from this page

	void onToCreate() {
		editorMode = Mode.CREATE;
		editorPersonId = null;
	}

	// Handle event "cancelCreate" from component "editor"

	void onCancelCreateFromEditor() {
		editorMode = null;
		editorPersonId = null;
	}

	// Handle event "successfulCreate" from component "editor"

	void onSuccessfulCreateFromEditor(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
	}

	// Handle event "failedCreate" from component "editor"

	void onFailedCreateFromEditor() {
		editorMode = Mode.CREATE;
		editorPersonId = null;
	}

	// /////////////////////////////////////////////////////////////////////
	// REVIEW
	// /////////////////////////////////////////////////////////////////////

	// Handle event "selected" from component "list"

	void onSelectedFromList(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
	}

	// /////////////////////////////////////////////////////////////////////
	// UPDATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toUpdate" from component "editor"

	void onToUpdateFromEditor(Long personId) {
		editorMode = Mode.UPDATE;
		editorPersonId = personId;
	}

	// Handle event "cancelUpdate" from component "editor"

	void onCancelUpdateFromEditor(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
	}

	// Handle event "successfulUpdate" from component "editor"

	void onSuccessfulUpdateFromEditor(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
	}

	// Handle event "failedUpdate" from component "editor"

	void onFailedUpdateFromEditor(Long personId) {
		editorMode = Mode.UPDATE;
		editorPersonId = personId;
	}

	// /////////////////////////////////////////////////////////////////////
	// DELETE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "successfulDelete" from component "editor"

	void onSuccessfulDeleteFromEditor(Long personId) {
		editorMode = null;
		editorPersonId = null;
	}

	// Handle event "failedDelete" from component "editor"

	void onFailedDeleteFromEditor(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
	}

}
