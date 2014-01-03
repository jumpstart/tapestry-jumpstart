// Based on Tapestry Stitch's TabGroup (http://tapestry-stitch.uklance.cloudbees.net) 
// and Java Magic's TabPanel (http://tawus.wordpress.com/2011/07/09/a-tab-panel-for-tapestry) .

package jumpstart.web.components;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jumpstart.web.models.TabTracker;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.runtime.RenderQueue;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class TabGroup {

	// Screen fields

	@Property
	private List<String> tabIds;

	@Property
	private String tabId;

	@Property
	private int tabNum;

	// Work fields

	private TabTracker tabTracker;
	private List<String> tabLabels;
	private List<String> tabMarkups;

	// Generally useful bits and pieces

	@Inject
	private Environment environment;

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@Inject
	private ComponentResources componentResources;

	// The code

	/**
	 * The tricky part is that we can't render the navbar before we've rendered the body, because we don't know how many
	 * elements are in the body nor what labels they would like. We solve this by making a TabTracker available to the
	 * body. The Tabs in the body will record, in TabTracker, the labels and markup they want. Later, in our 
	 * afterRenderBody(), we will get those labels and markup from TabTracker, then render the whole TabGroup at once.
	 */
	void beginRender() {
		environment.push(TabTracker.class, new TabTracker());
	}

	/**
	 * By the time this method is called, we expect each Tab in the body of this component to have recorded, in
	 * TabTracker, the tab labels and markup that it wants, and to have deleted from the DOM any markup it generated.
	 * Using what's in TabTracker we can now render a navbar with appropriate labels, then render the markups below it.
	 */
	void afterRenderBody(MarkupWriter markupWriter) {
		tabTracker = environment.pop(TabTracker.class);

		tabLabels = tabTracker.getLabels();
		tabMarkups = tabTracker.getMarkups();

		// Invent unique ids for each tab.

		tabIds = new ArrayList<>();

		for (int i = 0; i < tabLabels.size(); i++) {
			String id = javaScriptSupport.allocateClientId(componentResources);
			tabIds.add(id);
		}
	}

	void afterRender() {
		// We depend on http://getbootstrap.com/javascript/#tabs . We use its Markup technique.
		javaScriptSupport.require("bootstrap/tab");
	}

	public String getTabLabel() {
		return tabLabels.get(tabNum);
	}

	public String getActive() {
		return tabNum == 0 ? "active" : "";
	}

	public RenderCommand getTabMarkup() {
		return new RenderCommand() {
			public void render(MarkupWriter writer, RenderQueue queue) {
				writer.writeRaw(tabMarkups.get(tabNum));
			}
		};
	}
}
