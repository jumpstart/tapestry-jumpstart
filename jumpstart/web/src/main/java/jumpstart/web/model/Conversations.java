package jumpstart.web.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Conversations {

	private Map<String, Integer> counters = new HashMap<String, Integer>();
	private Map<String, Conversation> conversations = new HashMap<String, Conversation>();

	public String startConversation() {
		return startConversation("dEfAuLt");
	}

	public synchronized String startConversation(String conversationIdPrefix) {
		int conversationNumber = incrementCounter(conversationIdPrefix);
		String conversationId = conversationIdPrefix + Integer.toString(conversationNumber);

		startConversationForId(conversationId);

		return conversationId;
	}

	public synchronized void startConversationForId(String conversationId) {
		Conversation conversation = new Conversation(conversationId);
		add(conversation);
	}

	public void saveToConversation(String conversationId, Object key, Object value) {
		Conversation conversation = get(conversationId);
		// Save a new reference to the object, just in case Tapestry cleans up the other one as we leave the page.
		Object valueNewRef = value;
		conversation.setObject(key, valueNewRef);
	}

	public Object restoreFromConversation(String conversationId, Object key) {
		Conversation conversation = get(conversationId);
		return conversation == null ? null : conversation.getObject(key);
	}

	public void endConversation(String conversationId) {
		remove(conversationId);
	}

	public Collection<Conversation> getAll() {
		return conversations.values();
	}

	public boolean isEmpty() {
		return conversations.isEmpty();
	}

	private synchronized void add(Conversation conversation) {
		if (conversations.containsKey(conversation.getId())) {
			throw new IllegalArgumentException("Conversation already exists. conversationId = " + conversation.getId());
		}
		conversations.put(conversation.getId(), conversation);
	}

	public Conversation get(String conversationId) {
		return conversations.get(conversationId);
	}

	private void remove(String conversationId) {
		Object obj = conversations.remove(conversationId);
		if (obj == null) {
			throw new IllegalArgumentException("Conversation did not exist. conversationId = " + conversationId);
		}
	}

	public synchronized int incrementCounter(String counterKey) {

		if (counters == null) {
			counters = new HashMap<String, Integer>(2);
		}

		Integer counterValue = counters.get(counterKey);

		if (counterValue == null) {
			counterValue = 1;
		}
		else {
			counterValue++;
		}

		counters.put(counterKey, counterValue);
		return counterValue;
	}

	public String toString() {
		final String DIVIDER = ", ";

		StringBuilder buf = new StringBuilder();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[ ");
		buf.append("counters=");
		if (counters == null) {
			buf.append("null");
		}
		else {
			buf.append("{");
			for (Iterator<String> iterator = counters.keySet().iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				buf.append("(" + key + ", " + counters.get(key) + ")");
			}
			buf.append("}");
		}
		buf.append(DIVIDER);
		buf.append("conversations=");
		if (conversations == null) {
			buf.append("null");
		}
		else {
			buf.append("{");
			for (Iterator<String> iterator = conversations.keySet().iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				buf.append("(" + key + ", " + conversations.get(key) + ")");
			}
			buf.append("}");
		}
		buf.append("]");
		return buf.toString();
	}

}
