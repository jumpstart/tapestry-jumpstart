package jumpstart.web.components.theapp;

import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.pages.theapp.LogIn;
import jumpstart.web.state.theapp.Visit;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;
import org.slf4j.Logger;

@ProtectedPage
@Import(stylesheet = "css/theapp/simplelayout.css")
public class SimpleLayout {

	@Parameter
	private String title = "";

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
		return LogIn.class;
	}

	public String getTitle() {
		return title;
	}

	public String getLoginId() {
		return visit.getMyLoginId();
	}
}
