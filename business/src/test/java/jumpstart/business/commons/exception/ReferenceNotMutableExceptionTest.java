package jumpstart.business.commons.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.NumberFormat;

import jumpstart.business.BaseTest;
import jumpstart.business.domain.workout.Building;
import jumpstart.business.domain.workout.Room;

import org.junit.Test;

public class ReferenceNotMutableExceptionTest extends BaseTest {

	@Test
	public void test_ReferenceNotMutableException_is_thrown_on_changing_part_to_reference_different_whole()
			throws BusinessException {

		// First make a building with a room.

		Building bigBuilding = new Building("Big");
		bigBuilding = workoutService.createBuilding(bigBuilding);
		Room room1 = new Room("West1", bigBuilding);
		room1 = workoutService.addRoom(room1, false);

		// Now make 2nd building.

		Building littleBuilding = new Building("Little");
		littleBuilding = workoutService.createBuilding(littleBuilding);

		// Try to switch the room of the first building to the second building - expect a ReferenceNotMutableException
		// because the relationship between Building and Room is composition.

		room1.setBuildingForReferenceNotResettableExceptionTest(littleBuilding);
		try {
			workoutService.changeRoom(room1);
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = interpreter.interpret(e);
			assertEquals(s.getMessage(), ReferenceNotMutableException.class, s.getClass());
			assertEquals("Do not reset Building of Room.  Id was \""
					+ NumberFormat.getIntegerInstance().format(bigBuilding.getId()) + "\" and would have become \""
					+ NumberFormat.getIntegerInstance().format(littleBuilding.getId()) + "\".", s.getMessage());
		}

	}

}
