package jumpstart.web.pages.examples.ajax;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class AjaxSelectMore2 {

	// Screen fields

	@Persist(PersistenceConstants.FLASH)
	@Property
	private String carMake;

	@Persist(PersistenceConstants.FLASH)
	@Property
	private String carModel;

	@Persist(PersistenceConstants.FLASH)
	@Property
	private String carStyle;

	@Persist(PersistenceConstants.FLASH)
	@Property
	private String keyWords;

	// The code

	// set() is public so that other pages can use it to set up this page.

	public void set(String carMake, String carModel, String carStyle, String keyWords) {
		this.carMake = carMake;
		this.carModel = carModel;
		this.carStyle = carStyle;
		this.keyWords = keyWords;
	}
}
