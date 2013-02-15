// Based on Tapestry's SelectModelFactory.

package jumpstart.web.services;

import java.util.List;

import org.apache.tapestry5.SelectModel;

/**
 * Used to create an {@link org.apache.tapestry5.SelectModel}.
 */
public interface SelectIdModelFactory
{   
    /**
     * Creates a {@link org.apache.tapestry5.SelectModel} from a list of objects of the same type and a label property name and an id property name.
     * The returned model creates for every object in the list a selectable option and relies on existing 
     * {@link org.apache.tapestry5.ValueEncoder} for the object type. The value of the label property is used as user-presentable label for the option.
     * The value of the id property is used as the hidden id for the option.
     * 
     * @param objects objects to create model from
     * @param labelProperty property for the client-side label
     * @param idProperty property for the client-side value
     * @return the model
     */
    public SelectModel create(List<?> objects, String labelProperty, String idProperty);
}
