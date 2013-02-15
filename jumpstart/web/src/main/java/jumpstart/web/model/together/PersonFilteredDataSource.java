package jumpstart.web.model.together;

import java.util.List;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceRemote;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

public class PersonFilteredDataSource implements GridDataSource {
	private IPersonFinderServiceRemote personFinderService;
	private String partialName;

	private int startIndex;
	private List<Person> preparedResults;

	public PersonFilteredDataSource(IPersonFinderServiceRemote personFinderService, String partialName) {
		this.personFinderService = personFinderService;
		this.partialName = partialName;
	}

	@Override
	public int getAvailableRows() {
		return (int) personFinderService.countPersons(partialName);
	}

	@Override
	public void prepare(final int startIndex, final int endIndex, final List<SortConstraint> sortConstraints) {
		preparedResults = personFinderService.findPersons(partialName, startIndex, endIndex - startIndex + 1);
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
