// Based on an example kindly provided by George Christman. See http://tapestry.1045711.n5.nabble.com/Currency-Converter-tp5719990.html .

package jumpstart.web.translators;

import java.math.BigDecimal;
import java.text.NumberFormat;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.services.FormSupport;

// This class represents currency with BigDecimal. You may prefer to create a class that represents Money units.
public class MoneyTranslator implements Translator<BigDecimal> {
	private final String name;
	private final Class<BigDecimal> type;
	private final String messageKey;

	public MoneyTranslator() {
		this.name = "money";
		this.type = BigDecimal.class;
		this.messageKey = "money-format-exception";
	}

	@Override
	public String toClient(BigDecimal value) {
		return NumberFormat.getCurrencyInstance().format(value);
	}

	@Override
	public BigDecimal parseClient(Field field, String clientValue, String message) throws ValidationException {
		if (clientValue == null) {
			return null;
		}
		else {
			try {
				// Due to some overlooked situations such as negatives or values submitted without a $ but with a
				// comma, regular expressions are the better choice than parsing with NumberFormat.
				clientValue = clientValue.replaceAll("[^\\d\\-\\.]", "");
				return new BigDecimal(clientValue.toString());
			}
			catch (NumberFormatException ex) {
				throw new ValidationException(message);
			}
		}
	}

	@Override
	public void render(Field field, String message, MarkupWriter writer, FormSupport formSupport) {
		// Do nothing; we don't yet support client-side validation.
		// formSupport.addValidation(field, "currency", message, null);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<BigDecimal> getType() {
		return type;
	}

	@Override
	public String getMessageKey() {
		return messageKey;
	}

}
