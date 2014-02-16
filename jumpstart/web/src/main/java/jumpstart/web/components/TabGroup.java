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
	 * The tricky part is that we can't render the navbar before we've rendered the body because we don't know how many
	 * elements are in the body nor what labels they would like. We solve this by making a TabTracker available to the
	 * body. Each Tab in the body will put its label and markup in TabTracker instead of rendering it. Afterwards, in
	 * our afterRenderBody(), we will read TabTracker and render the tabs and tab content.
	 */
	void beginRender() {
		environment.push(TabTracker.class, new TabTracker());
	}

	/**
	 * By the time this method is called, we expect each Tab in the body of this component to have recorded a label and
	 * markup in TabTracker instead of rendering it. Using what's in TabTracker we get ready to render tabs and tab
	 * content.
	 */
	void afterRenderBody(MarkupWriter markupWriter) {
		TabTracker tabTracker = environment.pop(TabTracker.class);

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

	public String getActive() {
		return tabNum == 0 ? "active" : "";
	}

	public String getTabLabel() {
		return tabLabels.get(tabNum);
	}

	public RenderCommand getTabMarkup() {
		return new RenderCommand() {
			public void render(MarkupWriter writer, RenderQueue queue) {
				writer.writeRaw(tabMarkups.get(tabNum));
			}
		};
	}
}
