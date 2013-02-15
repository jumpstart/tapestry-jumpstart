package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

public class MoreValidation {

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String firstName;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Integer luckyNumber;

	// Generally useful bits and pieces

	@Component(id = "inputs")
	private Form form;

	// The code

	void onValidateFromFirstName(String value) throws ValidationException {

		// Error if doesn't contain letters only.

		if (value != null) {
			if (!value.matches("[A-Za-z]+")) {
				throw new ValidationException("First Name must contain letters only");
			}
		}

	}

	void onValidateFromLuckyNumber(Integer value) throws ValidationException {

		// Error if number 13 chosen.

		if (value != null) {
			if (value.equals(13)) {
				throw new ValidationException("Sorry, but 13 is not a lucky number.");
			}
		}

	}

	/**
	 * Cross-form validation goes in here.
	 */
	void onValidateFromInputs() {

		// Error if neither chosen.

		if (firstName == null && luckyNumber == null) {
			form.recordError("Please specify first name or lucky number.");
		}

		// Error if both chosen.

		if (firstName != null && luckyNumber != null) {
			form.recordError("Please do not specify both.");
		}
	}

}
