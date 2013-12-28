package jumpstart.business.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LettersValidator implements ConstraintValidator<Letters, String> {

	public void initialize(Letters letters) {
	}

	public boolean isValid(String value, ConstraintValidatorContext context) {
		return (value == null || value.matches("[A-Za-z]+"));
	}
}
