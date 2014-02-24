package jumpstart.web.pages.together.ajaxcomponentscrud;

import jumpstart.web.components.together.ajaxcomponentscrud.PersonList;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

@Import(stylesheet = "css/together/ajaxcomponentscrud.css")
public class Persons {

	public enum Function {
		CREATE, REVIEW, UPDATE;
	}

	// Screen fields

	@Property
	private Function function;
	
	@Property
	private Long listPersonId;

	@Property
	private Long editorPersonId;

	// Generally useful bits and pieces

	@InjectComponent
	private PersonList list; 
	
	@InjectComponent
	private Zone editorZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	// The code

	void onToCreate() {
		function = Function.CREATE;
		editorPersonId = null;
		
		list.doChangeOfSelectedPerson();

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	void onPersonSelectedFromList() {
		function = Function.REVIEW;
		editorPersonId = listPersonId;

		list.doChangeOfSelectedPerson();

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// CREATE
	// /////////////////////////////////////////////////////////////////////

	void onCanceledFromPersonCreate() {
		function = null;
		editorPersonId = null;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	void onCreatedFromPersonCreate(Long personId) {
		function = Function.REVIEW;
		editorPersonId = personId;
		listPersonId = personId;

		list.doChangeOfSelectedPerson();

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// REVIEW
	// /////////////////////////////////////////////////////////////////////

	void onToUpdateFromPersonReview(Long personId) {
		function = Function.UPDATE;
		editorPersonId = personId;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	void onDeletedFromPersonReview(Long personId) {
		function = null;
		editorPersonId = null;
		listPersonId = null;

		list.doChangeOfSelectedPerson();

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// UPDATE
	// /////////////////////////////////////////////////////////////////////

	void onCanceledFromPersonUpdate(Long personId) {
		function = Function.REVIEW;
		editorPersonId = personId;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	void onUpdatedFromPersonUpdate(Long personId) {
		function = Function.REVIEW;
		editorPersonId = personId;
		listPersonId = personId;

		list.doChangeOfSelectedPerson();

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// GETTERS ETC.
	// /////////////////////////////////////////////////////////////////////

	public boolean isFunction(Function function) {
		return this.function == function;
	}
}
