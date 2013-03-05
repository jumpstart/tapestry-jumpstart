package jumpstart.web.pages.examples.localization;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.LocalizationSetter;

public class LocalizationByTemplate {

	// Screen fields

	@Inject
	@Property
	private Locale currentLocale;

	@Property
	private Date date;

	@Property
	private double number = -1234.56;

	@Property
	private DateFormat dateFormat;

	@Property
	private NumberFormat numberFormat;

	@Property
	private List<Locale> supportedLocales;

	@Property
	private Locale supportedLocale;

	// Generally useful bits and pieces

	@Inject
	private LocalizationSetter localizationSetter;

	// The code

	void setupRender() {
		supportedLocales = localizationSetter.getSupportedLocales();
		date = new Date();
		dateFormat = DateFormat.getDateInstance(DateFormat.LONG, currentLocale);
		numberFormat = NumberFormat.getInstance(currentLocale);
	}

	void onSwitchLocale(String localeCode) {
		localizationSetter.setLocaleFromLocaleName(localeCode);
	}
}
