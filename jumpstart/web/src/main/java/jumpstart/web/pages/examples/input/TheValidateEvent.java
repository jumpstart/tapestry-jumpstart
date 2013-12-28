package jumpstart.web.pages.examples.input;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;

@Import(stylesheet="css/examples/plain.css")
public class TheValidateEvent {

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	@Size(max = 10)
	private String firstName;

	@Property
	@Persist(PersistenceConstants.FLASH)
	@Min(1)
	@Max(100)
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
		
		if (form.getHasErrors()) {
			return;
		}

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
