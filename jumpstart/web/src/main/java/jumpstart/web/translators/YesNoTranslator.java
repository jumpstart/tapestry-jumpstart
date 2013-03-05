package jumpstart.web.translators;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.services.FormSupport;

public class YesNoTranslator implements Translator<Boolean> {
	private final String name;
	private final Class<Boolean> type;
	private final String messageKey;

	public YesNoTranslator(String name) {
		this.name = name;
		this.type = Boolean.class;
		this.messageKey = "yesno-format-exception";
	}

	@Override
	public String toClient(Boolean value) {
		return (value == null ? null : value == true ? "yes" : "no");
	}

	@Override
	public Boolean parseClient(Field field, String clientValue, String message) throws ValidationException {
		if (clientValue == null) {
			return null;
		}
		else {
			String s = clientValue.toLowerCase();
			if (s.equals("y") || s.equals("yes")) {
				return Boolean.TRUE;
			}
			else if (s.equals("n") || s.equals("no")) {
				return Boolean.FALSE;
			}
			else {
				throw new ValidationException(message);
			}
		}
	}

	@Override
	public void render(Field field, String message, MarkupWriter writer, FormSupport formSupport) {
		// Do nothing; we don't yet support client-side validation.
		// formSupport.addValidation(field, "yesno", message, null);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<Boolean> getType() {
		return type;
	}

	@Override
	public String getMessageKey() {
		return messageKey;
	}

	
}
