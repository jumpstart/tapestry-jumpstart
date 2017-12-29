// Based on http://tapestry.apache.org/tapestry5/guide/beaneditform.html

package jumpstart.web.pages.infra;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.services.PropertyEditContext;

public class AppPropertyEditBlocks {

	@Property
	@Environmental
	private PropertyEditContext context;

	@InjectComponent
	private DateField dateMidnight;

	@InjectComponent
	private DateField localDate;

	public DateFormat getDateInputFormat() {
		return new SimpleDateFormat("dd MMMM yyyy");
	}

	public FieldTranslator<?> getDateMidnightTranslator() {
		return context.getTranslator(dateMidnight);
	}

	public FieldValidator<?> getDateMidnightValidator() {
		return context.getValidator(dateMidnight);
	}

	public FieldTranslator<?> getLocalDateTranslator() {
		return context.getTranslator(localDate);
	}

	public FieldValidator<?> getLocalDateValidator() {
		return context.getValidator(localDate);
	}

}
