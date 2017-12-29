package jumpstart.client;

/**
 * BusinessServicesLocator is used to centralize all lookups of business services in JNDI.
 */
public interface IBusinessServicesLocator {

	/**
	 * @param interfaceClass eg. IPersonServiceLocal.class .
	 * @return eg. An instance of IPersonServiceLocal as found by JNDI.
	 */
	public abstract Object getService(Class<?> interfaceClass);

	/**
	 * @param canonicalInterfaceName eg. "jumpstart.business.domain.examples.iface.IPersonServiceLocal".
	 * @return eg. An instance of IPersonServiceLocal as found by JNDI.
	 */
	public abstract Object getService(String canonicalInterfaceName);

	/**
	 * Invoked after any kind of naming or remote exception. All cached naming contexts and interfaces are discarded.
	 */
	public abstract void clear();

}
