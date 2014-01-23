package jumpstart.web.components.together.componentscrud2;

import java.text.Format;
import java.text.SimpleDateFormat;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.util.ExceptionUtil;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class PersonEditor {

	// Parameters

	@Parameter(required = true, allowNull = false)
	@Property
	private Person person;

	@Parameter(value = "false")
	@Property
	private boolean disabled;

	// Generally useful bits and pieces

	@Inject
	private Messages messages;

	// The code


	// ////////////////////////////////////////////////////////////////////////
	// GETTERS AND SETTERS.
	// ////////////////////////////////////////////////////////////////////////

	public String getPersonRegion() {
		return messages.get(Regions.class.getSimpleName() + "." + person.getRegion().name());
	}

	public String getDatePattern() {
		return "dd/MM/yyyy";
	}

	public Format getDateFormat() {
		return new SimpleDateFormat(getDatePattern());
	}
}
