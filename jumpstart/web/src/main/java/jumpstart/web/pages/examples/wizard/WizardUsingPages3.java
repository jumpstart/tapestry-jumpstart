package jumpstart.web.pages.examples.wizard;

import jumpstart.util.ExceptionUtil;
import jumpstart.web.base.examples.wizard.WizardConversationalPage;
import jumpstart.web.pages.Index;
import jumpstart.web.state.examples.wizard.CreditRequest;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

public class WizardUsingPages3 extends WizardConversationalPage {

	// The conversation contents

	@Property
	private CreditRequest creditRequest;

	// Other pages

	@InjectPage
	private WizardUsingPages2 prevPage;

	@InjectPage
	private WizardUsingPagesSuccess nextPage;

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
		saveCreditRequestToConversation(creditRequest);

		try {
			// In the real world we would probably submit it to the business layer here
			// but we're not, so let's simulate a busy period then complete the request!

			sleep(5000);
			creditRequest.complete();
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			form.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	Object onSuccess() {
		endConversation();

		// In the real world we would now have a credit request in the database and the success page would want its
		// id instead of these two fields.

		nextPage.set(creditRequest.getAmount(), creditRequest.getApplicantName());
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

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			// Ignore
		}
	}
}
