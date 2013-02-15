package jumpstart.business.domain.datestuff;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import jumpstart.util.JodaTimeUtil;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

/**
 * The DatesExample entity.
 */
@Entity
@SuppressWarnings("serial")
public class DatesExample implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id;

	@Version
	@Column(nullable = false)
	private Integer version;

	// Traditional java-style Date fields

	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date aTimestamp;

	@Temporal(TemporalType.DATE)
	private java.util.Date aDate;

	@Temporal(TemporalType.TIME)
	private java.util.Date aTime;

	// These fields are exposed as Joda Time types but persisted as standard Java types (Date, Timestamp, String, Integer).

	private java.sql.Timestamp aDateTime;

	private java.sql.Timestamp aDateTimeWithTZ;

	private String aDateTimeTZ;

	private java.sql.Date aDateMidnight;

	private java.sql.Date aDateMidnightWithTZ;

	private String aDateMidnightTZ;

	private java.sql.Timestamp aLocalDateTime;

	private java.sql.Date aLocalDate;

	private java.sql.Time aLocalTimeAsTime;

	private Integer aLocalTimeAsMillis;

	private String aLocalTimeAsString;

	public String toString() {
		final String DIVIDER = ", ";
		
		StringBuilder buf = new StringBuilder();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("id=" + id + DIVIDER);
		buf.append("aTimestamp=" + aTimestamp + DIVIDER);
		buf.append("aDate=" + aDate + DIVIDER);
		buf.append("aTime=" + aTime + DIVIDER);
		buf.append("aDateTime=" + aDateTime + DIVIDER);
		buf.append("aDateTimeWithTZ=" + aDateTimeWithTZ + DIVIDER);
		buf.append("aDateTimeTZ=" + aDateTimeTZ + DIVIDER);
		buf.append("aDateMidnight=" + aDateMidnight + DIVIDER);
		buf.append("aLocalDateTime=" + aLocalDateTime + DIVIDER);
		buf.append("aLocalDate=" + aLocalDate + DIVIDER);
		buf.append("aLocalTimeAsTime=" + aLocalTimeAsTime + DIVIDER);
		buf.append("aLocalTimeAsMillis=" + aLocalTimeAsMillis + DIVIDER);
		buf.append("aLocalTimeAsString=" + aLocalTimeAsString + DIVIDER);
		buf.append("version=" + version);
		buf.append("]");
		return buf.toString();
	}

	// The need for an equals() method is discussed at http://www.hibernate.org/109.html

	@Override
	public boolean equals(Object obj) {
		return (obj == this) || (obj instanceof DatesExample) && id != null && id.equals(((DatesExample) obj).getId());
	}

	// The need for a hashCode() method is discussed at http://www.hibernate.org/109.html

	@Override
	public int hashCode() {
		return id == null ? super.hashCode() : id.hashCode();
	}

	public Long getId() {
		return id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getADate() {
		return aDate;
	}

	public void setADate(Date aDate) {
		this.aDate = aDate;
	}

	public Date getATime() {
		return aTime;
	}

	public void setATime(Date aTime) {
		this.aTime = aTime;
	}

	public Date getATimestamp() {
		return aTimestamp;
	}

	public void setATimestamp(Date aTimestamp) {
		this.aTimestamp = aTimestamp;
	}

	public DateTime getADateTime() {
		return JodaTimeUtil.toDateTime(aDateTime);
	}

	public void setADateTime(DateTime dt) {
		this.aDateTime = JodaTimeUtil.toSQLTimestamp(dt);
	}

	public DateTime getADateTimeWithTZ() {
		return JodaTimeUtil.toDateTime(aDateTimeWithTZ, aDateTimeTZ);
	}

	public void setADateTimeWithTZ(DateTime dt) {
		this.aDateTimeWithTZ = JodaTimeUtil.toSQLTimestamp(dt);
		this.aDateTimeTZ = JodaTimeUtil.toTimeZoneID(dt);
	}

	public DateMidnight getADateMidnight() {
		return JodaTimeUtil.toDateMidnight(aDateMidnight);
	}

	public void setADateMidnight(DateMidnight dm) {
		this.aDateMidnight = JodaTimeUtil.toSQLDate(dm);
	}

	public DateMidnight getADateMidnightWithTZ() {
		return JodaTimeUtil.toDateMidnight(aDateMidnightWithTZ, aDateMidnightTZ);
	}

	public void setADateMidnightWithTZ(DateMidnight dm) {
		this.aDateMidnightWithTZ = JodaTimeUtil.toSQLDate(dm);
		this.aDateMidnightTZ = JodaTimeUtil.toTimeZoneID(dm);
	}

	public LocalDateTime getALocalDateTime() {
		return JodaTimeUtil.toLocalDateTime(aLocalDateTime);
	}

	public void setALocalDateTime(LocalDateTime ldt) {
		this.aLocalDateTime = JodaTimeUtil.toSQLTimestamp(ldt);
	}

	public LocalDate getALocalDate() {
		return JodaTimeUtil.toLocalDate(aLocalDate);
	}

	public void setALocalDate(LocalDate ld) {
		this.aLocalDate = JodaTimeUtil.toSQLDate(ld);
	}

	public LocalTime getALocalTimeAsTime() {
		return JodaTimeUtil.toLocalTime(aLocalTimeAsTime);
	}

	public void setALocalTimeAsTime(LocalTime lt) {
		this.aLocalTimeAsTime = JodaTimeUtil.toSQLTime(lt);
	}

	public LocalTime getALocalTimeAsMillis() {
		return JodaTimeUtil.toLocalTime(aLocalTimeAsMillis);
	}

	public void setALocalTimeAsMillis(LocalTime lt) {
		this.aLocalTimeAsMillis = JodaTimeUtil.toIntegerMillis(lt);
	}

	public LocalTime getALocalTimeAsString() {
		return JodaTimeUtil.toLocalTime(aLocalTimeAsString);
	}

	public void setALocalTimeAsString(LocalTime lt) {
		this.aLocalTimeAsString = JodaTimeUtil.toString(lt);
	}

}
