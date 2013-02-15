package jumpstart.web.pages.examples.javascript;

import java.text.DateFormat;
import java.text.Format;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.got5.tapestry5.jquery.ImportJQueryUI;

@Import(stylesheet = { "context:css/examples/jquerydatatables.css" })
// Apply the "smoothness" theme (downloaded from http://jqueryui.com/themeroller/)
@ImportJQueryUI(theme = "context:css/examples/jquery/themes/smoothness/jquery-ui-1.8.23.custom.css")
public class JQueryDataTables {
	static private final int MAX_RESULTS = 30;

	// Screen fields

	@Property
	private List<Person> persons;

	@Property
	private Person person;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@Inject
	private Messages messages;

	@Inject
	private Locale currentLocale;

	// The code

	void setupRender() {
		// Get all persons - ask business service to find them (from the database)
		persons = personFinderService.findPersons(MAX_RESULTS);
	}

	public JSONObject getOptions() {
		
		// The available options are documented at http://www.datatables.net/ref
		
		JSONObject options = new JSONObject();
		options.put("bJQueryUI", "true");
		return options;
	}

	public String getPersonRegion() {
		// Follow the same naming convention that the Select component uses
		return messages.get(Regions.class.getSimpleName() + "." + person.getRegion().name());
	}

	public Format getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.MEDIUM, currentLocale);
	}
}
