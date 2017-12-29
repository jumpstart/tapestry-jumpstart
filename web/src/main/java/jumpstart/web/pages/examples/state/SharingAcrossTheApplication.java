package jumpstart.web.pages.examples.state;

import jumpstart.web.services.CountryNames;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

@Import(stylesheet="css/examples/light.css")
public class SharingAcrossTheApplication {

	// Screen fields

	@Inject
	@Property
	private CountryNames countryNames;

}
