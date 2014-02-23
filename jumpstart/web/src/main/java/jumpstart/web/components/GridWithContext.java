package jumpstart.web.components;

import javax.inject.Inject;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.GridModel;
import org.apache.tapestry5.grid.GridSortModel;
import org.apache.tapestry5.internal.EmptyEventContext;
import org.apache.tapestry5.internal.services.ArrayEventContext;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.Environment;

/**
 * Behaves the same as Grid, plus it accepts a context parameter which it makes available to our modified GridPager.
 * This component achieves that by wrapping Grid, pushing the context into the environment stack. Our modified GridPager
 * will read that context and include it in the pager links that it generates.
 */
@SupportsInformalParameters
@Events({ GridWithContext.PAGING })
public class GridWithContext implements ClientElement, GridModel {

	public static final String PAGING = "Paging";

	/**
	 * The context for the GridPager links (optional parameter). This list of values will be converted into strings and
	 * included in the URI. The strings will be coerced back to whatever their values are and made available in the
	 * PAGING event bubbled up by our modified GridPager.
	 */
	@Parameter
	private Object[] context;

	// Generally useful bits and pieces

	@Component(publishParameters = "add, class, columnIndex, empty, encoder, exclude, inPlace, include, lean, model, overrides, pagerPosition, reorder, row, rowClass, rowIndex, rowsPerPage, sortModel, source, volatile", inheritInformalParameters = true)
	private Grid grid;

	@Inject
	private Environment environment;

	@Inject
	private TypeCoercer typeCoercer;

	// The code

	/**
	 * This beginRender() will execute before our inner Grid's beginRender(). It gives us the chance to change the
	 * environment first - let's push our context onto the environment stack.
	 */
	void beginRender(MarkupWriter writer) {
		EventContext gridContext = context == null ? new EmptyEventContext() : new ArrayEventContext(typeCoercer,
				context);
		environment.push(EventContext.class, gridContext);
	}

	/**
	 * This afterRender() will execute after our inner Grid's beginRender(). Let's undo what we did in beforeRender().
	 */
	void afterRender(MarkupWriter writer) {
		environment.pop(EventContext.class);
	}

	@Override
	public String getClientId() {
		return grid.getClientId();
	}

	@Override
	public BeanModel getDataModel() {
		return grid.getDataModel();
	}

	@Override
	public GridDataSource getDataSource() {
		return grid.getDataSource();
	}

	@Override
	public GridSortModel getSortModel() {
		return grid.getSortModel();
	}

}
