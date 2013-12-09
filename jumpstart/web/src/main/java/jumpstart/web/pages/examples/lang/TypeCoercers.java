package jumpstart.web.pages.examples.lang;

import java.util.Date;

import javax.ejb.EJB;

import jumpstart.business.domain.datestuff.DatesExample;
import jumpstart.business.domain.datestuff.iface.IDateStuffServiceLocal;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.joda.time.DateMidnight;

@Import(stylesheet = "css/examples/plain.css")
public class TypeCoercers {

	// Screen fields

	@Property
	private DatesExample datesExample;

	// Work fields

	// This carries version through the redirect that follows a server-side validation failure.
	@Persist(PersistenceConstants.FLASH)
	private Integer versionFlash;

	// Generally useful bits and pieces

	@EJB
	private IDateStuffServiceLocal dateStuffService;

	@Component(id = "dates")
	private Form form;

	@Inject
	private TypeCoercer typeCoercer;

	// The code

	// Form bubbles up the PREPARE_FOR_RENDER event during form render.

	void onPrepareForRender() throws Exception {
		datesExample = findDatesExample(1L);

		// If the form has errors then we're redisplaying after a redirect.
		// Form will restore your input values but it's up to us to restore Hidden values.

		if (form.getHasErrors()) {
			datesExample.setVersion(versionFlash);
		}
	}

	// Form bubbles up the PREPARE_FOR_SUBMIT event during form submission.

	void onPrepareForSubmit() throws Exception {
		datesExample = findDatesExample(1L);
	}

	void onValidateFromDates() {
		try {
			dateStuffService.changeDatesExample(datesExample);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			form.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	void onFailure() {
		versionFlash = datesExample.getVersion();
	}

	void onRefresh() {
		// By doing nothing the page will be displayed afresh.
	}

	private DatesExample findDatesExample(Long id) throws Exception {
		// Ask business service to find DatesExample
		DatesExample datesExample = dateStuffService.findDatesExample(id);

		if (datesExample == null) {
			throw new IllegalStateException("Database data has not been set up!");
		}

		return datesExample;
	}

	public String getDateFieldFormat() {
		return "dd MMMM yyyy";
	}

	public String getExplainLongToInteger() {
		try {
			return typeCoercer.explain(Long.class, Integer.class);
		}
		catch (Exception e) {
			return e.getMessage();
		}
	}

	public String getExplainStringToInteger() {
		try {
			return typeCoercer.explain(String.class, Integer.class);
		}
		catch (Exception e) {
			return e.getMessage();
		}
	}

	public String getExplainDateToDateMidnight() {
		try {
			return typeCoercer.explain(Date.class, DateMidnight.class);
		}
		catch (Exception e) {
			return e.getMessage();
		}
	}
}
