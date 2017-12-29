package jumpstart.web.components.theapp;

import org.apache.tapestry5.annotations.Parameter;

public class NavBar {
	private static final String ACTIVE_TAB_CLASS = "activeTab";
	private static final String ACTIVE_SUB_TAB_CLASS = "activeSubTab";

	@Parameter
	private String tab = "";

	@Parameter
	private String subTab = "";

	// Tabs

	public boolean isSecurityTabActive() {
		return tab.equals("Security");
	}

	public String getCSSClassForSecurityTab() {
		return getCSSClassForTab("Security");
	}

	private String getCSSClassForTab(String tabName) {
		return tabName.equals(tab) ? ACTIVE_TAB_CLASS : "";
	}

	// SubTabs

	public String getCSSClassForRoleSubTab() {
		return getCSSClassForSubTab("Role");
	}

	public String getCSSClassForUserSubTab() {
		return getCSSClassForSubTab("User");
	}

	public String getCSSClassForUserRoleSubTab() {
		return getCSSClassForSubTab("UserRole");
	}

	private String getCSSClassForSubTab(String subTabName) {
		return subTabName.equals(subTab) ? ACTIVE_SUB_TAB_CLASS : "";
	}
}
