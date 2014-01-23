package jumpstart.web.pages.together.componentscrud;

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

	void setupRender() {
		listPersonId = editorPersonId;
	}

	void onToCreate() {
		editorMode = Mode.CREATE;
		editorPersonId = null;
	}

	void onSelectedFromList(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
	}

	// /////////////////////////////////////////////////////////////////////
	// CREATE
	// /////////////////////////////////////////////////////////////////////

	void onCancelCreateFromEditor() {
		editorMode = null;
		editorPersonId = null;
	}

	void onSuccessfulCreateFromEditor(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
	}

	// /////////////////////////////////////////////////////////////////////
	// REVIEW
	// /////////////////////////////////////////////////////////////////////

	void onToUpdateFromEditor(Long personId) {
		editorMode = Mode.UPDATE;
		editorPersonId = personId;
	}

	void onSuccessfulDeleteFromEditor(Long personId) {
		editorMode = null;
		editorPersonId = null;
	}

	void onFailedDeleteFromEditor(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
	}

	// /////////////////////////////////////////////////////////////////////
	// UPDATE
	// /////////////////////////////////////////////////////////////////////

	void onCancelUpdateFromEditor(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
	}

	void onSuccessfulUpdateFromEditor(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
	}

}
