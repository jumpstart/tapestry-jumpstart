package jumpstart.web.pages.examples.navigation;

import java.math.BigDecimal;

import jumpstart.web.models.Mode;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet = "css/examples/plain.css")
public class MoreParameters2 {

	// The activation context

	@Property
	private int intParam;

	@Property
	private Long longParam;

	@Property
	private String stringParam;

	@Property
	private double doubleParam;

	@Property
	private BigDecimal bigDecimalParam;

	@Property
	private boolean booleanParam;

	@Property
	private Mode modeParam;

	@Property
	private Mode mode2Param;

	// The code

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(int intParam, Long longParam, String stringParam, double doubleParam, BigDecimal bigDecimalParam,
			boolean booleanParam, Mode modeParam, Mode mode2Param) {
		this.intParam = intParam;
		this.longParam = longParam;
		this.stringParam = stringParam;
		this.doubleParam = doubleParam;
		this.bigDecimalParam = bigDecimalParam;
		this.booleanParam = booleanParam;
		this.modeParam = modeParam;
		this.mode2Param = mode2Param;
	}

}
