package jumpstart.web.pages.examples.wizard;

import jumpstart.util.ExceptionUtil;
import jumpstart.web.base.examples.wizard.WizardConversationalPage;
import jumpstart.web.pages.Index;
import jumpstart.web.state.examples.wizard.CreditRequest;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

public class WizardUsingPages2 extends WizardConversationalPage {

	// The conversation contents

	@Property
	private CreditRequest creditRequest;

	// Other pages

	@InjectPage
	private WizardUsingPages1 prevPage;

	@InjectPage
	private WizardUsingPages3 nextPage;

	@InjectPage
	private Index indexPage;

	// Generally useful bits and pieces

	@InjectComponent
	private Form form;

	// The code

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
			creditRequest.validateApplicantInfo();
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

	Object onPrev() {
		prevPage.set(getConversationId());
		return prevPage;
	}

	Object onQuit() {
		endConversation();
		return indexPage;
	}

}
