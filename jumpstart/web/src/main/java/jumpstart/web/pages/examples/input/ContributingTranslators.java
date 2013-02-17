package jumpstart.web.pages.examples.input;

import java.math.BigDecimal;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class ContributingTranslators {

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Boolean newToTapestry;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private BigDecimal price;

}
