package jumpstart.business.commons.exception;

import javax.ejb.ApplicationException;

import jumpstart.business.MessageUtil;

@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class GenericBusinessException extends BusinessException {
	String messageId;
	Object[] messageArgs;

	/**
	 * Throw this exception when a business rule is violated.
	 * For example, Employee could throw this exception if salary > department salary cap.
	 *
	 * @param	messageId	the message key of a message that describes the rule violation.
	 */
	public GenericBusinessException(String messageId) {
		this(messageId, new Object[0]);
	}

	/**
	 * Throw this exception when a business rule is violated.
	 * For example, Employee could throw this exception if salary > department salary cap.
	 *
	 * @param	messageId	the message key of a message that describes the rule violation.
	 * @param	messageArg	a message argument to be substituted into the message.
	 */
	public GenericBusinessException(String messageId, Object... messageArgs) {
		super();
		this.messageId = messageId;
		this.messageArgs = messageArgs;
	}

	@Override
	public String getMessage() {

		// We deferred converting the message ids to messages until now, when we are more likely to be in the user's
		// locale.

		String msg = MessageUtil.toText(messageId, messageArgs);
		return msg;
	}

	public Object[] getMessageArgs() {
		return messageArgs;
	}

	public String getMessageId() {
		return messageId;
	}

}
