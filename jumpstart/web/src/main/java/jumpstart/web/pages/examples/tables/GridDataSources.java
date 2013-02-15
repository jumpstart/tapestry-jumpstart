package jumpstart.web.pages.examples.tables;

import javax.ejb.EJB;

import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.web.model.examples.tables.PersonPagedDataSource;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;

public class GridDataSources {

	// Screen fields

	@Property
	private GridDataSource persons;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@InjectComponent
	private Grid grid;

	// The code

	void setupRender() {
		persons = new PersonPagedDataSource(personFinderService);

		if (grid.getSortModel().getSortConstraints().isEmpty()) {
			grid.getSortModel().updateSort("firstName");
		}
	}

}
