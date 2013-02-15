package jumpstart.web.pages.examples.wizard;

import jumpstart.util.ExceptionUtil;
import jumpstart.web.base.examples.wizard.WizardConversationalPage;
import jumpstart.web.pages.Index;
import jumpstart.web.state.examples.wizard.CreditRequest;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

public class WizardUsingPages1 extends WizardConversationalPage {

	// The conversation contents

	@Property
	private CreditRequest creditRequest;

	// Other pages

	@InjectPage
	private WizardUsingPages2 nextPage;

	@InjectPage
	private Index indexPage;

	// Generally useful bits and pieces

	@Component(id = "form")
	private Form form;

	// The code

	@Override
	public void startConversation() {
		super.startConversation();
		creditRequest = new CreditRequest();
		saveCreditRequestToConversation(creditRequest);
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
			creditRequest.validateAmountInfo();
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			form.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	Object onSuccess() {
		nextPage.set(getConversationId());
		return nextPage;
	}

	Object onQuit() {
		endConversation();
		return indexPage;
	}

}
