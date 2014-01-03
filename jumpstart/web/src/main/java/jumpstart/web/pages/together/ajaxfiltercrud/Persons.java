package jumpstart.web.pages.together.ajaxfiltercrud;

import java.text.Format;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.models.together.PersonFilteredDataSource;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

public class Persons {

	private final String demoModeStr = System.getProperty("jumpstart.demo-mode");

	private enum Mode {
		CREATE, REVIEW, UPDATE;
	}

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
	private Person listPerson;

	@Property
	@ActivationRequestParameter
	private Mode editorMode;

	@Property
	@ActivationRequestParameter
	private Long editorPersonId;

	@Property
	private Person editorPerson;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String deleteMessage;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@EJB
	private IPersonManagerServiceLocal personManagerService;

	@Component
	// FIX!
	// private CustomForm createForm;
	private Form createForm;

	@Component
	// FIX!
	// private CustomForm updateForm;
	private Form updateForm;

	@InjectComponent
	private Zone listZone;

	@InjectComponent
	private Zone editorZone;

	@Inject
	private Messages messages;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	// The code

	// /////////////////////////////////////////////////////////////////////
	// FILTER
	// /////////////////////////////////////////////////////////////////////

	void onSuccessFromFilterForm() {
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(listZone);
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// CREATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toCreate"

	void onToCreate() {
		editorMode = Mode.CREATE;
		editorPersonId = null;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(listZone).addRender(editorZone);
		}
	}

	// Handle event "cancelCreate"

	void onCancelCreate() {
		editorMode = null;
		editorPersonId = null;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	// Component "createForm" bubbles up the PREPARE event when it is rendered or submitted

	void onPrepareFromCreateForm() throws Exception {
		editorMode = Mode.CREATE;
		// Instantiate a Person for the form data to overlay.
		editorPerson = new Person();
	}

	// Component "createForm" bubbles up the VALIDATE event when it is submitted

	void onValidateFromCreateForm() {

		if (createForm.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		if (demoModeStr != null && demoModeStr.equals("true")) {
			createForm.recordError("Sorry, but Create is not allowed in Demo mode.");
			return;
		}

		try {
			editorPerson = personManagerService.createPerson(editorPerson);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			createForm.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	// Component "createForm" bubbles up SUCCESS or FAILURE when it is submitted, depending on whether VALIDATE
	// records an error

	void onSuccessFromCreateForm() {
		editorMode = Mode.REVIEW;
		editorPersonId = editorPerson.getId();

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(listZone).addRender(editorZone);
		}
	}

	void onFailureFromCreateForm() {
		editorMode = Mode.CREATE;
		editorPersonId = null;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// REVIEW
	// /////////////////////////////////////////////////////////////////////

	// Handle event "selected"

	void onSelected(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;

		if (request.isXHR()) {
			editorPerson = personFinderService.findPerson(personId);
			ajaxResponseRenderer.addRender(listZone).addRender(editorZone);
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// UPDATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toUpdate"

	void onToUpdate(Long personId) {
		editorMode = Mode.UPDATE;
		editorPersonId = personId;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	// Handle event "cancelUpdate"

	void onCancelUpdate(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;

		if (request.isXHR()) {
			editorPerson = personFinderService.findPerson(personId);
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	// Component "updateForm" bubbles up the PREPARE_FOR_RENDER event when it is rendered

	void onPrepareForRenderFromUpdateForm(Long personId) {
		editorMode = Mode.UPDATE;
		editorPersonId = personId;

		// If the form is valid then we're not redisplaying due to error, so get the editorPerson.

		if (updateForm.isValid()) {
			editorPerson = personFinderService.findPerson(personId);
			// Handle null editorPerson in the template.
		}
	}

	// Component "updateForm" bubbles up the PREPARE_FOR_SUBMIT event when it is submitted

	void onPrepareForSubmitFromUpdateForm(Long personId) {
		editorPersonId = personId;

		// Get objects for the form fields to overlay.
		editorPerson = personFinderService.findPerson(personId);

		if (editorPerson == null) {
			editorPerson = new Person();
			updateForm.recordError("Person has been deleted by another process.");
		}
	}

	// Component "updateForm" bubbles up the VALIDATE event when it is submitted

	void onValidateFromUpdateForm() {

		if (updateForm.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		try {
			personManagerService.changePerson(editorPerson);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			updateForm.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	// Component "updateForm" bubbles up SUCCESS or FAILURE when it is submitted, depending on whether VALIDATE
	// records an error

	void onSuccessFromUpdateForm() {
		editorMode = Mode.REVIEW;

		if (request.isXHR()) {
			editorPerson = personFinderService.findPerson(editorPersonId);
			ajaxResponseRenderer.addRender(listZone).addRender(editorZone);
		}
	}

	void onFailureFromUpdateForm() {
		editorMode = Mode.UPDATE;

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(editorZone);
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// DELETE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "delete"

	void onDelete(Long personId, Integer personVersion) {

		editorPersonId = personId;
		boolean successfulDelete = false;

		if (demoModeStr != null && demoModeStr.equals("true")) {
			deleteMessage = "Sorry, but Delete is not allowed in Demo mode.";
		}
		else {

			try {
				personManagerService.deletePerson(personId, personVersion);
				successfulDelete = true;
			}
			catch (Exception e) {
				// Display the cause. In a real system we would try harder to get a user-friendly message.
				deleteMessage = ExceptionUtil.getRootCauseMessage(e);
			}

		}

		if (successfulDelete) {
			editorMode = null;
			editorPersonId = null;

			if (request.isXHR()) {
				ajaxResponseRenderer.addRender(listZone).addRender(editorZone);
			}
		}
		else {
			editorMode = Mode.REVIEW;
			editorPersonId = personId;

			if (request.isXHR()) {
				editorPerson = personFinderService.findPerson(personId);
				ajaxResponseRenderer.addRender(editorZone);
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// OTHER
	// /////////////////////////////////////////////////////////////////////

	public GridDataSource getListPersons() {
		return new PersonFilteredDataSource(personFinderService, partialName);
	}

	public String getLinkCSSClass() {
		if (listPerson != null && listPerson.getId().equals(editorPersonId)) {
			return "active";
		}
		else {
			return "";
		}
	}

	public String getZoneUpdateFunction() {
		return highlightZoneUpdates ? "highlight" : "show";
	}

	public boolean isModeCreate() {
		return editorMode == Mode.CREATE;
	}

	public boolean isModeReview() {
		return editorMode == Mode.REVIEW;
	}

	public boolean isModeUpdate() {
		return editorMode == Mode.UPDATE;
	}

	public boolean isModeNull() {
		return editorMode == null;
	}

	public String getEditorPersonRegion() {
		return messages.get(Regions.class.getSimpleName() + "." + editorPerson.getRegion().name());
	}

	public String getDatePattern() {
		return "dd/MM/yyyy";
	}

	public Format getDateFormat() {
		return new SimpleDateFormat(getDatePattern());
	}
}
