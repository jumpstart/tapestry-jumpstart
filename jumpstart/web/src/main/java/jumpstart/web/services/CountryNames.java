package jumpstart.web.services;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import jumpstart.util.StringUtil;

/**
 * Provides a sorted set of country names in upper case built from all available locales
 */

public class CountryNames {
	
	private Set<String> countryNames = new TreeSet<String>();

	public CountryNames() {
		Locale[] availableLocales = Locale.getAvailableLocales();

		for (Locale locale : availableLocales) {
			if (StringUtil.isNotEmpty(locale.getDisplayCountry())) {
				countryNames.add(locale.getDisplayCountry().toUpperCase());
			}
		}
	}

	public Set<String> getSet() {
		return Collections.unmodifiableSet(countryNames);
	}
	
}
