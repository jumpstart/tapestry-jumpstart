package jumpstart.business.domain.security;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.commons.exception.GenericBusinessException;
import jumpstart.business.domain.base.BaseService;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceRemote;

//@SecurityDomain("jumpstart")
@Stateless
@Local(ISecurityManagerServiceLocal.class)
@Remote(ISecurityManagerServiceRemote.class)
public class SecurityManagerService extends BaseService implements ISecurityManagerServiceLocal,
		ISecurityManagerServiceRemote {

	private final String demoModeStr = System.getProperty("jumpstart.demo-mode");

	/*******************************************************************************************************************
	 * BUSINESS METHODS
	 ******************************************************************************************************************/

	/**
	 * This method provides a way for users to change their own userPassword.
	 */
	public void changeUserPassword(Long id, String currentPassword, String newPassword) throws BusinessException {
		assertNotADemo();
		User user = find(User.class, id);
		user.changePassword(currentPassword, newPassword);
		flushToWorkAroundTOMEE_172();
	}

	/**
	 * This method provides a way for security officers to "reset" the userPassword.
	 */
	public void changeUserPassword(Long id, String newPassword) throws BusinessException {
		assertNotADemo();
		User user = find(User.class, id);
		user.setPassword(newPassword);
	}

	/*******************************************************************************************************************
	 * PERSISTENCE METHODS
	 ******************************************************************************************************************/

	// User
	public Long createUser(User user, String password) throws BusinessException {
		assertNotADemo();
		user.changePassword(null, password);
		persist(user);
		flushToWorkAroundTOMEE_172();
		// DO NOT return the whole User, because its UserPassword is present in it
		return user.getId();
	}

	public void changeUser(User user) throws BusinessException {
		assertNotADemo();
		User u = merge(user);
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!u.getId().equals(user.getId())) {
			throw new DoesNotExistException(User.class, user.getId());
		}

		flushToWorkAroundTOMEE_172();
	}

	public void deleteUser(User user) throws BusinessException {
		assertNotADemo();
		User u = merge(user);
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!u.getId().equals(user.getId())) {
			throw new DoesNotExistException(User.class, user.getId());
		}

		remove(u);
		flushToWorkAroundTOMEE_172();
	}

	// Role

	public Role createRole(Role role) throws BusinessException {
		assertNotADemo();
		persist(role);
		flushToWorkAroundTOMEE_172();
		return role;
	}

	public void changeRole(Role role) throws BusinessException {
		assertNotADemo();
		Role r = merge(role);
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!r.getId().equals(role.getId())) {
			throw new DoesNotExistException(Role.class, role.getId());
		}

		flushToWorkAroundTOMEE_172();
	}

	public void deleteRole(Role role) throws BusinessException {
		assertNotADemo();
		Role r = merge(role);
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!r.getId().equals(role.getId())) {
			throw new DoesNotExistException(Role.class, role.getId());
		}

		remove(r);
		flushToWorkAroundTOMEE_172();
	}

	// UserRole

	public UserRole addUserRole(Long userId, Long roleId, UserRole userRole) throws BusinessException {
		assertNotADemo();
		if (userRole.user != null) {
			throw new IllegalArgumentException();
		}
		if (userRole.role != null) {
			throw new IllegalArgumentException();
		}

		User user = find(User.class, userId);
		Role role = find(Role.class, roleId);

		return addUserRole(user, role, userRole);
	}

	public UserRole addUserRole(User user, Role role, UserRole userRole) throws BusinessException {
		assertNotADemo();
		if (userRole.user != null) {
			throw new IllegalArgumentException();
		}
		if (userRole.role != null) {
			throw new IllegalArgumentException();
		}

		User u = merge(user);
		Role r = merge(role);
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!u.getId().equals(user.getId())) {
			throw new DoesNotExistException(User.class, user.getId());
		}
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!r.getId().equals(role.getId())) {
			throw new DoesNotExistException(Role.class, role.getId());
		}

		u.addUserRole(userRole);
		r.addUserRole(userRole);

		persist(userRole);
		flushToWorkAroundTOMEE_172();
		return userRole;
	}

	public void changeUserRole(UserRole userRole) throws BusinessException {
		assertNotADemo();
		UserRole ur = merge(userRole);
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!ur.getId().equals(userRole.getId())) {
			throw new DoesNotExistException(UserRole.class, userRole.getId());
		}

		flushToWorkAroundTOMEE_172();
	}

	public void removeUserRole(UserRole userRole) throws BusinessException {
		assertNotADemo();
		UserRole ur = merge(userRole);
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!ur.getId().equals(userRole.getId())) {
			throw new DoesNotExistException(UserRole.class, userRole.getId());
		}

		remove(ur);
		flushToWorkAroundTOMEE_172();
	}

	/*******************************************************************************************************************
	 * PRIVATE METHODS
	 ******************************************************************************************************************/

	// These methods are solely to protect the JumpStart database in the demo site

	private void assertNotADemo() throws GenericBusinessException {
		if (isADemo()) {
			throw new GenericBusinessException("Demo_prohibited_function");
		}
	}

	private boolean isADemo() {
		boolean readonly = false;
		if (demoModeStr != null) {
			if (demoModeStr.equalsIgnoreCase("true")) {
				readonly = true;
			}
			else if (demoModeStr.equalsIgnoreCase("false")) {
				readonly = false;
			}
			else {
				throw new IllegalStateException(
						"System property jumpstart.demo-mode has been set to \""
								+ demoModeStr
								+ "\".  Please set it to \"true\" or \"false\".  If not specified at all then it will default to \"false\".");
			}
		}
		return readonly;
	}
}
