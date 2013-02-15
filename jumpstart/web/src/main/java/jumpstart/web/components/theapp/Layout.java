package jumpstart.web.components.theapp;

import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.pages.theapp.Login;
import jumpstart.web.state.theapp.Visit;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;
import org.slf4j.Logger;

@ProtectedPage
public class Layout {

	@Parameter
	private String tab = "";

	@Parameter
	private String subTab = "";

	@Parameter
	private String title = "";

	@Inject
	@Path("context:css/theapp/global.css")
	private Asset stylesheet1;

	@Inject
	@Path("context:css/theapp/global_wide.css")
	private Asset stylesheet2;

	@Inject
	private Logger logger;

	@Inject
	private Request request;

	@SessionState
	private Visit visit;

	Object onLogout() {
		logger.info(visit.getMyLoginId() + " is logging out.");
		Session session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return Login.class;
	}

	public String getTitle() {
		return title;
	}

	public Asset getStylesheet() {
		switch (visit.getPageStyle()) {
		case BOXY:
			return stylesheet1;
		case WIDE:
			return stylesheet2;
		default:
			return stylesheet1;
		}
	}

	public String getTab() {
		return tab;
	}

	public String getSubTab() {
		return subTab;
	}

	public String getLoginId() {
		return visit.getMyLoginId();
	}
}
