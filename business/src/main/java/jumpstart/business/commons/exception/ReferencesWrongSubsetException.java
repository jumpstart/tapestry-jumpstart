package jumpstart.business.commons.exception;

import javax.ejb.ApplicationException;

import jumpstart.business.MessageUtil;
import jumpstart.business.domain.base.BaseEntity;
import jumpstart.util.ClassUtil;

@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class ReferencesWrongSubsetException extends BusinessException {
	private String entityLabelMessageId;
	private String fieldLabelMessageId;
	private String referencedValueDescription;
	private String referencedEntityLabelMessageId;
	private String expectedSubsetDescription;
	private String foundSubsetDescription;

	/**
	 * Throw this exception when an entity property, such as Building.mainRoom, references another entity, such as a
	 * Room, that must belong to a particular subset, such as Room belonging to the same Building (ie. room.building
	 * must point to the right Building).
	 * 
	 * @param entity the entity, eg. a Building object.
	 * @param fieldLabelMessageId the key of a message that describes the entity and reference, eg. "Building_mainRoom".
	 * @param referencedValueDescription the value of the reference as a string, eg. mainRoom.getId().toString().
	 * @param referencedEntityLabelMessageId the key of a message that describes the entity controlling the subset , eg.
	 *        "Building".
	 * @param expectedSubsetDescription a description of the expected subset, eg. building.getId().toString().
	 * @param foundSubsetDescription a description of the found subset, eg. mainRoom.getBuilding().getId().toString().
	 */
	public ReferencesWrongSubsetException(BaseEntity entity, String fieldLabelMessageId,
			String referencedValueDescription, String referencedEntityLabelMessageId, String expectedSubsetDescription,
			String foundSubsetDescription) {

		// Don't convert the message ids to messages yet because we're in the server's locale, not the user's.

		super();
		this.entityLabelMessageId = ClassUtil.extractUnqualifiedName(entity);
		this.fieldLabelMessageId = fieldLabelMessageId;
		this.referencedValueDescription = referencedValueDescription;
		this.referencedEntityLabelMessageId = referencedEntityLabelMessageId;
		this.expectedSubsetDescription = expectedSubsetDescription;
		this.foundSubsetDescription = foundSubsetDescription;
	}

	@Override
	public String getMessage() {

		// We deferred converting the message ids to messages until now, when we are more likely to be in the user's
		// locale.

		Object[] msgArgs = new Object[] { MessageUtil.toText(entityLabelMessageId),
				MessageUtil.toText(fieldLabelMessageId), referencedValueDescription,
				MessageUtil.toText(referencedEntityLabelMessageId), expectedSubsetDescription, foundSubsetDescription };

		String msg = MessageUtil.toText("ReferencesWrongSubsetException", msgArgs);
		return msg;
	}

	public String getEntityLabelMessageId() {
		return entityLabelMessageId;
	}

	public String getExpectedSubsetDescription() {
		return expectedSubsetDescription;
	}

	public String getFieldLabelMessageId() {
		return fieldLabelMessageId;
	}

	public String getFoundSubsetDescription() {
		return foundSubsetDescription;
	}

	public String getReferencedEntityLabelMessageId() {
		return referencedEntityLabelMessageId;
	}

	public String getReferencedValueDescription() {
		return referencedValueDescription;
	}
}
