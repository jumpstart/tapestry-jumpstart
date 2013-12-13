package jumpstart.web.pages.examples.wizard;

import java.util.Collection;

import jumpstart.web.base.examples.wizard.WizardConversationalPage;
import jumpstart.web.models.Conversation;
import jumpstart.web.models.Conversations;
import jumpstart.web.pages.examples.wizard.WizardUsingFormFragments.Step;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

@Import(stylesheet = "css/examples/conversationslist.css")
public class ConversationsList {

	// Screen fields

	@Property
	private Collection<Conversation> allConversations;

	@Property
	private Conversation conversation;

	// Other pages

	@InjectPage
	private WizardUsingFormFragments creditRequestsWizard;

	// Generally useful bits and pieces

	@SessionState
	private Conversations conversations;

	// The code
	
	void setupRender() {
		allConversations = conversations.getAll();
	}

	Object onGoTo(String conversationId) throws Exception {
		Conversation conversation = conversations.get(conversationId);

		if (conversation != null) {
			// We know of 1 type of conversation only - it belongs to the credit requests wizard
			if (conversation.getObject(WizardConversationalPage.CREDIT_REQUEST_KEY) != null) {
				creditRequestsWizard.set(Step.START, conversationId);
				return creditRequestsWizard;
			}
		}

		return null;
	}

	public Object getObject() throws Exception {

		if (conversation != null) {
			// We know of 1 type of conversation only - it belongs to the credit requests wizard
			Object object = conversation.getObject(WizardConversationalPage.CREDIT_REQUEST_KEY);
			return object;
		}

		return null;
	}
}
