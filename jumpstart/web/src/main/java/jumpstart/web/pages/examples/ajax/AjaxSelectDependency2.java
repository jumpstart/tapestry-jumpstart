package jumpstart.web.pages.examples.ajax;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class AjaxSelectDependency2 {

	// Screen fields

	@Persist(PersistenceConstants.FLASH)
	@Property
	private String carSource;

	@Persist(PersistenceConstants.FLASH)
	@Property
	private String carMake;

	@Persist(PersistenceConstants.FLASH)
	@Property
	private String carModel;

	@Persist(PersistenceConstants.FLASH)
	@Property
	private String keyWords;

	// The code

	// set() is public so that other pages can use it to set up this page.

	public void set(String carSource, String carMake, String carModel, String keyWords) {
		this.carSource = carSource;
		this.carMake = carMake;
		this.carModel = carModel;
		this.keyWords = keyWords;
	}
}
