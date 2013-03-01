package jumpstart.web.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Menu holds a list of MenuOption.
 */
public class Menu {
	private List<MenuOption> menuOptions = new ArrayList<MenuOption>();

	public Menu() {
	}

	public void add(MenuOption menuOption) {
		menuOptions.add(menuOption);
	};

	public List<MenuOption> getMenuOptions() {
		return menuOptions;
	};
}
