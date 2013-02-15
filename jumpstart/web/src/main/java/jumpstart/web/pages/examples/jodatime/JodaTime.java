package jumpstart.web.pages.examples.jodatime;

import java.util.Locale;

import javax.ejb.EJB;

import jumpstart.business.domain.datestuff.DatesExample;
import jumpstart.business.domain.datestuff.iface.IDateStuffServiceLocal;

import org.apache.tapestry5.annotations.Property;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class JodaTime {

	// Screen fields

	@Property
	private DatesExample datesExample;

	@Property
	private DateTimeFormatter french;

	@Property
	private DateTimeFormatter ISODate;

	// Generally useful bits and pieces

	@EJB
	private IDateStuffServiceLocal dateStuffService;

	// The code

	void setupRender() {

		// Ask business service to find DatesExample with id = 1.

		datesExample = dateStuffService.findDatesExample(1L);

		if (datesExample == null) {
			throw new IllegalStateException("Database data has not been set up!");
		}

		french = DateTimeFormat.fullDate().withLocale(Locale.FRENCH);
		ISODate = ISODateTimeFormat.date();
	}
}
