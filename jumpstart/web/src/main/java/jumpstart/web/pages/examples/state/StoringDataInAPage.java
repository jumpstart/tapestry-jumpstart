package jumpstart.web.pages.examples.state;

import jumpstart.web.pages.Index;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class StoringDataInAPage {

	// Screen fields

	@Property
	@Persist
	private int count;

	// Generally useful bits and pieces

	@Inject
	private ComponentResources componentResources;

	// The code
	
	void setupRender() {
		count++;
	}

	Object onClear() {
		componentResources.discardPersistentFieldChanges();
		return Index.class;
	}
}
