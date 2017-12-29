package jumpstart.business.commons.exception;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import jumpstart.business.BaseTest;
import jumpstart.business.domain.workout.Department;

import org.junit.Test;

public class DuplicateAlternateKeyExceptionTest extends BaseTest {

	@Test
	public void test_DuplicateAlternateKeyException_is_thrown() throws BusinessException {

		// First make two entities.

		workoutService.createDepartment(new Department("science"));
		workoutService.createDepartment(new Department("law"));

		// Try to make an entity with the same alternate key as the first entity - expect a
		// DuplicateAlternateKeyException.

		try {
			workoutService.createDepartment(new Department("science"));
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = interpreter.interpret(e);

			if (s instanceof DuplicateAlternateKeyException) {
				DuplicateAlternateKeyException d = (DuplicateAlternateKeyException) s;
				if (d.getInformationLevel() == DuplicateAlternateKeyException.INFORMATIONLEVEL_ENTITY_TECHMSG) {
					assertTrue(d.getMessage().startsWith("Department already exists"));
				}
				else if (d.getInformationLevel() == DuplicateAlternateKeyException.INFORMATIONLEVEL_KEY_VALUE) {
					assertTrue(d.getMessage().startsWith("Already exists."));
					assertTrue(d.getTechnicalMessageText().startsWith("Duplicate entry 'science' for key 'name'"));
				}
				else {
					fail("Should not reach here");
				}
			}
			else if (s instanceof DuplicatePrimaryOrAlternateKeyException) {
				// This occurs when the database is Derby because it cannot differentiate between primary and alternate
				// key violations.
				DuplicatePrimaryOrAlternateKeyException d = (DuplicatePrimaryOrAlternateKeyException) s;
				assertTrue(d.getMessage().startsWith("Already exists in table \"DEPARTMENT\" with same primary key or other unique key."));
			}
			else {
				fail("Wrong exception type. Found " + s);
			}
		}

	}

}
