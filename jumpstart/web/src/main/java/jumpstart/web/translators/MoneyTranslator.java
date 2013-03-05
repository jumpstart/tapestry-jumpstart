// Based on an example kindly provided by George Christman. See http://tapestry.1045711.n5.nabble.com/Currency-Converter-tp5719990.html .

package jumpstart.web.translators;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.FormSupport;

// This class represents money with BigDecimal.
public class MoneyTranslator implements Translator<BigDecimal> {

	private final String name;
	private final int maxDecimalPlaces;
	private final ThreadLocale threadLocale;

	private final Class<BigDecimal> type;
	private final String messageKey;

	private ParsePosition parsePosition;

	public MoneyTranslator(String name, int maxDecimalPlaces, ThreadLocale threadLocale) {
		this.name = name;
		this.maxDecimalPlaces = maxDecimalPlaces;
		this.threadLocale = threadLocale;

		type = BigDecimal.class;
		messageKey = "money-format-exception";

		parsePosition = new ParsePosition(0);
	}

	@Override
	public String toClient(BigDecimal value) {

		// We want the client locale's currency format...
		
		Locale locale = threadLocale.getLocale();
		DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
		
		// ...without a currency symbol
		
		DecimalFormatSymbols decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
		decimalFormatSymbols.setCurrencySymbol("");
		decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
		
		return decimalFormat.format(value).trim();
	}

	@Override
	public BigDecimal parseClient(Field field, String clientValue, String message) throws ValidationException {
		
		if (clientValue == null) {
			return null;
		}
		else {
			BigDecimal money = parseMoney(clientValue.trim(), message);
			
			if (money.scale() > maxDecimalPlaces) {
				throw new ValidationException(message);
			}
			
			return money;
		}
		
	}

	private BigDecimal parseMoney(String clientValue, String message) throws ValidationException {

		// Based on the techniques described in http://www.ibm.com/developerworks/java/library/j-numberformat/ .

		Locale locale = threadLocale.getLocale();

		// Try parsing as a decimal number...

		DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getInstance(locale);
		numberFormat.setParseBigDecimal(true);

		// ...in the format of the client locale (eg. 12,345.67 or 12345.67 or -12,345.67 or -12 345,67)

		numberFormat.setNegativePrefix("-");
		numberFormat.setNegativeSuffix("");
		parsePosition.setIndex(0);
		BigDecimal bigDecimal = (BigDecimal) numberFormat.parse(clientValue, parsePosition);

		if (parsePosition.getIndex() == clientValue.length() && bigDecimal != null) {
			return bigDecimal;
		}

		// ...with trailing negative sign (eg. 12,345.67-)

		numberFormat.setNegativePrefix("");
		numberFormat.setNegativeSuffix("-");
		parsePosition.setIndex(0);
		bigDecimal = (BigDecimal) numberFormat.parse(clientValue, parsePosition);

		if (parsePosition.getIndex() == clientValue.length() && bigDecimal != null) {
			return bigDecimal;
		}

		// ...with brackets for negative (eg. (12,345.67))

		numberFormat.setNegativePrefix("(");
		numberFormat.setNegativeSuffix(")");
		parsePosition.setIndex(0);
		bigDecimal = (BigDecimal) numberFormat.parse(clientValue, parsePosition);

		if (parsePosition.getIndex() == clientValue.length() && bigDecimal != null) {
			return bigDecimal;
		}

		throw new ValidationException(message);
	}

	@Override
	public void render(Field field, String message, MarkupWriter writer, FormSupport formSupport) {
		// Do nothing; we don't yet support client-side validation.
		// formSupport.addValidation(field, name, message, null);
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
