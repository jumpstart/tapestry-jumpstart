package jumpstart.web.pages.examples.input;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

@Import(stylesheet = "css/examples/plain.css")
public class Validators {

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	@NotNull
	@Size(max = 10)
	private String firstName;

	@Property
	@Persist(PersistenceConstants.FLASH)
	@NotNull
	@Size(max = 10)
	private String lastName;

}
