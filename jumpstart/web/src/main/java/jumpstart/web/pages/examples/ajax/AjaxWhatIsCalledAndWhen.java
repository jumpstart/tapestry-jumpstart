package jumpstart.web.pages.examples.ajax;

import java.util.Date;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.slf4j.Logger;

@Import(stylesheet={"css/examples/ajaxwhatiscalledandwhen.css"})
public class AjaxWhatIsCalledAndWhen {
	static final private String[] ALL_MAKES = new String[] { "Honda", "Toyota" };
	
	// Screen fields

	private String name1;

	private String name2;

	private String carMake;

	@Property
	private String[] carMakes;

	// Generally useful bits and pieces

	@Inject
	private Logger logger;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@InjectComponent
	private Zone time1Zone;

	@InjectComponent
	private Zone time2Zone;

	@InjectComponent
	private Zone formZone;

	@InjectComponent
	private Zone name2Zone;

	@InjectComponent
	private Zone carDisplayZone;

	// The code

	void pageLoaded() {
		logger.debug("   ");
		logger.debug("pageLoaded()");
	}

	void pageAttached() {
		logger.debug("   ");
		logger.debug("pageAttached()");
	}

	void pageDetached() {
		logger.debug("pageDetached()");
	}

	void onActivate() {
		logger.debug("...onActivate()");
	}

	void onPassivate() {
		logger.debug("...onPassivate()");
	}

	void setupRender() {
		logger.debug("...setupRender()");
		if (carMakes == null) {
			carMakes = ALL_MAKES;
		}
	}

	void beginRender() {
		logger.debug("...beginRender()");
	}

	void afterRender() {
		logger.debug("...afterRender()");
	}

	void cleanupRender() {
		logger.debug("...cleanupRender()");
	}

	void onUpdateTime1() {
		logger.debug("...onUpdateTime1()");
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(time1Zone);
		}
	}

	void onAction() {
		logger.debug("...onAction()");
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(time2Zone);
		}
	}

	void onPrepareForRender() {
		logger.debug("...onPrepareForRender()");
	}

	void onPrepare() {
		logger.debug("...onPrepare()");
	}

	void onPrepareForSubmit() {
		logger.debug("...onPrepareForSubmit()");
	}

	void onValidateFromName1() {
		logger.debug("...onValidateFromName1()");
	}

	void onSelected() {
		logger.debug("...onSelected()");
	}

	void onValidateFromFormWithZone() {
		logger.debug("...onValidateFromFormWithZone()");
	}

	void onSuccess() {
		logger.debug("...onSuccess()");
	}

	void onFailure() {
		logger.debug("...onFailure()");
	}

	void onSubmit() {
		logger.debug("...onSubmit()");
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(formZone);
		}
	}

	void onName2Changed() {
		logger.debug("...onName2Changed()");
		name2 = request.getParameter("param");
		if (name2 == null) {
			name2 = "";
		}
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(name2Zone);
		}
	}

	void onValueChangedFromCarMake(String carMake) {
		logger.debug("...onValueChangedFromCarMake()");
		this.carMake = carMake;
		carMakes = ALL_MAKES;
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(carDisplayZone);
		}
	}

	public String getMessage() {
		logger.debug("...getMessage()");
		return "This message is generated by getMessage().";
	}

	public Date getTime1() {
		logger.debug("...getTime1()");
		return new Date();
	}

	public Date getTime2() {
		logger.debug("...getTime2()");
		return new Date();
	}

	public String getName1() {
		logger.debug("...getName1()");
		return name1;
	}

	public void setName1(String name1) {
		logger.debug("...setName1()");
		this.name1 = name1;
	}

	public String getCarMake() {
		logger.debug("...getCarMake()");
		return carMake;
	}

	public void setCarMake(String carMake) {
		logger.debug("...setCarMake()");
		this.carMake = carMake;
	}
	
	public String getName2() {
		logger.debug("...getName2()");
		return name2;
	}
	
	public void setName2(String name2) {
		logger.debug("...setName2()");
		this.name2 = name2;
	}
}
