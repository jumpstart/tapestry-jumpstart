package jumpstart.util;

import org.slf4j.Logger;

public class EJBProviderUtil {
	private static String PROPERTY_EJB_PROVIDER = "jumpstart.ejb-provider";

	public static EJBProviderEnum detectEJBProvider(Logger logger) {
		EJBProviderEnum ejbProvider = null;

		String ejbProviderStr = null;

		try {
			ejbProviderStr = System.getProperty(PROPERTY_EJB_PROVIDER);

			if (ejbProviderStr == null) {
				throw new IllegalStateException("System property \"" + PROPERTY_EJB_PROVIDER
						+ "\" not found. Please set it to one of: {" + getAllowedValuesAsStr() + "}.");
			}

			ejbProvider = EJBProviderEnum.valueOf(ejbProviderStr);
		}
		catch (IllegalStateException e) {
			throw e;
		}
		catch (SecurityException e) {
			throw new IllegalStateException("Failed to get system property \"" + PROPERTY_EJB_PROVIDER + "\": " + e);
		}
		catch (Exception e) {
			throw new IllegalStateException("Found system property \"" + PROPERTY_EJB_PROVIDER + "\" equals \""
					+ ejbProviderStr + "\", expected one of: {" + getAllowedValuesAsStr() + "}.");
		}

		return ejbProvider;
	}

	private static String getAllowedValuesAsStr() {
		String valuesStr = "";

		EJBProviderEnum[] values = EJBProviderEnum.values();

		for (EJBProviderEnum ejbProviderEnum : values) {
			valuesStr += "\"" + ejbProviderEnum.name() + "\", ";
		}

		return valuesStr.substring(0, valuesStr.lastIndexOf(","));
	}

}
