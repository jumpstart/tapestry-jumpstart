package jumpstart.web.pages.examples.component;

import jumpstart.web.pages.Index;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Kaptcha1 {

	// Other pages

	@InjectPage
	private Kaptcha2 page2;

	// Generally useful bits and pieces

	@Inject
	private ComponentResources componentResources;

	// The code

	Object onSuccess() {
		componentResources.discardPersistentFieldChanges();
		return page2;
	}
	
	Object onHome() {
		componentResources.discardPersistentFieldChanges();
		return Index.class;
	}
}
