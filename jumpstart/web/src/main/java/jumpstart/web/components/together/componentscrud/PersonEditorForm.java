package jumpstart.web.components.together.componentscrud;

import java.text.Format;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * This component will trigger the following events on its container (which in this example is the page):
 * {@link jumpstart.web.components.examples.component.PersonEditorForm.PersonEditor#CANCEL_CREATE},
 * {@link jumpstart.web.components.examples.component.PersonEditorForm.PersonEditor#SUCCESSFUL_CREATE}(Long personId),
 * {@link jumpstart.web.components.examples.component.PersonEditorForm.PersonEditor#TO_UPDATE}(Long personId),
 * {@link jumpstart.web.components.examples.component.PersonEditorForm.PersonEditor#SUCCESSFUL_UPDATE}(Long personId),
 * {@link jumpstart.web.components.examples.component.PersonEditorForm.PersonEditor#SUCCESSFUL_DELETE}(Long personId),
 * {@link jumpstart.web.components.examples.component.PersonEditorForm.PersonEditor#FAILED_DELETE}(Long personId).
 */
// @Events is applied to a component solely to document what events it may trigger. It is not checked at runtime.
@Events({ PersonEditorForm.CANCEL_CREATE, PersonEditorForm.SUCCESSFUL_CREATE, PersonEditorForm.TO_UPDATE,
		PersonEditorForm.CANCEL_UPDATE, PersonEditorForm.SUCCESSFUL_UPDATE, PersonEditorForm.SUCCESFUL_DELETE,
		PersonEditorForm.FAILED_DELETE })
@Import(stylesheet = "css/together/filtercrud.css")
public class PersonEditorForm {
	public static final String CANCEL_CREATE = "cancelCreate";
	public static final String SUCCESSFUL_CREATE = "successfulCreate";
	public static final String TO_UPDATE = "toUpdate";
	public static final String CANCEL_UPDATE = "cancelUpdate";
	public static final String SUCCESSFUL_UPDATE = "successfulUpdate";
	public static final String SUCCESFUL_DELETE = "successfulDelete";
	public static final String FAILED_DELETE = "failedDelete";

	private final String demoModeStr = System.getProperty("jumpstart.demo-mode");

	public enum Mode {
		CREATE, REVIEW, UPDATE;
	}

	// Parameters

	@Parameter(required = true)
	@Property
	private Mode mode;

	@Parameter(required = true)
	@Property
	private Long personId;

	// Screen fields

	@Property
	private Person person;

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

	@InjectComponent("firstName")
	private TextField firstNameField;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private Messages messages;

	// The code

	void setupRender() {

		if (mode == Mode.REVIEW) {
			if (personId == null) {
				person = null;
				// Handle null person in the template.
			}
			else {
				if (person == null) {
					person = personFinderService.findPerson(personId);
					// Handle null person in the template.
				}
			}
		}

	}

	// /////////////////////////////////////////////////////////////////////
	// CREATE
	// /////////////////////////////////////////////////////////////////////

	boolean onCancelCreate() {
		// Return false, which means we haven't handled the event so bubble it up.
		// This method is here solely as documentation, because without this method the event would bubble up anyway.
		return false;
	}

	void onPrepareForRenderFromCreateForm() throws Exception {

		// If fresh start, make sure there's a Person object available.

		if (createForm.isValid()) {
			person = new Person();
		}
	}

	void onPrepareForSubmitFromCreateForm() throws Exception {
		// Instantiate a Person for the form data to overlay.
		person = new Person();
	}

	void onValidateFromCreateForm() {

		if (person.getFirstName() != null && person.getFirstName().equals("Acme")) {
			createForm.recordError(firstNameField, firstNameField.getLabel() + " must not be Acme.");
		}

		if (demoModeStr != null && demoModeStr.equals("true")) {
			createForm.recordError("Sorry, but Create is not allowed in Demo mode.");
		}

		if (createForm.getHasErrors()) {
			return;
		}

		try {
			person = personManagerService.createPerson(person);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			createForm.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	boolean onSuccessFromCreateForm() {
		// We want to tell our containing page explicitly what person we've created, so we trigger new event
		// "successfulCreate" with a parameter. It will bubble up because we don't have a handler method for it.
		componentResources.triggerEvent(SUCCESSFUL_CREATE, new Object[] { person.getId() }, null);
		// We don't want "success" to bubble up, so we return true to say we've handled it.
		return true;
	}

	// /////////////////////////////////////////////////////////////////////
	// REVIEW
	// /////////////////////////////////////////////////////////////////////

	boolean onToUpdate(Long personId) {
		// Return false, which means we haven't handled the event so bubble it up.
		// This method is here solely as documentation, because without this method the event would bubble up anyway.
		return false;
	}

	boolean onDelete(Long personId, Integer personVersion) {
		this.personId = personId;

		if (demoModeStr != null && demoModeStr.equals("true")) {
			deleteMessage = "Sorry, but Delete is not allowed in Demo mode.";

			// Trigger new event "failedDelete" which will bubble up.
			componentResources.triggerEvent(FAILED_DELETE, new Object[] { personId }, null);
			// We don't want "delete" to bubble up, so we return true to say we've handled it.
			return true;
		}

		try {
			personManagerService.deletePerson(personId, personVersion);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			deleteMessage = ExceptionUtil.getRootCauseMessage(e);

			// Trigger new event "failedDelete" which will bubble up.
			componentResources.triggerEvent(FAILED_DELETE, new Object[] { personId }, null);
			// We don't want "delete" to bubble up, so we return true to say we've handled it.
			return true;
		}

		// Trigger new event "successfulDelete" which will bubble up.
		componentResources.triggerEvent(SUCCESFUL_DELETE, new Object[] { personId }, null);
		// We don't want "delete" to bubble up, so we return true to say we've handled it.
		return true;
	}

	// /////////////////////////////////////////////////////////////////////
	// UPDATE
	// /////////////////////////////////////////////////////////////////////

	boolean onCancelUpdate(Long personId) {
		// Return false, which means we haven't handled the event so bubble it up.
		// This method is here solely as documentation, because without this method the event would bubble up anyway.
		return false;
	}

	void onPrepareForRenderFromUpdateForm() {

		// If fresh start, make sure there's a Person object available.

		if (updateForm.isValid()) {
			person = personFinderService.findPerson(personId);
			// Handle null person in the template.
		}
	}

	void onPrepareForSubmitFromUpdateForm() {
		// Get objects for the form fields to overlay.
		person = personFinderService.findPerson(personId);

		if (person == null) {
			updateForm.recordError("Person has been deleted by another process.");
			// Instantiate an empty person to avoid NPE in the Form.
			person = new Person();
		}
	}

	void onValidateFromUpdateForm() {

		if (personId == 2 && !person.getFirstName().equals("Mary")) {
			updateForm.recordError(firstNameField, firstNameField.getLabel() + " for this person must be Mary.");
		}

		if (updateForm.getHasErrors()) {
			return;
		}

		try {
			personManagerService.changePerson(person);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			updateForm.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	boolean onSuccessFromUpdateForm() {
		// We want to tell our containing page explicitly what person we've updated, so we trigger new event
		// "successfulUpdate" with a parameter. It will bubble up because we don't have a handler method for it.
		componentResources.triggerEvent(SUCCESSFUL_UPDATE, new Object[] { personId }, null);
		// We don't want "success" to bubble up, so we return true to say we've handled it.
		return true;
	}

	// /////////////////////////////////////////////////////////////////////
	// GETTERS ETC.
	// /////////////////////////////////////////////////////////////////////

	public boolean isModeCreate() {
		return mode == Mode.CREATE;
	}

	public boolean isModeReview() {
		return mode == Mode.REVIEW;
	}

	public boolean isModeUpdate() {
		return mode == Mode.UPDATE;
	}

	public String getPersonRegion() {
		return messages.get(Regions.class.getSimpleName() + "." + person.getRegion().name());
	}

	public String getDatePattern() {
		return "dd/MM/yyyy";
	}

	public Format getDateFormat() {
		return new SimpleDateFormat(getDatePattern());
	}
}
