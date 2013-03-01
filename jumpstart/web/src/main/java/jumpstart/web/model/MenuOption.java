package jumpstart.web.model;

/**
 * MenuOption holds a label to display and a logical page name, eg. "Hello World" and "previews/withlayout/HelloWorld".
 */
public class MenuOption {
	private String label;
	private String page;

	public MenuOption(String label, String page) {
		this.label = label;
		this.page = page;
	}

	public String getLabel() {
		return label;
	};

	public String getPage() {
		return page;
	};
}
