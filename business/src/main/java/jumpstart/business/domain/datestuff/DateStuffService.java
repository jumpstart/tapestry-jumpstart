package jumpstart.business.domain.datestuff;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jumpstart.business.domain.datestuff.iface.IDateStuffServiceLocal;
import jumpstart.business.domain.datestuff.iface.IDateStuffServiceRemote;

@Stateless
@Local(IDateStuffServiceLocal.class)
@Remote(IDateStuffServiceRemote.class)
public class DateStuffService implements IDateStuffServiceLocal, IDateStuffServiceRemote {

	@PersistenceContext(unitName = "jumpstart")
	private EntityManager em;

	public DatesExample findDatesExample(Long id) {
		return em.find(DatesExample.class, id);
	}

	public DatesExample changeDatesExample(DatesExample datesExample) {
		return em.merge(datesExample);
	}

}
