package jumpstart.web;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;

public class RunJetty {

	public static void main(String[] args) throws Exception {

		// Create and configure a Jetty web server.

		Server server = new Server();
		XmlConfiguration configuration = new XmlConfiguration(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("jetty.xml"));
		configuration.configure(server);

		List<Connector> connectors = new ArrayList<>();

		if (isUseAjp()) {
			// Connector ajpConnector = new Ajp13SocketConnector();
			// ajpConnector.setPort(Integer.getInteger("jetty.port", 8009));
			// connectors.add(ajpConnector);
			throw new UnsupportedOperationException("AJP is no longer recommended by Jetty.");
		}
		else {
			ServerConnector httpConnector = new ServerConnector(server);
			httpConnector.setPort(Integer.getInteger("jetty.port", 8080));
			connectors.add(httpConnector);

			// ServerConnector httpsConnector = new ServerConnector(server);
			// httpsConnector.setPort(Integer.getInteger("jetty.port", 8443));
			// httpsConnector.setDefaultProtocol("https");
			// connectors.add(httpsConnector);
		}

		server.setConnectors(connectors.toArray(new Connector[0]));

		// Describe our web app

		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setContextPath("/jumpstart");
		webAppContext.setWar("collapsed/jumpstart.war");
		// webAppContext.setClassLoader(Thread.currentThread().getContextClassLoader());
		webAppContext.setParentLoaderPriority(true);

		// Give the web app some JAAS security

		// JAASUserRealm userRealm = new JAASUserRealm("JumpStartRealm");
		// userRealm.setLoginModuleName("JDBCLoginModule");
		// webapp.getSecurityHandler().setUserRealm(userRealm);

		// Replace the web app's list of classes with one that excludes slf4j and jaas

		// webapp.setServerClasses(new String[] { "org.mortbay.jetty." });
		// webapp.setServerClasses(new String[] { "-org.mortbay.jetty.plus.jaas.", "org.mortbay.jetty." });
		// webAppContext.setServerClasses(new String[] { "-org.eclipse.jetty.plus.jaas.", "org.eclipse.jetty." });

		// Tell our Jetty web server to handle our web app

		server.setHandler(webAppContext);

		// Start the server then wait until it stops.

		server.start();
		server.join();
	}

	private static boolean isUseAjp() {
		final String useAjpStr = System.getProperty("jumpstart.jetty.use.ajp");
		return useAjpStr != null && useAjpStr.equals("true");
	}

}
