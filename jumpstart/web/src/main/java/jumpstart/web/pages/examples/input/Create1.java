package jumpstart.web.pages.examples.input;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;

@Import(stylesheet="css/examples/plain.css")
public class Create1 {

	private final String demoModeStr = System.getProperty("jumpstart.demo-mode");

	// Screen fields

	@Property
	private Person person;

	// Other pages

	@InjectPage
	private Create2 page2;

	// Generally useful bits and pieces

	@Component(id = "personForm")
	private BeanEditForm personForm;

	@EJB
	private IPersonManagerServiceLocal personManagerService;

	// The code

	// Form bubbles up the PREPARE event during form render and form submission.

	void onPrepare() throws Exception {
		person = new Person();
	}

	void onValidateFromPersonForm() {
		
		if (personForm.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}
		
		if (demoModeStr != null && demoModeStr.equals("true")) {
			personForm.recordError("Sorry, but this function is not allowed in Demo mode.");
			return;
		}
		
		try {
			personManagerService.createPerson(person);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			personForm.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	Object onSuccess() {
		page2.set(person.getId());
		return page2;
	}

	void onRefresh() {
		// By doing nothing the page will be displayed afresh.
	}
}
