package jumpstart.web.pages.examples.navigation;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.annotations.Property;

public class ActivationRequestParameters1 {
	
	@Property
	private Long personId;
	
	void setupRender() {
		personId = new Long(1L);
	}
	
	public Map<String, Object> getQueryParams() {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("personid", personId);
		return queryParams;
	}
	
}