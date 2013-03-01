package jumpstart.web.pages.examples.wizard;

import jumpstart.util.ExceptionUtil;
import jumpstart.web.model.Conversations;
import jumpstart.web.pages.Index;
import jumpstart.web.state.examples.wizard.CreditRequest;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;

public class WizardUsingFormFragments {

	public static final String WIZARD_CONVERSATION_PREFIX = "wiz";
	public static final String CREDIT_REQUEST_KEY = "CR";

	public enum Step {
		START, APPLICANT, SUBMIT, SUCCESS, BAD_FLOW
	}

	// The activation context

	private String conversationId = null;

	private Step step = null;

	// The conversation and its contents

	@SessionState
	private Conversations conversations;

	@Property
	private CreditRequest creditRequest;

	// Other pages

	@InjectPage
	private WizardUsingFormFragmentsSuccess successPage;

	@InjectPage
	private WizardUsingPagesBadFlow badFlowPage;

	@InjectPage
	private Index indexPage;

	// Generally useful bits and pieces

	@Component(id = "form")
	private Form form;

	// The code

	public void set(Step step, String conversationId) {
		this.step = step;
		this.conversationId = conversationId;
	}

	Object[] onPassivate() {
		return new Object[] { step, conversationId };
	}

	Object onActivate() {
		if (step == null) {
			startConversation();
			step = Step.START;
			return this;
		}
		return null;
	}

	Object onActivate(Step step, String conversationId) throws Exception {
		this.step = step;
		this.conversationId = conversationId;

		if (this.step == null) {
			startConversation();
			this.step = Step.START;
			return this;
		}

		// If the conversation does not contain the model
		// then it means the Back/Reload/Refresh button has been used to reach an old conversation,
		// so redirect to the bad-flow-step

		if (restoreCreditRequestFromConversation() == null) {
			return badFlowPage;
		}

		return null;
	}

	void onPrepare() {
		if (creditRequest == null) {
			// Get objects for the form fields to overlay.
			creditRequest = restoreCreditRequestFromConversation();
		}
	}

	void onValidateFromForm() {

		if (form.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		saveCreditRequestToConversation(creditRequest);

		try {
			switch (step) {
			case START:
				creditRequest.validateAmountInfo();
				break;
			case APPLICANT:
				creditRequest.validateApplicantInfo();
				break;
			case SUBMIT:
				// In the real world we would probably submit it to the business layer here
				// but we're not, so let's simulate a busy period then complete the request!

				sleep(5000);
				creditRequest.complete();
				break;
			default:
				throw new IllegalStateException("Should not get here. step = " + step);
			}
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			form.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	Object onSuccess() {
		switch (step) {
		case START:
			step = Step.APPLICANT;
			return null;
		case APPLICANT:
			step = Step.SUBMIT;
			return null;
		case SUBMIT:
			endConversation();

			// In the real world we would now have a credit request in the database and the success page would want its
			// id instead of these two fields.

			successPage.set(creditRequest.getAmount(), creditRequest.getApplicantName());
			return successPage;
		default:
			throw new IllegalStateException("Should not get here. step = " + step);
		}
	}

	void onPrev() {
		switch (step) {
		case APPLICANT:
			step = Step.START;
			break;
		case SUBMIT:
			step = Step.APPLICANT;
			break;
		default:
			throw new IllegalStateException("Should not get here. step = " + step);
		}
	}

	void onRestart() {
		step = null;
	}

	Object onQuit() {
		endConversation();
		return indexPage;
	}

	public void startConversation() {
		conversationId = conversations.startConversation(WIZARD_CONVERSATION_PREFIX);
		creditRequest = new CreditRequest();
		saveCreditRequestToConversation(creditRequest);
	}

	private void saveCreditRequestToConversation(CreditRequest creditRequest) {
		conversations.saveToConversation(conversationId, CREDIT_REQUEST_KEY, creditRequest);
	}

	private CreditRequest restoreCreditRequestFromConversation() {
		return (CreditRequest) conversations.restoreFromConversation(conversationId, CREDIT_REQUEST_KEY);
	}

	private void endConversation() {
		conversations.endConversation(conversationId);

		// If conversations SSO is now empty then remove it from the session

		if (conversations.isEmpty()) {
			conversations = null;
		}
	}

	public boolean isInEntrySteps() {
		return step == Step.START || step == Step.APPLICANT || step == Step.SUBMIT;
	}

	public boolean isInStart() {
		return step == Step.START;
	}

	public boolean isInApplicant() {
		return step == Step.APPLICANT;
	}

	public boolean isInSubmit() {
		return step == Step.SUBMIT;
	}

	public boolean isInBadFlow() {
		return step == Step.BAD_FLOW;
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			// Ignore
		}
	}

}
