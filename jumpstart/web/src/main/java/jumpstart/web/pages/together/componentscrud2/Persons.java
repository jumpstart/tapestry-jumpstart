package jumpstart.web.pages.together.componentscrud2;

import jumpstart.web.components.together.componentscrud.PersonEditorForm.Mode;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet = "css/together/filtercrud.css")
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

	// Handle event "cancelCreate" from component "personCreate"

	void onCancelFromPersonCreate() {
		editorMode = null;
		editorPersonId = null;
	}

	// Handle event "successfulCreate" from component "personCreate"

	void onSuccessfulCreateFromPersonCreate(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
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

	// Handle event "toUpdate" from component "personReview"

	void onToUpdateFromPersonReview(Long personId) {
		editorMode = Mode.UPDATE;
		editorPersonId = personId;
	}

	// Handle event "cancelUpdate" from component "personUpdate"

	void onCancelUpdateFromPersonUpdate(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
	}

	// Handle event "successfulUpdate" from component "personUpdate"

	void onSuccessfulUpdatePersonUpdate(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
	}

	// /////////////////////////////////////////////////////////////////////
	// DELETE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "successfulDelete" from component "personReview"

	void onSuccessfulDeleteFromPersonReview(Long personId) {
		editorMode = null;
		editorPersonId = null;
	}

	// Handle event "failedDelete" from component "personReview"

	void onFailedDeleteFromPersonReview(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
	}

	// /////////////////////////////////////////////////////////////////////
	// OTHER
	// /////////////////////////////////////////////////////////////////////

	// Getters

	public boolean isModeCreate() {
		return editorMode == Mode.CREATE;
	}

	public boolean isModeReview() {
		return editorMode == Mode.REVIEW;
	}

	public boolean isModeUpdate() {
		return editorMode == Mode.UPDATE;
	}
}
