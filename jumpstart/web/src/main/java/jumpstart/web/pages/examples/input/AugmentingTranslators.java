package jumpstart.web.pages.examples.input;

import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.internal.translator.NumericTranslatorSupport;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class AugmentingTranslators {
	private static final String PARSED = "(parseClient handler was invoked)";

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private long primitiveWithZeroSuppressed;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String primitiveWithZeroSuppressedMessage;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Long objectDisplayingNullAsZero;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String objectDisplayingNullAsZeroMessage;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Long objectUsingZeroNullFieldStrategy;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String objectUsingZeroNullFieldStrategyMessage;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Long objectAllowingShorthandInput;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String objectAllowingShorthandInputMessage;

	// Generally useful bits and pieces

	@Inject
	private Locale locale;

	@Inject
	private NumericTranslatorSupport numericTranslatorSupport;

	@Inject
	private Messages messages;

	@Component(id = "objectAllowingShorthandInput")
	private TextField objectAllowingShorthandInputField;

	// The code

	/* 1st field. */

	String onToClientFromPrimitiveWithZeroSuppressed() {
		if (primitiveWithZeroSuppressed == 0) {
			return "";
		}
		else {
			// Return control to the normal translator.
			return null;
		}
	}

	Object onParseClientFromPrimitiveWithZeroSuppressed(String input) {
		// We included this handler only to set the message. Return control to the normal translator.
		primitiveWithZeroSuppressedMessage = PARSED;
		return null;
	}

	/* 2nd field. */

	String onToClientFromObjectDisplayingNullAsZero() {
		if (objectDisplayingNullAsZero == null) {
			return "0";
		}
		else {
			// Return control to the normal translator.
			return null;
		}
	}

	Object onParseClientFromObjectDisplayingNullAsZero(String input) {
		// We included this handler only to set the message. Return control to the normal translator.
		objectDisplayingNullAsZeroMessage = PARSED;
		return null;
	}

	/* 3rd field. */

	Object onParseClientFromObjectUsingZeroNullFieldStrategy(String input) {
		// We included this handler only to set the message. Return control to the normal translator.
		objectUsingZeroNullFieldStrategyMessage = PARSED;
		return null;
	}

	/* 4th field. */

	Object onParseClientFromObjectAllowingShorthandInput(String input) throws ValidationException {
		objectAllowingShorthandInputMessage = PARSED;
		String trimmed = input.trim();

		// If the trimmed input has a suffix of "k", "K", "m", or "M", then replace it with 3 or 6 zeroes.

		if (trimmed.length() > 1) {
			String lastChar = trimmed.substring(trimmed.length() - 1);

			if (lastChar.equalsIgnoreCase("k")) {
				trimmed = trimmed.substring(0, trimmed.length() - 1) + "000";
			}
			else if (lastChar.equalsIgnoreCase("m")) {
				trimmed = trimmed.substring(0, trimmed.length() - 1) + "000000";
			}
		}

		try {

			// Convert to a canonical form, stripping out grouping separators and disallowing decimal separators.
			// We can't leave this to NumericTranslatorSupport because it uses Java's NumberFormat.parse(String) which
			// is very lenient.

			String canonical = toCanonical(trimmed, true);

			Long l = numericTranslatorSupport.parseClient(Long.class, canonical);
			return l;
		}
		catch (ParseException e) {
			String message = messages.format(numericTranslatorSupport.getMessageKey(Long.class),
					objectAllowingShorthandInputField.getLabel());
			throw new ValidationException(message);
		}
	}

	/**
	 * This is the same pre-processing that Tapestry's client-side translator does (in tapestry.js).
	 */
	private String toCanonical(String s, boolean anInteger) throws ParseException {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);

		char minusSign = symbols.getMinusSign();
		char groupingSeparator = symbols.getGroupingSeparator();
		char decimalSeparator = symbols.getDecimalSeparator();

		// Convert non-breaking space to space. Necessary for French and other locales.
		if ((int) groupingSeparator == 160) {
			groupingSeparator = ' ';
		}

		StringBuilder canonical = new StringBuilder("");

		for (char ch : s.toCharArray()) {

			if (ch == minusSign) {
				canonical.append("-");
			}
			else if (ch == groupingSeparator) {
				continue;
			}
			else if (ch == decimalSeparator) {
				if (anInteger) {
					throw new ParseException("Integer contains a decimal separator.", -1);
				}
				canonical.append(".");
			}
			else if (ch >= '0' && ch <= '9') {
				canonical.append(ch);
			}
			else {
				// System.out.println("ch = " + (int) ch + ", groupingSeparator = " + (int) groupingSeparator + ".");
				throw new ParseException(
						"Contains character other than digit, minus sign, grouping separator, or decimal separator", -1);
			}

		}

		return canonical.toString();
	}

}
