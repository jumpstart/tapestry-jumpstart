package jumpstart.web.pages.examples.component;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Alerts {
	
	// Screen fields
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private Duration duration;
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private Severity severity;
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private int quantity;
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private Boolean showDismiss;

	// Generally useful bits and pieces

	@Inject
	private AlertManager alertManager;

	// The code
	
	void setupRender() {
		if (duration == null) {
			duration = Duration.SINGLE;
		}
		if (severity == null) {
			severity = Severity.INFO;
		}
		if (quantity == 0) {
			quantity = 1;
		}
		if (showDismiss == null) {
			showDismiss = true;
		}
	}

	void onValidateFromForm() {
		for (int i = 0; i < quantity; i++) {
			alertManager.alert(duration, severity, "Here is a " + duration + ", " + severity + " alert.");
		}
	}

	public boolean isHideDismiss() {
		return !showDismiss;
	}
}
