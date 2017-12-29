package jumpstart.web.pages.examples.navigation;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet = "css/examples/olive.css")
public class ActivationRequestParameters1 {
	
	@Property
	private String partialName;
	
	void setupRender() {
		partialName = "humpty o";
	}
	
	public Map<String, Object> getQueryParams() {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("partialName", partialName);
		return queryParams;
	}
	
}