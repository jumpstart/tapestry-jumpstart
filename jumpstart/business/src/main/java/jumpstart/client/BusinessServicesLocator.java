package jumpstart.client;

import java.util.regex.Pattern;

import jumpstart.business.commons.jndi.JNDIObjectLocator;
import jumpstart.util.EJBProviderEnum;
import jumpstart.util.EJBProviderUtil;

import org.slf4j.Logger;

/**
 * BusinessServicesLocator is used to centralize all lookups of business services in JNDI. At the time of writing it is
 * used by the business tier's BaseTest and the web tier's EJBAnnotationWorker and PageProtectionFilter.
 * 
 * This version knows the formats of JNDI names assigned by OpenEJB, GlassFish, and JBoss (the EJB 3.1 specification
 * only "almost" standardized them). It minimises the overhead of JNDI lookups by caching the objects it looks up. If
 * this class becomes a bottleneck, then you may need to decentralise it.
 */
public class BusinessServicesLocator extends JNDIObjectLocator implements IBusinessServicesLocator {
	private static final String REMOTE = "REMOTE";
	private static final String LOCAL = "LOCAL";
	private static final Pattern StripLocalPattern = Pattern.compile(LOCAL + "|" + REMOTE, Pattern.CASE_INSENSITIVE);

	private final EJBProviderEnum ejbProvider;

	public BusinessServicesLocator(Logger logger) {
		super(logger);
		// You wouldn't normally have to do this but JumpStart has to deal with many types of environment...
		this.ejbProvider = EJBProviderUtil.detectEJBProvider(logger);
	}

	/**
	 * An example of interfaceClass is IPersonServiceLocal.
	 */
	@Override
	public Object getService(Class<?> interfaceClass) {
		return getService(interfaceClass.getCanonicalName());
	}

	/**
	 * We expect canonicalInterfaceName to be like "jumpstart.business.domain.examples.iface.IPersonServiceLocal", ie.
	 * its simple name has a leading "I" and trailing "Local" or "Remote".
	 */
	@Override
	public Object getService(String canonicalInterfaceName) {
		String jndiName = null;

		// You wouldn't normally have to do all this work but JumpStart has to deal with many types of environment and
		// EJB 3.1 still hasn't quite standardised JNDI names.

		if (ejbProvider == EJBProviderEnum.OPENEJB_4_LOCAL || ejbProvider == EJBProviderEnum.TOMCAT_7_OPENEJB_4_LOCAL
				|| ejbProvider == EJBProviderEnum.OPENEJB_4_REMOTE) {
			// Uses the implementation class name eg. "PersonServiceLocal".
			jndiName = getSimpleName(canonicalInterfaceName).substring(1);
		}
		else if (ejbProvider == EJBProviderEnum.GLASSFISH_3_LOCAL || ejbProvider == EJBProviderEnum.GLASSFISH_3_REMOTE) {
			// Use the EJB3.1 global name eg.
			// "java:global/jumpstart/jumpstartejb/PersonService!jumpstart.business.domain.examples.iface.IPersonServiceLocal".
			jndiName = "java:global/jumpstart/jumpstartejb/"
					+ stripOffLocalOrRemote(getSimpleName(canonicalInterfaceName).substring(1)) + "!"
					+ canonicalInterfaceName;
		}
		else if (ejbProvider == EJBProviderEnum.JBOSS_7_LOCAL) {
			// Uses the web app name, and other bits, eg. "jumpstart/PersonService/local".
			jndiName = "jumpstart/" + stripOffLocalOrRemote(getSimpleName(canonicalInterfaceName).substring(1))
					+ "/local";
		}
		else if (ejbProvider == EJBProviderEnum.JBOSS_7_REMOTE) {
			// Uses an odd format - similar to the EJB3.1 global name, eg.
			// "ejb/jumpstart/jumpstart.jar/PersonService!jumpstart.business.domain.examples.iface.IPersonServiceRemote".
			jndiName = "ejb:jumpstart/jumpstart.jar/"
					+ stripOffLocalOrRemote(getSimpleName(canonicalInterfaceName).substring(1)) + "!"
					+ canonicalInterfaceName;
		}
		else {
			throw new IllegalStateException("Don't know how to use ejbProvider = " + ejbProvider);
		}

		return getJNDIObject(jndiName);
	}

	private static String getSimpleName(String s) {
		return s.substring(s.lastIndexOf(".") + 1);
	}

	private static String stripOffLocalOrRemote(String s) {
		String stripped = s;
		String uc = s.toUpperCase();

		if (uc.endsWith(LOCAL) || uc.endsWith(REMOTE)) {
			stripped = StripLocalPattern.matcher(s).replaceFirst("");
		}

		return stripped;
	}
}
