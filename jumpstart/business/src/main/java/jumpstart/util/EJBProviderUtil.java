package jumpstart.util;


import org.slf4j.Logger;

public class EJBProviderUtil {

	static public EJBProviderEnum detectEJBProvider(Logger logger) {
		logger.info("Looking for an EJB provider...");
		
		try {
			Class.forName("org.apache.tomcat.JarScanner");
			Class.forName("org.apache.openejb.loader.Loader");
			logger.info("...found TomCat 7 OpenEJB 4 local.");
			return EJBProviderEnum.TOMCAT_7_OPENEJB_4_LOCAL;
		} 
		catch (Exception e) {
		}

		try {
			Class.forName("org.apache.openejb.loader.Loader");
			logger.info("...found OpenEJB 4 local.");
			return EJBProviderEnum.OPENEJB_4_LOCAL;
		}
		catch (Exception e) {
		}

		try {
			Class.forName("org.apache.openejb.client.Client");
			logger.info("...found OpenEJB 4 remote.");
			return EJBProviderEnum.OPENEJB_4_REMOTE;
		}
		catch (Exception e) {
		}

		try {
			Class.forName("org.jboss.modules.Main");
			logger.info("...found JBoss 7 local.");
			return EJBProviderEnum.JBOSS_7_LOCAL;
		}
		catch (Exception e) {
		}

		try {
			Class.forName("org.jboss.ejb.client.remoting.ClientMapping");
			logger.info("...found JBoss 7 remote.");
			return EJBProviderEnum.JBOSS_7_REMOTE;
		}
		catch (Exception e) {
		}

		try {
			Class.forName("com.sun.enterprise.admin.cli.AsadminMain");
			logger.info("...found GlassFish 3 local.");
			return EJBProviderEnum.GLASSFISH_3_LOCAL;
		}
		catch (Exception e) {
		}

		try {
			Class.forName("org.glassfish.appclient.client.acc.agent.ACCAgentClassLoader");
			logger.info("...found GlassFish 3 remote.");
			return EJBProviderEnum.GLASSFISH_3_REMOTE;
		}
		catch (Exception e) {
		}

		throw new IllegalStateException(
				"Failed to detect a known EJBProvider. Tried OpenEJB, JBoss, and GlassFish.");
	}
}
