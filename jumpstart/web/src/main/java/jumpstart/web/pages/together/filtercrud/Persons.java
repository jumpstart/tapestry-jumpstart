package jumpstart.web.pages.together.filtercrud;

import java.text.Format;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.components.CustomForm;
import jumpstart.web.model.together.PersonFilteredDataSource;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Persons {

	private final String demoModeStr = System.getProperty("jumpstart.demo-mode");

	private enum Mode {
		CREATE, REVIEW, UPDATE;
	}

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
	private GridDataSource listPersons;

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

	@Component
	private CustomForm createForm;

	@Component
	private CustomForm updateForm;

	@Inject
	private Messages messages;

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
		listPersons = new PersonFilteredDataSource(personFinderService, partialName);

		if (editorMode == Mode.REVIEW) {
			if (editorPersonId == null) {
				editorPerson = null;
			}
			else {
				if (editorPerson == null) {
					editorPerson = personFinderService.findPerson(editorPersonId);
					// Handle null editorPerson in the template.
				}
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// CREATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toCreate"

	void onToCreate() {
		editorMode = Mode.CREATE;
		editorPersonId = null;
	}

	// Handle event "cancelCreate"

	void onCancelCreate() {
		editorMode = null;
		editorPersonId = null;
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
	}

	void onFailureFromCreateForm() {
		editorMode = Mode.CREATE;
		editorPersonId = null;
	}

	// /////////////////////////////////////////////////////////////////////
	// REVIEW
	// /////////////////////////////////////////////////////////////////////

	// Handle event "selected"

	void onSelected(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
	}

	// /////////////////////////////////////////////////////////////////////
	// UPDATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toUpdate"

	void onToUpdate(Long personId) {
		editorMode = Mode.UPDATE;
		editorPersonId = personId;
	}

	// Handle event "cancelUpdate"

	void onCancelUpdate(Long personId) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
	}

	// Component "updateForm" bubbles up the PREPARE_FOR_RENDER event before it is rendered

	void onPrepareForRenderFromUpdateForm() {
		editorMode = Mode.UPDATE;

		editorPerson = personFinderService.findPerson(editorPersonId);
		// Handle null editorPerson in the template.

		// If the form has errors then we're redisplaying after a redirect.
		// Form will restore your input values but it's up to us to restore Hidden values.

		if (updateForm.getHasErrors()) {
			if (editorPerson != null) {
				editorPerson.setVersion(versionFlash);
			}
		}
	}

	// Component "updateForm" bubbles up the PREPARE_FOR_SUBMIT event during form submission

	void onPrepareForSubmitFromUpdateForm() {
		editorMode = Mode.UPDATE;

		// Get objects for the form fields to overlay.
		editorPerson = personFinderService.findPerson(editorPersonId);

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
		editorPersonId = editorPerson.getId();
		setupRender();
	}

	void onFailureFromUpdateForm() {
		editorMode = Mode.UPDATE;
		versionFlash = editorPerson.getVersion();
	}

	// /////////////////////////////////////////////////////////////////////
	// DELETE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "delete"

	void onDelete(Long personId, Integer personVersion) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;
		editorPerson = personFinderService.findPerson(personId);
		// Handle null editorPerson in the template.

		if (demoModeStr != null && demoModeStr.equals("true")) {
			deleteMessage = "Sorry, but Delete is not allowed in Demo mode.";
			return;
		}

		try {
			personManagerService.deletePerson(personId, personVersion);
			editorMode = null;
			editorPersonId = null;
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			deleteMessage = ExceptionUtil.getRootCauseMessage(e);
		}
	}

	// /////////////////////////////////////////////////////////////////////
	// OTHER
	// /////////////////////////////////////////////////////////////////////

	// Getters

	public String getLinkCSSClass() {
		if (listPerson != null && listPerson.getId().equals(editorPersonId)) {
			return "active";
		}
		else {
			return "";
		}
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

	public String getEditorPersonRegion() {
		// Follow the same naming convention that the Select component uses
		return messages.get(Regions.class.getSimpleName() + "." + editorPerson.getRegion().name());
	}

	public String getDatePattern() {
		return "dd/MM/yyyy";
	}

	public Format getDateFormat() {
		return new SimpleDateFormat(getDatePattern());
	}
}
