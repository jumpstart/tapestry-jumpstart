/*
 * Created on May 9, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jumpstart.util.properties;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesLoader {
	public static final Logger LOGGER = LoggerFactory.getLogger(PropertiesLoader.class);

	private String resourceName;
	private URL resourceURL;
	private Properties properties;
	InputStream inputStream;

	public PropertiesLoader(String resourceName) {
		this.resourceName = resourceName;
		this.resourceURL = null;
		this.properties = new Properties();

		// try classpath
		this.inputStream = this.getClass().getResourceAsStream("/" + resourceName);

		if (inputStream != null) {
			resourceURL = this.getClass().getResource("/" + resourceName);
		}
		else {
			// try local
			inputStream = this.getClass().getResourceAsStream(resourceName);

			if (inputStream != null) {
				resourceURL = this.getClass().getResource(resourceName);
			}
			else {
				throw new IllegalStateException("Failed to load properties resource \"" + resourceName
						+ "\". It should be on the classpath.  If using JBoss, put the \"" + resourceName
						+ "\" file in the server's conf/ directory.");
			}
		}
		try {
			properties.load(inputStream);
		}
		catch (java.io.IOException e) {
			e.printStackTrace();
			throw new IllegalStateException("Failed to load properties resource \"" + resourceName
					+ "\". It should be on the classpath.  If using JBoss, put the \"" + resourceName
					+ "\" file in the server's conf/ directory.", e);
		}
	}

	public Properties getProperties() {
		return properties;
	}

	public String getOptionalProperty(String propertyName) {
		String propertyValue = properties.getProperty(propertyName);
		return propertyValue;
	}

	public String getMandatoryProperty(String propertyName) {
		String propertyValue = getOptionalStringProperty(propertyName);

		if (propertyValue == null) {
			throw new PropertyMissingException(propertyName, resourceName);
		}

		if (propertyValue.trim().equals("")) {
			throw new PropertyEmptyException(propertyName, resourceName);
		}

		return propertyValue;
	}

	public String getOptionalStringProperty(String propertyName) {
		return getOptionalProperty(propertyName);
	}

	public String getMandatoryStringProperty(String propertyName) {
		return getMandatoryProperty(propertyName);
	}

	public String getMandatoryStringProperty(String propertyName, String[] allowableValues) {
		String propertyValue = getMandatoryProperty(propertyName);

		boolean valid = false;
		for (int i = 0; i < allowableValues.length; i++) {
			if (propertyValue.equals(allowableValues[i])) {
				valid = true;
				break;
			}
		}

		if (!valid) {
			throw new PropertyValueException(propertyName, this.resourceName, propertyValue, allowableValues);
		}

		return propertyValue;
	}

	public Boolean getOptionalBooleanProperty(String propertyName) {
		String propertyValue = getOptionalProperty(propertyName);

		if (propertyValue == null) {
			return null;
		}
		else if (propertyValue.equals("true")) {
			return true;
		}
		else if (propertyValue.equals("false")) {
			return false;
		}
		else {
			throw new PropertyValueException(propertyName, this.resourceName, propertyValue, new String[] { "", "true",
					"false" });
		}
	}

	public boolean getMandatoryBooleanProperty(String propertyName) {
		String propertyValue = getMandatoryProperty(propertyName);

		if (propertyValue.equals("true")) {
			return true;
		}
		else if (propertyValue.equals("false")) {
			return false;
		}
		else {
			throw new PropertyValueException(propertyName, this.resourceName, propertyValue, new String[] { "true",
					"false" });
		}
	}

	public int getMandatoryIntProperty(String propertyName) {
		String propertyValue = getMandatoryProperty(propertyName);

		try {
			int i = Integer.parseInt(propertyValue);
			return i;
		}
		catch (NumberFormatException e) {
			throw new PropertyValueException(propertyName, this.resourceName, propertyValue, " an integer.");
		}
	}

	public void store(String headers) throws IOException {
		try {
			FileOutputStream out = new FileOutputStream(resourceURL.getFile());
			properties.store(out, headers);
		}
		catch (IOException e) {
			LOGGER.error("Could not store properties to resource \"" + resourceURL + "\" because: " + e);
			throw e;
		}
	}

	public Object setProperty(String key, String value) {
		return properties.setProperty(key, value);
	}

	public Object setProperty(String key, int value) {
		final DecimalFormat df = new DecimalFormat();
		return properties.setProperty(key, df.format(value));
	}

}
