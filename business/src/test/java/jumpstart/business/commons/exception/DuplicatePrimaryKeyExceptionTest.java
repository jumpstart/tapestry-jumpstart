package jumpstart.business.commons.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import jumpstart.business.BaseTest;
import jumpstart.business.domain.workout.StringThing;

import org.junit.Test;

public class DuplicatePrimaryKeyExceptionTest extends BaseTest {

	@Test
	public void test_DuplicatePrimaryKeyException_is_thrown() throws BusinessException {

		// First make two entities.

		workoutService.createStringThing(new StringThing("ABC", "Alpha"));
		workoutService.createStringThing(new StringThing("DEF", "Delta"));

		// Try to make an entity with the same id as the first entity - expect a DuplicatePrimaryKeyException.

		try {
			workoutService.createStringThing(new StringThing("ABC", "Apricot"));
			fail("Should not reach here because 3rd customer has same id as 1st.");
		}
		catch (Exception e) {
			BusinessException s = interpreter.interpret(e);

			if (s instanceof DuplicatePrimaryKeyException) {
				DuplicatePrimaryKeyException d = (DuplicatePrimaryKeyException) s;
				if (d.getInformationLevel() == DuplicatePrimaryKeyException.INFORMATIONLEVEL_NONE) {
					assertEquals("Already exists.", d.getMessage());
				}
				else if (d.getInformationLevel() == DuplicatePrimaryKeyException.INFORMATIONLEVEL_ID) {
					assertTrue(d.getMessage().startsWith("\"ABC\" already exists."));
				}
				else {
					fail("Should not reach here");
				}
			}
			else if (s instanceof DuplicatePrimaryOrAlternateKeyException) {
				// This occurs when the database is Derby because it cannot differentiate between primary and alternate
				// key violations.
				DuplicatePrimaryOrAlternateKeyException d = (DuplicatePrimaryOrAlternateKeyException) s;
				assertTrue(d.getMessage().startsWith("Already exists in table \"STRINGTHING\" with same primary key or other unique key."));
			}
			else {
				fail("Wrong exception type. Found " + s);
			}
		}
	}

}
