package jumpstart.web.pages.theapp.security;

import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.Role;
import jumpstart.business.domain.security.UserRole;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

@ProtectedPage
public class RoleView extends SimpleBasePage {

	// Activation context

	private Long roleId;
	
	// Screen fields

	@Property
	private Role role;

	@Property
	private List<UserRole> userRoles;

	@Property
	private UserRole userRole;

	// Other pages

	@InjectPage
	private UserRoleView viewPage;

	// Generally useful bits and pieces

	@Inject
	private PageRenderLinkSource pageRenderLinkSource;
	
	@EJB
	private ISecurityFinderServiceLocal securityFinderService;

	// The code
	
	public void set(Long roleId) {
		this.roleId = roleId;
	}

	Long onPassivate() {
		return roleId;
	}

	void onActivate(Long roleId) throws DoesNotExistException {
		this.roleId = roleId;
	}

	void setupRender() throws BusinessException {
		try {
			role = securityFinderService.findRole(roleId);
		}
		catch (DoesNotExistException e) {
			// Handle null role in the template
		}

		userRoles = securityFinderService.findUserRolesShallowishByRole(roleId);
	}

	void onRefresh() {
	}

	Object onCancel() {
		return RoleSearch.class;
	}

	Object onViewUserRole(Long id) {
		viewPage.set(id, createLinkToThisPage());
		return viewPage;
	}

	private Link createLinkToThisPage() {
		Link thisPageLink = pageRenderLinkSource.createPageRenderLinkWithContext(this.getClass(), onPassivate());
		return thisPageLink;
	}
}
