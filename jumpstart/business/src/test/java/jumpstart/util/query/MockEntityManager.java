package jumpstart.util.query;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

public class MockEntityManager implements EntityManager {

	public MockEntityManager() {
		super();
	}

	@Override
	public void persist(Object arg0) {
	}

	@Override
	public <T> T merge(T arg0) {
		return null;
	}

	@Override
	public void remove(Object arg0) {
	}

	@Override
	public <T> T find(Class<T> arg0, Object arg1) {
		return null;
	}

	@Override
	public <T> T getReference(Class<T> arg0, Object arg1) {
		return null;
	}

	@Override
	public void flush() {
	}

	@Override
	public void setFlushMode(FlushModeType arg0) {
	}

	@Override
	public FlushModeType getFlushMode() {
		return null;
	}

	@Override
	public void lock(Object arg0, LockModeType arg1) {
	}

	@Override
	public void refresh(Object arg0) {
	}

	@Override
	public void clear() {
	}

	@Override
	public boolean contains(Object arg0) {
		return false;
	}

	@Override
	public Query createQuery(String arg0) {
		return new MockQuery();
	}

	@Override
	public Query createNamedQuery(String arg0) {
		return new MockQuery();
	}

	@Override
	public Query createNativeQuery(String arg0) {
		return new MockQuery();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Query createNativeQuery(String arg0, Class arg1) {
		return new MockQuery();
	}

	@Override
	public Query createNativeQuery(String arg0, String arg1) {
		return new MockQuery();
	}

	@Override
	public void joinTransaction() {
	}

	@Override
	public Object getDelegate() {
		return null;
	}

	@Override
	public void close() {
	}

	@Override
	public boolean isOpen() {
		return false;
	}

	@Override
	public EntityTransaction getTransaction() {
		return null;
	}

	@Override
	public <T> TypedQuery<T> createNamedQuery(String arg0, Class<T> arg1) {
		return null;
	}

	@Override
	public <T> TypedQuery<T> createQuery(CriteriaQuery<T> arg0) {
		return null;
	}

	@Override
	public <T> TypedQuery<T> createQuery(String arg0, Class<T> arg1) {
		return null;
	}

	@Override
	public void detach(Object arg0) {
	}

	@Override
	public <T> T find(Class<T> arg0, Object arg1, Map<String, Object> arg2) {
		return null;
	}

	@Override
	public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2) {
		return null;
	}

	@Override
	public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2, Map<String, Object> arg3) {
		return null;
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return null;
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return null;
	}

	@Override
	public LockModeType getLockMode(Object arg0) {
		return null;
	}

	@Override
	public Metamodel getMetamodel() {
		return null;
	}

	@Override
	public Map<String, Object> getProperties() {
		return null;
	}

	@Override
	public void lock(Object arg0, LockModeType arg1, Map<String, Object> arg2) {
	}

	@Override
	public void refresh(Object arg0, Map<String, Object> arg1) {
	}

	@Override
	public void refresh(Object arg0, LockModeType arg1) {
	}

	@Override
	public void refresh(Object arg0, LockModeType arg1, Map<String, Object> arg2) {
	}

	@Override
	public void setProperty(String arg0, Object arg1) {
	}

	@Override
	public <T> T unwrap(Class<T> arg0) {
		return null;
	}
}
