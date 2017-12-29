package jumpstart.business.commons.exception;

import java.io.Serializable;

import javax.ejb.ApplicationException;

import jumpstart.business.MessageUtil;
import jumpstart.business.domain.base.BaseEntity;
import jumpstart.util.ClassUtil;

@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class ReferenceNotMutableException extends BusinessException {
	private String entityLabelMessageId;
	private String fieldLabelMessageId;
	private Serializable referenceObjectId;
	private Serializable newReferencedObjectId;

	/**
	 * Throw this exception from an entity that references its containing entity when there is an attempt to change the
	 * reference. For example, a Room belongs to a Building and its reference to the Building must not change. It
	 * could
	 * <code>throw new ReferenceNotMutableException(this, "Room_building", current_building.getId(), new_building.getId())</code>
	 * 
	 * @param entity
	 *            the entity holding the set of objects, eg. a Building object.
	 * @param fieldLabelMessageId
	 *            the key of a message that describes the entity and reference, eg. "Room_building".
	 * @param referenceObjectId
	 *            the id of the entity it currently references.
	 * @param newReferencedObjectId
	 *            the id of the entity in the new reference.
	 */
	public ReferenceNotMutableException(BaseEntity entity, String fieldLabelMessageId, Serializable referenceObjectId,
			Serializable newReferencedObjectId) {

		// Don't convert the message ids to messages yet because we're in the server's locale, not the user's.

		super();
		this.entityLabelMessageId = ClassUtil.extractUnqualifiedName(entity);
		this.fieldLabelMessageId = fieldLabelMessageId;
		this.referenceObjectId = referenceObjectId;
		this.newReferencedObjectId = newReferencedObjectId;
	}

	@Override
	public String getMessage() {

		// We deferred converting the message ids to messages until now, when we are more likely to be in the user's
		// locale.

		Object[] msgArgs = new Object[] { MessageUtil.toText(entityLabelMessageId),
				MessageUtil.toText(fieldLabelMessageId), referenceObjectId, newReferencedObjectId };

		String msg = MessageUtil.toText("ReferenceNotMutableException", msgArgs);
		return msg;
	}

	public String getEntityLabelMessageId() {
		return entityLabelMessageId;
	}

	public String getFieldLabelMessageId() {
		return fieldLabelMessageId;
	}

	public Serializable getNewReferencedObjectId() {
		return newReferencedObjectId;
	}

	public Serializable getReferenceObjectId() {
		return referenceObjectId;
	}
}
