package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class Validators {

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String firstName;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String lastName;

}
