/*
 * Created on Jul 4, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jumpstart.util.properties;

/**
 * @author geocal
 * 
 *         To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code
 *         Generation&gt;Code and Comments
 */
public class PropertyMissingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String propertyName;
	private String resourceName;

	PropertyMissingException(String propertyName, String resourceName) {
		this.propertyName = propertyName;
		this.resourceName = resourceName;
	}

	@Override
	public String getMessage() {
		return "Property \"" + propertyName + "\" is missing from resource \"" + resourceName + "\".";
	}
}
