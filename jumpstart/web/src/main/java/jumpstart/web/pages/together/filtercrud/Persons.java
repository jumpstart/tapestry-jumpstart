package jumpstart.web.pages.together.filtercrud;

import java.text.Format;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.validation.constraints.Size;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.models.together.PersonFilteredDataSource;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

@Import(stylesheet = "css/together/filtercrud.css")
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
	@Size(max = 10)
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

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@EJB
	private IPersonManagerServiceLocal personManagerService;

	@Component
	private Form createForm;

	@Component
	private Form updateForm;

	@Component(id = "firstName")
	private TextField firstNameField;

	@Inject
	private Messages messages;

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
		listPersons = new PersonFilteredDataSource(personFinderService, partialName);

		if (editorMode == Mode.REVIEW) {
			if (editorPersonId == null) {
				editorPerson = null;
				// Handle null editorPerson in the template.
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

	// Component "createForm" bubbles up the PREPARE_FOR_RENDER event before it is rendered

	void onPrepareForRenderFromCreateForm() throws Exception {

		// If fresh start, make sure there's a Person object available.

		if (createForm.isValid()) {
			editorMode = Mode.CREATE;
			editorPerson = new Person();
		}
	}

	// Component "createForm" bubbles up the PREPARE_FOR_SUBMIT event when it is submitted

	void onPrepareForSubmitFromCreateForm() throws Exception {
		editorMode = Mode.CREATE;
		// Instantiate a Person for the form data to overlay.
		editorPerson = new Person();
	}

	// Component "createForm" bubbles up the VALIDATE event when it is submitted

	void onValidateFromCreateForm() {

		if (editorPerson.getFirstName() != null && editorPerson.getFirstName().equals("Acme")) {
			createForm.recordError(firstNameField, firstNameField.getLabel() + " must not be Acme.");
		}

		if (demoModeStr != null && demoModeStr.equals("true")) {
			createForm.recordError("Sorry, but Create is not allowed in Demo mode.");
		}

		if (createForm.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
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

		// If fresh start, make sure there's a Person object available.

		if (updateForm.isValid()) {
			editorMode = Mode.UPDATE;
			editorPerson = personFinderService.findPerson(editorPersonId);
			// Handle null editorPerson in the template.
		}
	}

	// Component "updateForm" bubbles up the PREPARE_FOR_SUBMIT event during form submission

	void onPrepareForSubmitFromUpdateForm() {
		editorMode = Mode.UPDATE;

		// Get objects for the form fields to overlay.
		editorPerson = personFinderService.findPerson(editorPersonId);

		if (editorPerson == null) {
			updateForm.recordError("Person has been deleted by another process.");
			// Instantiate an empty person to avoid NPE in the Form.
			editorPerson = new Person();
		}
	}

	// Component "updateForm" bubbles up the VALIDATE event when it is submitted

	void onValidateFromUpdateForm() {

		if (editorPersonId == 2 && !editorPerson.getFirstName().equals("Mary")) {
			updateForm.recordError(firstNameField, firstNameField.getLabel() + " for this person must be Mary.");
		}

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
	}

	// /////////////////////////////////////////////////////////////////////
	// DELETE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "delete"

	void onDelete(Long personId, Integer personVersion) {
		editorMode = Mode.REVIEW;
		editorPersonId = personId;

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
		return messages.get(Regions.class.getSimpleName() + "." + editorPerson.getRegion().name());
	}

	public String getDatePattern() {
		return "dd/MM/yyyy";
	}

	public Format getDateFormat() {
		return new SimpleDateFormat(getDatePattern());
	}
}
