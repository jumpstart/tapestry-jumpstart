package jumpstart.web.model.together;

import java.util.ArrayList;
import java.util.List;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceRemote;
import jumpstart.util.query.SortCriterion;
import jumpstart.util.query.SortDirection;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

public class PersonPagedDataSource implements GridDataSource {
	private IPersonFinderServiceRemote personFinderService;

	private int startIndex;
	private List<Person> preparedResults;

	public PersonPagedDataSource(IPersonFinderServiceRemote personFinderService) {
		this.personFinderService = personFinderService;
	}

	@Override
	public int getAvailableRows() {
		return (int) personFinderService.countPersons();
	}

	@Override
	public void prepare(final int startIndex, final int endIndex, final List<SortConstraint> sortConstraints) {
		List<SortCriterion> sortCriteria = new ArrayList<SortCriterion>();
		sortCriteria.add(new SortCriterion("firstName", SortDirection.ASCENDING));
		sortCriteria.add(new SortCriterion("lastName", SortDirection.ASCENDING));
		preparedResults = personFinderService.findPersons(startIndex, endIndex - startIndex + 1, sortCriteria);

		this.startIndex = startIndex;
	}

	@Override
	public Object getRowValue(final int index) {
		return preparedResults.get(index - startIndex);
	}

	@Override
	public Class<Person> getRowType() {
		return Person.class;
	}

}
