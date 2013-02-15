package jumpstart.web.pages.examples.lang;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class IfNotNegateSwitchElseUnless {

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	// We use a String, not a Boolean, in the radio group value so that we can represent null. Boolean can't represent
	// null because Tapestry will coerce it to Boolean.FALSE. See https://issues.apache.org/jira/browse/TAPESTRY-1928 .
	private String valueForMyBoolean;

	@Property
	private Boolean myBoolean;

	// Generally useful bits and pieces

	@Inject
	Block t, f, n;

	// The code

	void setupRender() {

		// First time in, valueForMyBoolean will be null.

		if (valueForMyBoolean == null) {
			valueForMyBoolean = "T";
		}

		// Set myBoolean based on valueForMyBoolean.

		if (valueForMyBoolean.equals("T")) {
			myBoolean = Boolean.TRUE;
		}
		else if (valueForMyBoolean.equals("F")) {
			myBoolean = Boolean.FALSE;
		}
		else if (valueForMyBoolean.equals("N")) {
			myBoolean = null;
		}
		else {
			throw new IllegalStateException(valueForMyBoolean);
		}
	}

	public Block getCase() {

		// If myBoolean was an int or enum we could use switch/case logic instead of if/else -
		// see http://tapestry.apache.org/switching-cases.html

		if (myBoolean == null) {
			return n;
		}
		else if (myBoolean == Boolean.TRUE) {
			return t;
		}
		else {
			return f;
		}
	}

}
