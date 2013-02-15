package jumpstart.business.commons.exception;

import javax.ejb.ApplicationException;

import jumpstart.business.MessageUtil;
import jumpstart.business.domain.base.BaseEntity;
import jumpstart.util.ClassUtil;

@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class ValueRequiredException extends BusinessException {
	private String entityLabelMessageId;
	private String fieldLabelMessageId;

	/**
	 * Throw this exception from an entity that has a required property that has not been given a value (eg. null, empty
	 * or 0).
	 * 
	 * @param entity the entity being set.
	 * @param fieldLabelMessageId the key of a message that represents the field that has not been given a value.
	 */
	public ValueRequiredException(BaseEntity entity, String fieldLabelMessageId) {

		// Don't convert the message ids to messages yet because we're in the server's locale, not the user's.

		super();
		this.entityLabelMessageId = ClassUtil.extractUnqualifiedName(entity);
		this.fieldLabelMessageId = fieldLabelMessageId;
	}

	@Override
	public String getMessage() {

		// We deferred converting the message ids to messages until now, when we are more likely to be in the user's
		// locale.

		Object[] msgArgs = new Object[] { MessageUtil.toText(entityLabelMessageId),
				MessageUtil.toText(fieldLabelMessageId) };

		String msg = MessageUtil.toText("ValueRequiredException", msgArgs);
		return msg;
	}

	public String getEntityLabelMessageId() {
		return entityLabelMessageId;
	}

	public String getFieldLabelMessageId() {
		return fieldLabelMessageId;
	}
}
