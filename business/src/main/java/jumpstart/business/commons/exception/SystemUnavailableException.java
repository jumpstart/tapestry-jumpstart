package jumpstart.business.commons.exception;

import jumpstart.business.MessageUtil;

@SuppressWarnings("serial")
public class SystemUnavailableException extends SystemException {
	String symptom;

	/**
	 * Throw this exception when the system becomes unavailable eg. due to database connection failure.
	 */
	public SystemUnavailableException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Throw this exception when the system becomes unavailable eg. due to database connection failure.
	 */
	public SystemUnavailableException(String symptom, Throwable throwable) {
		super(throwable);
		this.symptom = symptom;
	}

	@Override
	public String getMessage() {
		String msg = MessageUtil.toText("SystemUnavailableException", symptom);
		return msg;
	}

	public String getSymptom() {
		return symptom;
	}

}
