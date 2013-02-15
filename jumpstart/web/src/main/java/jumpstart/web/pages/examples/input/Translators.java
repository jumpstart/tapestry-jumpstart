package jumpstart.web.pages.examples.input;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class Translators {

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Byte byteValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Short shortValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Integer integerValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Long longValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Float floatValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Double doubleValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private BigInteger bigIntegerValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private BigDecimal bigDecimalValue;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String stringValue;

}
