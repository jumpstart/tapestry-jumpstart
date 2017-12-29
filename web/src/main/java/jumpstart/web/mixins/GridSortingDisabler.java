//
// Based on an example by Steve Eynon posted in http://tapestry.1045711.n5.nabble.com/Grid-disable-sorting-mixin-td3401410.html .
//
package jumpstart.web.mixins;

import java.util.List;

import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.corelib.components.Grid;

@MixinAfter
public class GridSortingDisabler {

	@InjectContainer
	private Grid grid;

	void setupRender() {
		if (grid.getDataSource().getAvailableRows() == 0) {
			return;
		}

		BeanModel<?> gridBeanModel = grid.getDataModel();
		List<String> propertyNames = gridBeanModel.getPropertyNames();
		
		for (String propertyName : propertyNames) {
			PropertyModel propertyModel = gridBeanModel.get(propertyName);
			propertyModel.sortable(false);
		}
	}
}
