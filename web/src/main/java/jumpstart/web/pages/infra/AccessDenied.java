package jumpstart.web.pages.infra;

import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Response;

/**
 * Intended for use with AssetProtectionFilter.
 */
public class AccessDenied {

	// Other useful bits and pieces

	@Inject
	private Response response;

	// The code

	public void setupRender() {
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	}

}
