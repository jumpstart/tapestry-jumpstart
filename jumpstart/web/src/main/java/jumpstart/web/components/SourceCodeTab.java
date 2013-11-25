
// Based on Tapestry Stitch's CodeTab. See http://tapestry-stitch.uklance.cloudbees.net .

package jumpstart.web.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class SourceCodeTab {

//	@Parameter(required = true, defaultPrefix = BindingConstants.ASSET)
//	@Property
//	private Asset src;
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String src;

	public String getName() {
//		return src.getResource().getFile();
		return extractSimpleName(src);
	}

	private String extractSimpleName(String path) {
		String simpleName = path;

		int i = path.lastIndexOf("/");
		simpleName = path.substring(i + 1);

		return simpleName;
	}
}
