package jumpstart.web.pages.examples.component;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class Html5InputTypes {

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String searchValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String urlValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String telValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String emailValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String datetimeValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String dateValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String monthValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String weekValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String timeValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String datetimelocalValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String numberValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String rangeValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String colorValue;

}
