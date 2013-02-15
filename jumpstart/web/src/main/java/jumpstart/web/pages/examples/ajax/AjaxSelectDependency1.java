package jumpstart.web.pages.examples.ajax;

import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

public class AjaxSelectDependency1 {
	static final private String SOURCE_LOCAL = "Local";
	static final private String SOURCE_IMPORTED = "Imported";
	static final private String[] ALL_SOURCES = new String[] { SOURCE_LOCAL, SOURCE_IMPORTED };

	static final private String MAKE_HOLDEN = "Holden";
	static final private String MAKE_HONDA = "Honda";
	static final private String MAKE_TOYOTA = "Toyota";
	static final private String[] ALL_MAKES = new String[] { MAKE_HOLDEN, MAKE_HONDA, MAKE_TOYOTA };

	static final private String MODEL_COMMODORE = "Commodore";
	static final private String MODEL_CAMRY = "Camry";
	static final private String MODEL_COROLLA = "Corolla";
	static final private String MODEL_PRIUS = "Prius";
	static final private String MODEL_ACCORD = "Accord";
	static final private String MODEL_CIVIC = "Civic";
	static final private String MODEL_JAZZ = "Jazz";
	static final private String[] LOCAL_HOLDEN_MODELS = new String[] { MODEL_COMMODORE };
	static final private String[] IMPORTED_HOLDEN_MODELS = new String[] {};
	static final private String[] LOCAL_TOYOTA_MODELS = new String[] { MODEL_CAMRY };
	static final private String[] IMPORTED_TOYOTA_MODELS = new String[] { MODEL_CAMRY, MODEL_COROLLA, MODEL_PRIUS };
	static final private String[] LOCAL_HONDA_MODELS = new String[] {};
	static final private String[] IMPORTED_HONDA_MODELS = new String[] { MODEL_ACCORD, MODEL_CIVIC, MODEL_JAZZ };
	static final private String[] NO_MODELS = new String[] {};

	// Activation request parameters (AKA query parameters)

	@ActivationRequestParameter("source")
	private String chosenCarSource;

	@ActivationRequestParameter("make")
	private String chosenCarMake;

	// Screen fields

	@Property
	private String[] carSources;

	@Property
	private String carSource;

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
	private AjaxSelectDependency2 page2;

	// Generally useful bits and pieces

	@InjectComponent
	private Zone carSourceZone;

	@InjectComponent
	private Zone carMakeZone;

	@InjectComponent
	private Zone carModelZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	// The code

	void setupRender() {
		carSources = ALL_SOURCES;
		carMakes = ALL_MAKES;
		carModels = NO_MODELS;
	}

	void onValueChangedFromCarSource(String carSource) {

		// Record the source in the activation parameters (AKA query parameters) so it is available in requests from the other zones.
		
		chosenCarSource = carSource;
		
		// Refresh the makes and models.
		
		carMake = chosenCarMake;
		carMakes = ALL_MAKES;
		
		refreshCarModels();
		
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(carMakeZone).addRender(carModelZone);
		}
	}

	void onValueChangedFromCarMake(String carMake) {

		// Record the make in the activation parameters (AKA query parameters) so it is available in requests from the other zones.

		chosenCarMake = carMake;

		// Refresh the sources and models.
		
		carSource = chosenCarSource;
		carSources = ALL_SOURCES;
		
		refreshCarModels();

		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(carSourceZone).addRender(carModelZone);
		}
	}

	private void refreshCarModels() {

		// Show the models of the chosen source AND make.

		carModel = null;
		carModels = NO_MODELS;

		if (chosenCarSource != null && chosenCarMake != null) {
			
			if (chosenCarSource.equals(SOURCE_LOCAL)) {

				if (chosenCarMake.equals(MAKE_HOLDEN)) {
					carModels = LOCAL_HOLDEN_MODELS;
				}
				else if (chosenCarMake.equals(MAKE_HONDA)) {
					carModels = LOCAL_HONDA_MODELS;
				}
				else if (chosenCarMake.equals(MAKE_TOYOTA)) {
					carModels = LOCAL_TOYOTA_MODELS;
				}
				
			}
			else if (chosenCarSource.equals(SOURCE_IMPORTED)) {
				
				if (chosenCarMake.equals(MAKE_HOLDEN)) {
					carModels = IMPORTED_HOLDEN_MODELS;
				}
				else if (chosenCarMake.equals(MAKE_HONDA)) {
					carModels = IMPORTED_HONDA_MODELS;
				}
				else if (chosenCarMake.equals(MAKE_TOYOTA)) {
					carModels = IMPORTED_TOYOTA_MODELS;
				}
				
			}
			
		}

	}

	Object onSuccess() {
		page2.set(carSource, carMake, carModel, keywords);
		return page2;
	}
}
