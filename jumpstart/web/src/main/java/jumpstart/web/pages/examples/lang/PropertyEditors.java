package jumpstart.web.pages.examples.lang;

import javax.ejb.EJB;

import jumpstart.business.domain.datestuff.DatesExample;
import jumpstart.business.domain.datestuff.iface.IDateStuffServiceLocal;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;

public class PropertyEditors {

	// Screen fields

	@Property
	private DatesExample datesExample;
	
	// Generally useful bits and pieces

	@EJB
	private IDateStuffServiceLocal dateStuffService;

	@Component(id = "updateDates")
	private BeanEditForm form;

	// The code

	void setupRender() throws Exception {
		datesExample = findDatesExample(1L);
	}

	void onPrepareForSubmit() throws Exception {
		datesExample = findDatesExample(1L);
	}

	void onValidateFromUpdateDates() {
		try {
			if (datesExample.getADateMidnight() == null || datesExample.getALocalDate() == null) {
				form.recordError("Both dates are required.");
				return;
			}
			dateStuffService.changeDatesExample(datesExample);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			form.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	private DatesExample findDatesExample(Long id) throws Exception {
		// Ask business service to find DatesExample
		DatesExample datesExample = dateStuffService.findDatesExample(id);

		if (datesExample == null) {
			throw new IllegalStateException("Database data has not been set up!");
		}

		return datesExample;
	}
	
}
