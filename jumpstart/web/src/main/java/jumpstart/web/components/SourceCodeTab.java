// Based on Tapestry Stitch's TabGroup (http://tapestry-stitch.uklance.cloudbees.net) 
// and Java Magic's TabPanel (http://tawus.wordpress.com/2011/07/09/a-tab-panel-for-tapestry).

package jumpstart.web.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class SourceCodeTab {

	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String src;

	public String getName() {
		return extractSimpleName(src);
	}

	private String extractSimpleName(String path) {
		String simpleName = path;

		int i = path.lastIndexOf("/");
		simpleName = path.substring(i + 1);

		return simpleName;
	}
}
