package jumpstart.web.components.together;

import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.model.Menu;
import jumpstart.web.model.MenuOption;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

@ProtectedPage
public class Layout2 {
	private static final String CHOSEN_OPTION_CSS_CLASS = "chosenOption";

	// Parameters

	@Parameter
	@Property
	private String title;

	@Parameter
	@Property
	private String chosenOption;

	// Screen fields

	private Menu menu;

	@Property
	private MenuOption menuOption;

	// The code

	public Menu getMenu() {

		if (menu == null) {
			menu = new Menu();
			menu.add(new MenuOption("Graceful AJAX Filter CRUD", "together/withlayout2/gracefulajaxfiltercrud/Persons"));
			menu.add(new MenuOption("Graceful AJAX Components CRUD", "together/withlayout2/gracefulajaxcomponentscrud/Persons"));
		}

		return menu;
	}

	public String getMenuOptionCSSClass() {
		return menuOption.getLabel().equals(chosenOption) ? CHOSEN_OPTION_CSS_CLASS : "";
	}

}
