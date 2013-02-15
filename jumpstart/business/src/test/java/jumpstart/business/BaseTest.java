package jumpstart.business;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.interpreter.BusinessServiceExceptionInterpreter;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceRemote;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceRemote;
import jumpstart.business.domain.workout.iface.IWorkoutServiceRemote;
import jumpstart.client.BusinessServicesLocator;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {

	static protected boolean servicesLocated = false;

	static protected IWorkoutServiceRemote workoutService;
	static protected ISecurityFinderServiceRemote securityFinderService;
	static protected ISecurityManagerServiceRemote securityManagerService;

	static protected final Logger logger = LoggerFactory.getLogger(BusinessServiceExceptionInterpreter.class);
	static protected BusinessServiceExceptionInterpreter interpreter = new BusinessServiceExceptionInterpreter();

	static public void locateServices() {
		BusinessServicesLocator locator = new BusinessServicesLocator(logger);
		workoutService = (IWorkoutServiceRemote) locator.getService(IWorkoutServiceRemote.class);
		securityFinderService = (ISecurityFinderServiceRemote) locator.getService(ISecurityFinderServiceRemote.class);
		securityManagerService = (ISecurityManagerServiceRemote) locator.getService(ISecurityManagerServiceRemote.class);
	}

	@Before
	public void setUp() throws Exception {

		if (!servicesLocated) {
			locateServices();
			servicesLocated = true;
		}

		deleteAllWorkoutEntities();
	}

	public void deleteAllWorkoutEntities() throws BusinessException {
		workoutService.deleteAllStudents();
		workoutService.deleteAllTeachers();
		workoutService.deleteAllDepartments();
		workoutService.removeAllRooms();
		workoutService.deleteAllBuildings();
		workoutService.deleteAllStringThings();
	}

}
