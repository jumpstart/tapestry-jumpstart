package jumpstart.business.commons.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import jumpstart.business.BaseTest;
import jumpstart.business.domain.workout.Department;

import org.junit.Test;

public class DoesNotExistExceptionTest extends BaseTest {
	private NumberFormat formatter = new DecimalFormat("###,###");

	@Test
	public void test_DoesNotExistException_is_thrown_on_find_by_wrong_String_id() {

		// Try to find an entity that doesn't exist - expect a DoesNotExistException.

		try {
			workoutService.findStringThing("garbage");
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = interpreter.interpret(e);
			assertEquals(s.getMessage(), DoesNotExistException.class, s.getClass());
			assertEquals("StringThing \"garbage\" does not exist.", e.getMessage());
		}

	}

	@Test
	public void test_DoesNotExistException_is_thrown_on_find_by_wrong_Long_id() {

		// Try to find an entity that doesn't exist - expect a DoesNotExistException.

		try {
			workoutService.findBuilding(new Long(346746437));
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = interpreter.interpret(e);
			assertEquals(s.getMessage(), DoesNotExistException.class, s.getClass());
			assertEquals("Building \"" + formatter.format(346746437) + "\" does not exist.", s.getMessage());
		}

	}

	// @Test
	// public void test_DoesNotExistException_is_thrown_on_find_shallowish_by_wrong_String_id() {
	//
	// try {
	// dBTestSvc.findStringThingShallowish("garbage");
	// fail("Should not reach here");
	// }
	// catch (Exception e) {
	// BusinessException s = interpreter.interpretAsBusinessException(e);
	// assertEquals(s.getMessage(), DoesNotExistException.class, s.getClass());
	// assertEquals("Teacher \"garbage\" does not exist.", e.getMessage());
	// }
	//
	// }

	@Test
	public void test_DoesNotExistException_is_thrown_on_find_shallowish_by_wrong_Long_id() {

		// Try to find an entity that doesn't exist, using a "shallowish" method, ie. one that join fetches its
		// immediate ManyToOne references - expect a DoesNotExistException.

		try {
			workoutService.findTeacherShallowish(new Long(346746437));
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = interpreter.interpret(e);
			assertEquals(s.getMessage(), DoesNotExistException.class, s.getClass());
			assertEquals("Teacher \"" + formatter.format(346746437) + "\" does not exist.", s.getMessage());
		}

	}

	@Test
	public void test_DoesNotExistException_is_thrown_on_change_of_deleted_entity() throws BusinessException {

		// First make a department.

		Department department0 = new Department("Deleteme");
		department0 = workoutService.createDepartment(department0);

		// Get the department.

		Department department1 = workoutService.findDepartment(department0.getId());
		Long id1 = department1.getId();

		// Delete the original department.

		workoutService.deleteDepartment(department0);

		// Try to modify the department - expect a DoesNotExistException.

		try {
			department1.setName("New Name");
			workoutService.changeDepartment(department1);
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = interpreter.interpret(e);
			assertEquals(s.getMessage(), DoesNotExistException.class, s.getClass());
			assertEquals("Department \"" + formatter.format(id1) + "\" does not exist.", s.getMessage());
		}

	}

	@Test
	public void test_DoesNotExistException_is_thrown_on_delete_of_deleted_entity() throws BusinessException {

		// First make a department.

		Department department0 = new Department("Deleteme");
		department0 = workoutService.createDepartment(department0);

		// Get the department.

		Department department1 = workoutService.findDepartment(department0.getId());
		Long id1 = department1.getId();

		// Delete the original department.

		workoutService.deleteDepartment(department0);

		// Try to delete the department - expect a DoesNotExistException.

		try {
			workoutService.deleteDepartment(department1);
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = interpreter.interpret(e);
			assertEquals(s.getMessage(), DoesNotExistException.class, s.getClass());
			assertEquals("Department \"" + formatter.format(id1) + "\" does not exist.", s.getMessage());
		}

	}
	
}
