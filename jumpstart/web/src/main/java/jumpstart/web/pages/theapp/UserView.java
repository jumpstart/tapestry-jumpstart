package jumpstart.web.pages.theapp;

import javax.ejb.EJB;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.User.PageStyle;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.util.EnumValueEncoder;

@ProtectedPage
public class UserView extends SimpleBasePage {

	// Activation context

	@Property
	private Long userId;

	// Screen fields

	@Property
	private User user;

	// Other pages

	// Generally useful bits and pieces

	@EJB
	private ISecurityFinderServiceLocal securityFinderService;

	@Inject
	private PageRenderLinkSource pageRenderLinkSource;

	@Inject
	private TypeCoercer typeCoercer;

	// The code

	public void set(Long userId) {
		this.userId = userId;
	}

	void onActivate(Long userId) {
		this.userId = userId;
	}

	Long onPassivate() {
		return userId;
	}

	void setupRender() throws BusinessException {
		try {
			user = securityFinderService.findUser(userId);
		}
		catch (DoesNotExistException e) {
			// Handle null user in the template
		}

	}

	void onRefresh() {
	}

	public PageStyle getBoxy() {
		return User.PageStyle.BOXY;
	}

	public PageStyle getWide() {
		return User.PageStyle.WIDE;
	}

	public EnumValueEncoder<PageStyle> getPageStyleEncoder() {
		return new EnumValueEncoder<PageStyle>(typeCoercer, User.PageStyle.class);
	}
}
