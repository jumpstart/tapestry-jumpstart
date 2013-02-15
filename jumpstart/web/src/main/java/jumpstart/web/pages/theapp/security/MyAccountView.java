package jumpstart.web.pages.theapp.security;

import java.util.List;

import javax.ejb.EJB;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.domain.security.User;
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
public class MyAccountView extends SimpleBasePage {

	// Screen fields
	
	@Property
	private User user;
	
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
	
	void setupRender() throws BusinessException {
		Long userId = getVisit().getMyUserId();
		user = securityFinderService.findUser(userId);
		userRoles = securityFinderService.findUserRolesShallowishByUser(user.getId());
	}

	Object onEdit() {
		return MyAccountEdit.class;
	}

	Object onChangePassword() {
		return MyPasswordChange.class;
	}

	Object onViewUserRole(Long id) {
		viewPage.set(id, createLinkToThisPage());
		return viewPage;
	}

	private Link createLinkToThisPage() {
		Link thisPageLink = pageRenderLinkSource.createPageRenderLink(this.getClass());
		return thisPageLink;
	}
}
