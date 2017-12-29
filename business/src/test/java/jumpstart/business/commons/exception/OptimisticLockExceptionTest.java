package jumpstart.business.commons.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import jumpstart.business.BaseTest;
import jumpstart.business.domain.workout.Teacher;

import org.junit.Test;

public class OptimisticLockExceptionTest extends BaseTest {

	@Test
	public void test_OptimisticLockException_is_thrown() throws BusinessException {

		// First make a teacher, then get a 2nd instance of it.

		Teacher teacher1 = new Teacher("T1", null, null);
		teacher1 = workoutService.createTeacher(teacher1, false);
		Teacher teacher2 = workoutService.findTeacher(teacher1.getId());

		// Change the first instance - expect no exception.

		teacher1.setName("T1x");
		workoutService.changeTeacher(teacher1);

		// Try changing the second instance - expect OptimisticLockException.

		teacher2.setName("T1y");
		try {
			workoutService.changeTeacher(teacher1);
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = interpreter.interpret(e);
			assertEquals(s.getMessage(), OptimisticLockException.class, e.getClass());
			assertEquals("Not saved or deleted because it has been changed or deleted by others since displayed.", s
					.getMessage());
		}

	}

}
