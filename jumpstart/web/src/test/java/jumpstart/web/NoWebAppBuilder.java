package jumpstart.web;

import java.util.Map;
import java.util.Set;

import org.apache.openejb.assembler.classic.AppInfo;
import org.apache.openejb.assembler.classic.WebAppBuilder;

/**
 * This disables OpenEJB's http facilities. They were introduced in OpenEJB 4.6. They are a nuisance to us because they
 * compete with Jetty, causing services such as our ServerContextListeners to be started twice - once by OpenEJB and
 * once by Jetty.
 * <p/>
 * To enable this, set -Dorg.apache.openejb.assembler.classic.WebAppBuilder=com.goxpro.xpro.web.NoWebAppBuilder
 * <p/>
 * An alternative to consider is to run TomEE instead of Jetty and OpenEJB.
 */
public class NoWebAppBuilder implements WebAppBuilder {

	@Override
	public void deployWebApps(AppInfo arg0, ClassLoader arg1) throws Exception {
		// No-op.
	}

	@Override
	public Map<ClassLoader, Map<String, Set<String>>> getJsfClasses() {
		// No-op.
		return null;
	}

	@Override
	public void undeployWebApps(AppInfo arg0) throws Exception {
		// No-op.
	}
}
