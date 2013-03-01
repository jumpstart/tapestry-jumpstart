package jumpstart.web.base.examples.wizard;

import jumpstart.web.model.Conversations;
import jumpstart.web.pages.examples.wizard.WizardUsingPagesBadFlow;
import jumpstart.web.state.examples.wizard.CreditRequest;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.SessionState;

public class WizardConversationalPage {
	public static final String WIZARD_CONVERSATION_PREFIX = "wiz";
	public static final String CREDIT_REQUEST_KEY = "CR";

	// The conversation

	@SessionState
	private Conversations conversations;

	private String conversationId = null;

	// Other pages

	@InjectPage
	private WizardUsingPagesBadFlow badFlowPage;

	// The code

	public void set(String conversationId) {
		this.conversationId = conversationId;
	}

	String onPassivate() {
		return conversationId;
	}

	Object onActivate() throws Exception {
		if (getConversationId() == null) {
			startConversation();
			return this;
		}
		return null;
	}

	protected Object onActivate(String conversationId) throws Exception {
		this.conversationId = conversationId;

		// If the conversation does not contain the model
		// then it means the Back/Reload/Refresh button has been used to reach an old conversation,
		// so redirect to the bad-flow-step

		if (restoreCreditRequestFromConversation() == null) {
			return badFlowPage;
		}

		return null;
	}

	protected void startConversation() {
		conversationId = conversations.startConversation(WIZARD_CONVERSATION_PREFIX);
	}

	protected void saveCreditRequestToConversation(CreditRequest creditRequest) {
		conversations.saveToConversation(conversationId, CREDIT_REQUEST_KEY, creditRequest);
	}

	protected CreditRequest restoreCreditRequestFromConversation() {
		return (CreditRequest) conversations.restoreFromConversation(conversationId, CREDIT_REQUEST_KEY);
	}

	protected void endConversation() {
		conversations.endConversation(conversationId);

		// If conversations SSO is now empty then remove it from the session

		if (conversations.isEmpty()) {
			conversations = null;
		}
	}

	protected String getConversationId() {
		return conversationId;
	}
}
