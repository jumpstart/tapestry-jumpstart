package jumpstart.web.pages.examples.styling;

import org.apache.tapestry5.annotations.Import;

@Import(stylesheet = "css/examples/olive.css")
public class Stylesheets {

	public String getUsername() {
		return "world";
	}

}
