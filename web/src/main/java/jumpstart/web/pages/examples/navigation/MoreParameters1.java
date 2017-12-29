package jumpstart.web.pages.examples.navigation;

import java.math.BigDecimal;

import jumpstart.web.models.Mode;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet = "css/examples/olive.css")
public class MoreParameters1 {
	
	@Property
	private int anInt;
	
	@Property
	private Long aLong;
	
	@Property
	private String aString;
	
	@Property
	private double aDouble;
	
	@Property
	private BigDecimal aBigDecimal;
	
	@Property
	private boolean aBoolean;
	
	@Property
	private Mode aMode;
	
	void setupRender()  {
		anInt = 1;
		aLong = new Long(2);
		aString = "3";
		aDouble = 4.321;
		aBigDecimal = new BigDecimal("5.432");
		aBoolean = true;
		aMode = Mode.REVIEW;
	}

}
