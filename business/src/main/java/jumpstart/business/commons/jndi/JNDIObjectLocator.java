package jumpstart.business.commons.jndi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import jumpstart.business.commons.exception.SystemUnavailableException;
import jumpstart.util.EJBProviderEnum;
import jumpstart.util.EJBProviderUtil;

import org.slf4j.Logger;

/**
 * JNDIObjectLocator is used to centralize all JNDI lookups. It minimises the overhead of JNDI lookups by caching the
 * objects it looks up.
 */
public class JNDIObjectLocator {
	protected Logger logger;
	private InitialContext initialContext;
	private Map<String, Object> jndiObjectCache = Collections.synchronizedMap(new HashMap<String, Object>());

	public JNDIObjectLocator(Logger logger) {
		this.logger = logger;
	}

	public synchronized void clear() {
		jndiObjectCache.clear();
	}

	public Object getJNDIObject(String jndiName) {

		Object jndiObject = jndiObjectCache.get(jndiName);

		if (jndiObject == null && !jndiObjectCache.containsKey(jndiName)) {
			try {
				jndiObject = lookup(jndiName);
				jndiObjectCache.put(jndiName, jndiObject);
			}
			catch (RuntimeException e) {
				clear();
				throw e;
			}
		}
		return jndiObject;
	}

	private synchronized Object lookup(String name) {

		// Recheck the cache because the name we're looking for may have been added while we were waiting for sync.

		if (!jndiObjectCache.containsKey(name)) {
			try {
				return getInitialContext().lookup(name);
			}
			catch (NameNotFoundException e) {
				clear();
				throw new SystemUnavailableException("JNDI lookup failed for \"" + name
						+ "\".  Is ejb server not started? Has the ejb.provider property been specified correctly", e);
			}
			catch (NamingException e) {
				clear();
				throw new SystemUnavailableException(
						"JNDI lookup failed for \""
								+ name
								+ "\".  Is ejb server not started?  If using jboss, is jbossall-client.jar missing from classpath?"
								+ " Error looking up " + e.getRemainingName() + " because of " + e.getCause()
								+ " while " + e.getExplanation(), e);
			}
		}
		else {
			return jndiObjectCache.get(name);
		}
	}

	private synchronized InitialContext getInitialContext() {
		if (initialContext == null) {

			try {
				EJBProviderEnum ejbProvider = EJBProviderUtil.detectEJBProvider(logger);

				initialContext = new InitialContext();

				// Glassfish 3.1.1 can't list context - see http://java.net/jira/browse/GLASSFISH-17220
				if (ejbProvider != EJBProviderEnum.GLASSFISH_3_REMOTE) {
					logger.info("InitialContext environment = " + initialContext.getEnvironment());
					logger.info("InitialContext contains:");
					listContext("   ", initialContext);
				}
			}
			catch (NamingException e) {
				clear();
				throw new SystemUnavailableException(
						"Cannot get initial context."
								+ " Is JNDI server not started?  If using jboss, is jbossall-client.jar missing from classpath?"
								+ " Error looking up " + e.getRemainingName() + " because of " + e.getCause()
								+ " while " + e.getExplanation(), e);
			}

		}

		return initialContext;
	}

	/**
	 * This is not essential, but it can be handy when things go wrong to have the objects in the context listed in the
	 * log.
	 * 
	 * @param s
	 * @param c
	 * @throws NamingException
	 */
	private final void listContext(String s, Context c) throws NamingException {
		NamingEnumeration<NameClassPair> pairs = c.list("");
		for (; pairs.hasMoreElements();) {
			NameClassPair p = pairs.next();
			logger.info(s + "/" + p.getName() + " : " + p.getClassName());

			try {
				Object o = c.lookup(p.getName());

				if (o instanceof Context) {
					Context child = (Context) o;
					listContext(s + "/" + p.getName(), child);
				}
			}
			catch (Throwable t) {
				// Not really a problem so just log it.
				logger.debug("      " + t.getClass().getName() + ": " + t.getMessage());
			}
		}
	}
}
