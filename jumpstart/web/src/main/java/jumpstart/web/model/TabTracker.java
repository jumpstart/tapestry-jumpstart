// Based on Tapestry Stitch's TabGroup (http://tapestry-stitch.uklance.cloudbees.net) 
// and Java Magic's TabPanel (http://tawus.wordpress.com/2011/07/09/a-tab-panel-for-tapestry) .

package jumpstart.web.model;

import java.util.ArrayList;
import java.util.List;

public class TabTracker {

	private List<String> labels = new ArrayList<>();
	private List<String> markups = new ArrayList<>();

	public void addTab(String label, String markup) {
		labels.add(label);
		markups.add(markup);
	}

	public List<String> getLabels() {
		return labels;
	}

	public List<String> getMarkups() {
		return markups;
	}
}
