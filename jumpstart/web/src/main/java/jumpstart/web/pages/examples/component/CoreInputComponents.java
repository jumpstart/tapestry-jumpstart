package jumpstart.web.pages.examples.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.services.StringValueEncoder;

public class CoreInputComponents {

	@Property
	private final StringValueEncoder stringValueEncoder = new StringValueEncoder();

	/* Checkbox */

	@Property
	@Persist(PersistenceConstants.FLASH)
	private boolean checkboxValue;

	/* Checklist */

	@Property
	@Persist(PersistenceConstants.FLASH)
	private List<String> checklistSelectedValues;

	@Property
	private final String[] STATIONERY = { "Pens", "Pencils", "Paper" };

	/* DateField */

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Date dateValue;

	@Property
	// We could return a DateFormat, but instead we'll return a String which DateField will coerce into a DateFormat.
	private String dateInFormatStr = "dd/MM/yyyy";
	
	@Property
	// We could return a DateFormat, but instead we'll return a String which DateField will coerce into a DateFormat.
	private String datetimeOutFormatStr = "yyyy-MM-dd HH:mm:ss z";

	/* Palette */

	@Property
	@Persist(PersistenceConstants.FLASH)
	private List<String> paletteSelectedValues;

	@Property
	private final String[] PETS = { "Dog", "Cat", "Parrot", "Mouse" };

	/* Password */

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String passwordValue;

	/* RadioGroup and Radio */

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String radioSelectedValue;

	/* Select */

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String selectedValue;

	/* TextArea */

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String textAreaValue;

	/* TextField */

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String textValue;

	/* Life-cycle stuff. Fields that are mutable MUST be initialized here rather than where they are declared. */

	void onPrepareForRender() {
		if (dateValue == null) {
			dateValue = new Date(); 
		}
		if (paletteSelectedValues == null) {
			paletteSelectedValues = new ArrayList<String>();
		}
	}

}
