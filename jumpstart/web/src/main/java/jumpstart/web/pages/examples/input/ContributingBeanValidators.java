package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

// The @Import tells Tapestry to put a link to the file in the head of the page so that the browser will pull it in. 
@Import(stylesheet="css/examples/plain.css", library = "js/letters.js")
public class ContributingBeanValidators {

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String firstName;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String lastName;

}
