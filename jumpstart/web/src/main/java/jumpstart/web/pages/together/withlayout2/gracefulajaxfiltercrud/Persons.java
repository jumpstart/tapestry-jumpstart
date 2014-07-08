package jumpstart.web.pages.together.withlayout2.gracefulajaxfiltercrud;

import java.text.Format;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.models.together.PersonFilteredDataSource;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
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
		CREATE, REVIEW, UPDATE, CONFIRM_DELETE;
	}

	// The activation context

	@Property
	private Mode editorMode;

	@Property
	private Long editorPersonId;

	@ActivationRequestParameter
	private Mode arpEditorMode;

	@ActivationRequestParameter
	private Long arpEditorPersonId;

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
	private Person editorPerson;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String deleteMessage;

	// Work fields

	// This carries version through the redirect that follows a server-side validation failure.
	@Persist(PersistenceConstants.FLASH)
	private Integer versionFlash;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@EJB
	private IPersonManagerServiceLocal personManagerService;

	@InjectComponent
	// FIX!
	// private CustomForm createForm;
	private Form createForm;

	@InjectComponent
	// FIX!
	// private CustomForm updateForm;
	private Form updateForm;

	@InjectComponent
	private Form confirmDeleteForm;

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

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(EventContext ec) {

		if (request.isXHR()) {
			editorMode = arpEditorMode;
			editorPersonId = arpEditorPersonId;
		}
		else {
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

	}

	// onPassivate() is called by Tapestry to get the activation context to put in the URL.

	Object[] onPassivate() {

		if (request.isXHR()) {
			arpEditorMode = editorMode;
			arpEditorPersonId = editorPersonId;

			return null;
		}
		else {

			if (editorMode == null) {
				return null;
			}
			else if (editorMode == Mode.CREATE) {
				return new Object[] { editorMode };
			}
			else if (editorMode == Mode.REVIEW || editorMode == Mode.UPDATE || editorMode == Mode.CONFIRM_DELETE) {
				return new Object[] { editorMode, editorPersonId };
			}
			else {
				throw new IllegalStateException(editorMode.toString());
			}
		}

	}

	// setupRender() is called by Tapestry right before it starts rendering the page.

	void setupRender() {
		if (editorMode == Mode.REVIEW) {
			if (editorPersonId == null) {
				editorPerson = null;
				// Handle null editorPerson in the template.
			}
			else {
				editorPerson = personFinderService.findPerson(editorPersonId);
				// Handle null editorPerson in the template.
			}
		}
	}

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

	// Component "updateForm" bubbles up the PREPARE_FOR_RENDER event during form render

	void onPrepareForRenderFromUpdateForm(Long personId) {
		editorMode = Mode.UPDATE;
		editorPersonId = personId;

		if (request.isXHR()) {

			// If the form is valid then we're not redisplaying due to error, so get the editorPerson.

			if (updateForm.isValid()) {
				editorPerson = personFinderService.findPerson(personId);
				// Handle null editorPerson in the template.
			}

		}
		else {
			editorPerson = personFinderService.findPerson(personId);
			// Handle null editorPerson in the template.

			// If the form has errors then we're redisplaying after a redirect.
			// Form will restore your input values but it's up to us to restore Hidden values.

			if (updateForm.getHasErrors()) {
				if (editorPerson != null) {
					editorPerson.setVersion(versionFlash);
				}
			}

		}
	}

	// Component "updateForm" bubbles up the PREPARE_FOR_SUBMIT event during form submission

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
		else {
			versionFlash = editorPerson.getVersion();
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// DELETE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "delete"

	void onDelete(Long personId, Integer personVersion) {

		// If request is AJAX then the user has pressed Delete..., was presented with a Confirm dialog, and OK'd it.

		if (request.isXHR()) {
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

		// Else, (JavaScript disabled) user has pressed Delete..., but not yet confirmed so go to a confirmation screen.

		else {
			editorMode = Mode.CONFIRM_DELETE;
			editorPersonId = personId;
		}

	}

	// /////////////////////////////////////////////////////////////////////
	// CONFIRM DELETE - used only when JavaScript is disabled.
	// /////////////////////////////////////////////////////////////////////

	// Handle event "cancelConfirmDelete"

	void onCancelConfirmDelete(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
	}

	// Component "confirmDeleteForm" bubbles up the PREPARE_FOR_RENDER event during form render.

	void onPrepareForRenderFromConfirmDeleteForm() {
		editorMode = Mode.CONFIRM_DELETE;

		editorPerson = personFinderService.findPerson(editorPersonId);
		// Handle null editorPerson in the template.

		// If the form has errors then we're redisplaying after a redirect.
		// Form will restore your input values but it's up to us to restore Hidden values.

		if (confirmDeleteForm.getHasErrors()) {
			if (editorPerson != null) {
				editorPerson.setVersion(versionFlash);
			}
		}
	}

	// Component "confirmDeleteForm" bubbles up the PREPARE_FOR_SUBMIT event during form submission.

	void onPrepareForSubmitFromConfirmDeleteForm() {
		// Get objects for the form fields to overlay.
		editorPerson = personFinderService.findPerson(editorPersonId);

		if (editorPerson == null) {
			editorPerson = new Person();
			confirmDeleteForm.recordError("Person has already been deleted by another process.");
		}
	}

	// Component "confirmDeleteForm" bubbles up the VALIDATE event when it is submitted

	void onValidateFromConfirmDeleteForm() {

		if (confirmDeleteForm.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		if (demoModeStr != null && demoModeStr.equals("true")) {
			confirmDeleteForm.recordError("Sorry, but Delete is not allowed in Demo mode.");
		}
		else {

			try {
				personManagerService.deletePerson(editorPersonId, editorPerson.getVersion());
			}
			catch (Exception e) {
				// Display the cause. In a real system we would try harder to get a user-friendly message.
				confirmDeleteForm.recordError(ExceptionUtil.getRootCauseMessage(e));
			}

		}

	}

	// Component "confirmDeleteForm" bubbles up SUCCESS or FAILURE when it is submitted, depending on whether
	// VALIDATE records an error

	void onSuccessFromConfirmDeleteForm() {
		editorMode = null;
		editorPersonId = null;
	}

	void onFailureFromConfirmDeleteForm() {
		editorMode = Mode.CONFIRM_DELETE;
		versionFlash = editorPerson.getVersion();
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

	public boolean isAjax() {
		return request.isXHR();
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

	public boolean isModeConfirmDelete() {
		return editorMode == Mode.CONFIRM_DELETE;
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
