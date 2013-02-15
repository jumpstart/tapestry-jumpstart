package jumpstart.web.pages.examples.ajax;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

public class AjaxSelect1 {
	static final private String MAKE_HONDA = "Honda";
	static final private String MAKE_TOYOTA = "Toyota";
	static final private String[] ALL_MAKES = new String[] { MAKE_HONDA, MAKE_TOYOTA };

	static final private String[] HONDA_MODELS = new String[] { "Accord", "Civic", "Jazz" };
	static final private String[] TOYOTA_MODELS = new String[] { "Camry", "Corolla" };
	static final private String[] NO_MODELS = new String[] {};

	// Screen fields

	@Property
	private String[] carMakes;

	@Property
	private String carMake;

	@Property
	private String[] carModels;

	@Property
	private String carModel;

	@Property
	private String keywords;

	// Other pages

	@InjectPage
	private AjaxSelect2 page2;

	// Generally useful bits and pieces

	@InjectComponent
	private Zone carModelZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	// The code

	void setupRender() {
		if (carMakes == null) {
			carMakes = ALL_MAKES;
			carModels = NO_MODELS;
		}
	}

	void onValueChangedFromCarMake(String carMake) {
		
		// A new make has been chosen - clear the model.

		carModel = null;
		carModels = NO_MODELS;

		// Show the models of the chosen make.
		
		if (carMake != null) {
			if (carMake.equals(MAKE_HONDA)) {
				carModels = HONDA_MODELS;
			}
			else if (carMake.equals(MAKE_TOYOTA)) {
				carModels = TOYOTA_MODELS;
			}
		}

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(carModelZone);
		}
	}

	Object onSuccess() {
		page2.set(carMake, carModel, keywords);
		return page2;
	}
}
