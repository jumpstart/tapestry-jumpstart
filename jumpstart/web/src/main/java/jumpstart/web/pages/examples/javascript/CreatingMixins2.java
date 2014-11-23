package jumpstart.web.pages.examples.javascript;

import jumpstart.web.state.examples.javascript.MyOrder;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

@Import(stylesheet = "css/examples/js.css")
public class CreatingMixins2 {

	// Screen fields

	@SessionState
	@Property
	private MyOrder myOrder;

}
