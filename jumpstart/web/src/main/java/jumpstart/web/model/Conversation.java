package jumpstart.web.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Conversation {
	private String id;
	private Map<Object, Object> objectsByKey = null;

	public Conversation(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setObject(Object key, Object obj) {
		if (objectsByKey == null) {
			objectsByKey = new HashMap<Object, Object>(1);
		}
		objectsByKey.put(key, obj);
	}

	public Object getObject(Object key) {
		if (objectsByKey == null) {
			return null;
		}
		else {
			return objectsByKey.get(key);
		}
	}

	public String toString() {
		final String DIVIDER = ", ";

		StringBuilder buf = new StringBuilder();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("id=" + id + DIVIDER);
		buf.append("objectsByKey=");
		if (objectsByKey == null) {
			buf.append("null");
		}
		else {
			buf.append("{");
			for (Iterator<Object> iterator = objectsByKey.keySet().iterator(); iterator.hasNext();) {
				Object key = (Object) iterator.next();
				buf.append("(" + key + "," + "<" + objectsByKey.get(key) == null ? "null" : objectsByKey.get(key).getClass()
						.getSimpleName()
						+ ">" + ")");
			}
			buf.append("}");
		}
		buf.append("]");
		return buf.toString();
	}
}
