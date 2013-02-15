package jumpstart.web.pages.examples.ajax;

import java.util.ArrayList;
import java.util.List;

import jumpstart.web.services.CountryNames;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class AutocompleteMixin {

	// Screen fields

	@Property
	private String countryName;

	// Generally useful bits and pieces

	@Inject
	private CountryNames countryNames;

	// The code

	List<String> onProvideCompletionsFromCountryName(String partial) {
		List<String> matches = new ArrayList<String>();
		partial = partial.toUpperCase();

		for (String countryName : countryNames.getSet()) {
			if (countryName.contains(partial)) {
				matches.add(countryName);
			}
		}

		return matches;
	}

}
