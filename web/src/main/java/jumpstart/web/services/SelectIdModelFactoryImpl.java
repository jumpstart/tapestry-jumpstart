// Based on Tapestry's SelectModelFactoryImpl.

package jumpstart.web.services;

import java.util.List;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.services.ValueEncoderSource;

public class SelectIdModelFactoryImpl implements SelectIdModelFactory {
	private final PropertyAccess propertyAccess;
	private final ValueEncoderSource valueEncoderSource;

	public SelectIdModelFactoryImpl(final PropertyAccess propertyAccess, final ValueEncoderSource valueEncoderSource) {
		super();
		this.propertyAccess = propertyAccess;
		this.valueEncoderSource = valueEncoderSource;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SelectModel create(final List<?> objects, final String labelProperty, final String idProperty) {
		final List<OptionModel> options = CollectionFactory.newList();

		for (final Object object : objects) {
			final ClassPropertyAdapter classPropertyAdapter = this.propertyAccess.getAdapter(object);

			final PropertyAdapter labelPropertyAdapter = classPropertyAdapter.getPropertyAdapter(labelProperty);
			final ValueEncoder labelEncoder = this.valueEncoderSource.getValueEncoder(labelPropertyAdapter.getType());
			final Object label = labelPropertyAdapter.get(object);

			final PropertyAdapter idPropertyAdapter = classPropertyAdapter.getPropertyAdapter(idProperty);
			final ValueEncoder idEncoder = this.valueEncoderSource.getValueEncoder(idPropertyAdapter.getType());
			final Object id = idPropertyAdapter.get(object);

			options.add(new OptionModelImpl(labelEncoder.toClient(label), idEncoder.toClient(id)));
		}

		return new SelectModelImpl(null, options);
	}
}
