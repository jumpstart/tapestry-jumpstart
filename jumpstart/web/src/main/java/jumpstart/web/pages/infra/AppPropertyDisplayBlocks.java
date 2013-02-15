// Based on http://tapestry.apache.org/tapestry5/guide/beaneditform.html

package jumpstart.web.pages.infra;

import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.PropertyOutputContext;

public class AppPropertyDisplayBlocks {

	@Property
	@Environmental
	private PropertyOutputContext context;

}
