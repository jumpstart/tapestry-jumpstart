package jumpstart.web.state.examples.wizard;

import java.io.Serializable;

// In the real world we'd typically make this a business domain entity 
//@Entity
@SuppressWarnings("serial")
public class CreditRequest implements Serializable {

	private int amount = 0;
	private String applicantName = "";
	private Status status = Status.INCOMPLETE;

	public enum Status {
		INCOMPLETE, COMPLETE
	}

	public String toString() {
		final String DIVIDER = ", ";

		StringBuilder buf = new StringBuilder();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("amount=" + amount + DIVIDER);
		buf.append("applicantName=" + applicantName + DIVIDER);
		buf.append("status=" + status);
		buf.append("]");
		return buf.toString();
	}

	public CreditRequest() {
	}

	public void validateAmountInfo() throws Exception {
		if (amount < 10 || amount > 9999) {
			throw new Exception("Amount must be between 10 and 9999.");
		}
	}

	public void validateApplicantInfo() throws Exception {
		if (applicantName == null || applicantName.trim().equals("")) {
			throw new Exception("Applicant name is required.");
		}
	}

	public void validate() throws Exception {
		validateAmountInfo();
		validateApplicantInfo();
	}

	public void complete() throws Exception {
		validate();
		status = Status.COMPLETE;
	}

	public Status getStatus() {
		return status;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

}
