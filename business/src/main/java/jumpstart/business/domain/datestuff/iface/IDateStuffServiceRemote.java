package jumpstart.business.domain.datestuff.iface;

import jumpstart.business.domain.datestuff.DatesExample;

/**
 * The <code>IDateStuffServiceRemote</code> bean exposes the business methods in the interface.
 */
public interface IDateStuffServiceRemote {

	// DatesExample

	DatesExample findDatesExample(Long id);

	DatesExample changeDatesExample(DatesExample datesExample);

}
