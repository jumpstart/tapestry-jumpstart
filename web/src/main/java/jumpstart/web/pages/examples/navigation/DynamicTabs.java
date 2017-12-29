package jumpstart.web.pages.examples.navigation;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceRemote;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(stylesheet = "css/examples/js.css")
public class DynamicTabs {
	static private final int MAX_RESULTS = 20;

	// Screen fields

	@Property
	private List<Person> persons;

	@Property
	private Person person;

	@Property
	private int index;

	// Useful bits and pieces

	@EJB
	private IPersonFinderServiceRemote personFinder;

	@Inject
	private JavaScriptSupport javaScriptSupport;

	// The code

	void setupRender() {
		persons = personFinder.findPersons(MAX_RESULTS);
	}

	void afterRender() {
		javaScriptSupport.require("bootstrap/tab");
	}

	public String getTabCssClass() {
		return index == 0 ? "active" : "";
	}

	public String getTabPaneCssClass() {
		return index == 0 ? "fade in active" : "fade";
	}

}
