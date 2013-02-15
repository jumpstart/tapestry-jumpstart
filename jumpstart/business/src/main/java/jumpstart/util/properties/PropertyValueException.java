package jumpstart.util.properties;

public class PropertyValueException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final int ALLOWED_VALUE_TYPE_DISCRETE = 1;
	private static final int ALLOWED_VALUE_TYPE_RANGE = 2;
	private static final int ALLOWED_VALUE_TYPE_DESCRIBED = 3;

	private String propertyName;
	private String propertyValue;
	private String resourceName;

	private int allowedValueType;
	private String[] allowedValues;
	private String fromValue;
	private String toValue;
	private String description;

	PropertyValueException(String propertyName, String resourceName, String propertyValue, String[] allowedValues) {
		this.propertyName = propertyName;
		this.resourceName = resourceName;

		this.propertyValue = propertyValue;
		this.allowedValueType = ALLOWED_VALUE_TYPE_DISCRETE;
		this.allowedValues = allowedValues;
	}

	PropertyValueException(String propertyName, String resourceName, String propertyValue, String fromValue,
			String toValue) {
		this.propertyName = propertyName;
		this.resourceName = resourceName;

		this.propertyValue = propertyValue;
		this.allowedValueType = ALLOWED_VALUE_TYPE_RANGE;
		this.fromValue = fromValue;
		this.toValue = toValue;
	}

	PropertyValueException(String propertyName, String resourceName, String propertyValue, String description) {
		this.propertyName = propertyName;
		this.resourceName = resourceName;

		this.propertyValue = propertyValue;
		this.allowedValueType = ALLOWED_VALUE_TYPE_DESCRIBED;
		this.description = description;
	}

	@Override
	public String getMessage() {
		switch (allowedValueType) {

		case ALLOWED_VALUE_TYPE_DISCRETE:
			String s = "{ ";
			for (int i = 0; i < allowedValues.length; i++) {
				if (i > 0) {
					s += ", ";
				}
				s += "\"" + allowedValues[i] + "\"";
			}
			s += " }";
			return "Property \"" + propertyName + "\" in resource \"" + resourceName + "\" has invalid value \""
					+ propertyValue + "\". Allowed values are " + s + ".";

		case ALLOWED_VALUE_TYPE_RANGE:
			return "Property \"" + propertyName + "\" in resource \"" + resourceName + "\" has invalid value \""
					+ propertyValue + "\". Allowed values is from " + fromValue + " to " + toValue + ".";

		case ALLOWED_VALUE_TYPE_DESCRIBED:
			return "Property \"" + propertyName + "\" in resource \"" + resourceName + "\" has invalid value \""
					+ propertyValue + "\". Allowed values is " + description + ".";

		default:
			throw new IllegalStateException("Should never reach here.");
		}
	}
}
