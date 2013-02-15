package jumpstart.web.pages.examples.ajax;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

public class AjaxSelectMore1 {
	static final private String MAKE_HONDA = "Honda";
	static final private String MAKE_TOYOTA = "Toyota";
	static final private String[] ALL_MAKES = new String[] { MAKE_HONDA, MAKE_TOYOTA };

	static final private String MODEL_ACCORD = "Accord";
	static final private String MODEL_CIVIC = "Civic";
	static final private String MODEL_JAZZ = "Jazz";
	static final private String MODEL_CAMRY = "Camry";
	static final private String MODEL_COROLLA = "Corolla";
	static final private String[] HONDA_MODELS = new String[] { MODEL_ACCORD, MODEL_CIVIC, MODEL_JAZZ };
	static final private String[] TOYOTA_MODELS = new String[] { MODEL_CAMRY, MODEL_COROLLA };
	static final private String[] NO_MODELS = new String[] {};

	static final private String[] ACCORD_STYLES = new String[] { "Sedan", "Hatchback" };
	static final private String[] CIVIC_STYLES = new String[] { "Sedan", "Wagon", "Coupe" };
	static final private String[] JAZZ_STYLES = new String[] { "1.6", "2.0" };
	static final private String[] CAMRY_STYLES = new String[] { "Sedan", "Wagon" };
	static final private String[] COROLLA_STYLES = new String[] { "Town", "Sports" };
	static final private String[] NO_STYLES = new String[] {};

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
	private String[] carStyles;

	@Property
	private String carStyle;

	@Property
	private String keywords;

	// Other pages

	@InjectPage
	private AjaxSelectMore2 page2;

	// Generally useful bits and pieces

	@InjectComponent
	private Zone carModelZone;

	@InjectComponent
	private Zone carStyleZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	// The code

	void setupRender() {
		if (carMakes == null) {
			carMakes = ALL_MAKES;
			carModels = NO_MODELS;
			carStyles = NO_STYLES;
		}
	}

	void onValueChangedFromCarMake(String carMake) {

		// A new make has been chosen - clear the model and style.

		carModel = null;
		carModels = NO_MODELS;
		carStyle = null;
		carStyles = NO_STYLES;

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
			ajaxResponseRenderer.addRender(carModelZone).addRender(carStyleZone);
		}
	}

	void onValueChangedFromCarModel(String carModel) {

		// A new model has been chosen - clear the style.

		carStyle = null;
		carStyles = NO_STYLES;

		// Show the styles of the chosen model.

		if (carModel != null) {
			if (carModel.equals(MODEL_ACCORD)) {
				carStyles = ACCORD_STYLES;
			}
			else if (carModel.equals(MODEL_CIVIC)) {
				carStyles = CIVIC_STYLES;
			}
			else if (carModel.equals(MODEL_JAZZ)) {
				carStyles = JAZZ_STYLES;
			}
			else if (carModel.equals(MODEL_CAMRY)) {
				carStyles = CAMRY_STYLES;
			}
			else if (carModel.equals(MODEL_COROLLA)) {
				carStyles = COROLLA_STYLES;
			}
		}

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender("carStyleZone", carStyleZone);
		}
	}

	Object onSuccess() {
		page2.set(carMake, carModel, carStyle, keywords);
		return page2;
	}
}
