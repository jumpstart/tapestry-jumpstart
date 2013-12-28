package jumpstart.business.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * A JSR-303 Bean Validation constraint that requires a string to be null or contain letters only.
 */
@Documented
@Constraint(validatedBy = LettersValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface Letters {

	String message() default "{jumpstart.business.validation.constraints.Letters.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
