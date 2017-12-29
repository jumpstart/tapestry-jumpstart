package jumpstart.business.domain.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import jumpstart.business.BaseTest;
import jumpstart.business.commons.exception.AuthenticationException;
import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.GenericBusinessException;
import jumpstart.business.commons.exception.ValueRequiredException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserPasswordTest extends BaseTest {
	static private Long johnId;
	
	private User john = null;
	private User mrDeleteMe = null;

	@BeforeClass
	static public void setUpBeforeClass() throws Exception {
		if (!servicesLocated) {
			locateServices();
			servicesLocated = true;
		}

		deleteAllUsers();
		johnId = createJohn();
	}

	static public void deleteAllUsers() throws BusinessException {
		List<User> users = securityFinderService.findUsersShallowish();
		for (User user : users) {
			securityManagerService.deleteUser(user);
		}
	}

	static public Long createJohn() throws ParseException, BusinessException {
		User john = new User();
		john.setLoginId("john");
		john.setSalutation("Mr");
		john.setFirstName("John");
		john.setLastName("Citizen");
		john.setEmailAddress("john@thecompany.com");
		DateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
		john.setExpiryDate(ymd.parse("2010-12-31"));
		john.setActive(true);

		Long johnId = securityManagerService.createUser(john, "john");
		return johnId;
	}

	@Before
	public void setUp() throws Exception {
		john = getJohn();
		mrDeleteMe = setUpMrDeleteMe();
	}

	public User getJohn() {
		User john = null;

		try {
			john = securityFinderService.findUser(new Long(johnId));
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals("john", john.getLoginId());

		// Change his password to "john". OK if already is.

		try {
			securityManagerService.changeUserPassword(john.getId(), "john");
		}
		catch (GenericBusinessException e) {
			assertEquals("User_newpassword_is_same", e.getMessageId());
		}
		catch (Exception e) {
			fail(e.getClass().toString());
		}

		return john;
	}

	public static User setUpMrDeleteMe() throws Exception {

		User newUser = new User();

		newUser.setLoginId("DeleteMe");
		newUser.setSalutation("Mr");
		newUser.setFirstName("Delete");
		newUser.setLastName("Me");
		newUser.setActive(true);

		return newUser;
	}

	@Test
	public void test_null_password_is_rejected_on_create_and_change_of_user() {

		// Try creating user with null password

		try {
			Long id = securityManagerService.createUser(mrDeleteMe, null);
			// Oops
			User u = securityFinderService.findUser(id);
			securityManagerService.deleteUser(u);
			fail("Should not reach here");
		}
		catch (BusinessException e) {
			assertTrue(e instanceof ValueRequiredException);
		}

		// Try changing existing password to null - 1st signature

		try {
			securityManagerService.changeUserPassword(john.getId(), null);
			fail("Should not reach here");
		}
		catch (BusinessException e) {
			assertTrue(e instanceof ValueRequiredException);
		}

		// Try changing existing password to null - 2nd signature

		try {
			securityManagerService.changeUserPassword(john.getId(), "john", null);
			fail("Should not reach here");
		}
		catch (BusinessException e) {
			assertTrue(e instanceof ValueRequiredException);
		}

	}

	@Test
	public void test_blank_password_is_rejected_on_create_and_change_of_user() {

		// Try creating user with blank password

		try {
			Long id = securityManagerService.createUser(mrDeleteMe, "");
			// Oops
			User u = securityFinderService.findUser(id);
			securityManagerService.deleteUser(u);
			fail("Should not reach here");
		}
		catch (BusinessException e) {
			assertTrue(e instanceof ValueRequiredException);
		}

		// Try changing existing password to blank - 1st signature

		try {
			securityManagerService.changeUserPassword(john.getId(), "");
			fail("Should not reach here");
		}
		catch (BusinessException e) {
			assertTrue(e instanceof ValueRequiredException);
		}

		// Try changing existing password to blank - 2nd signature

		try {
			securityManagerService.changeUserPassword(john.getId(), "john", "");
			fail("Should not reach here");
		}
		catch (BusinessException e) {
			assertTrue(e instanceof ValueRequiredException);
		}

	}

	@Test
	public void test_unchanged_password_is_rejected_on_changing_user_password() {

		// Try changing password to same as it is now - 1st signature

		try {
			securityManagerService.changeUserPassword(john.getId(), "john");
		}
		catch (GenericBusinessException e) {
			assertEquals("User_newpassword_is_same", e.getMessageId());
		}
		catch (Exception e) {
			fail(e.getMessage());
		}

		// Try changing password to same as it is now - 2nd signature

		try {
			securityManagerService.changeUserPassword(john.getId(), "john", "john");
		}
		catch (GenericBusinessException e) {
			assertEquals("User_newpassword_is_same", e.getMessageId());
		}
		catch (Exception e) {
			fail(e.getClass().toString());
		}

	}

	@Test
	public void test_current_password_must_be_provided_on_changing_password() {

		try {
			securityManagerService.changeUserPassword(john.getId(), "bogus", "john");
			fail("Should not reach here");
		}
		catch (AuthenticationException e) {
			assertEquals("User_password_incorrect", e.getMessageId());
		}
		catch (Exception e) {
			System.out.println(e);
			fail(e.getClass().toString());
		}

	}

}
