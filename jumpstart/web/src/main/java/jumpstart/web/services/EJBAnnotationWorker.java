// Based on http://wiki.apache.org/tapestry/JEE-Annotation, adapted to EJB 3.0 (from 3.1).

package jumpstart.web.services;

import javax.ejb.EJB;

import jumpstart.client.BusinessServicesLocator;
import jumpstart.util.StringUtil;

import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticField;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Inject an EJB into tapestry sources
 */
public class EJBAnnotationWorker implements ComponentClassTransformWorker2 {
	private static final Logger logger = LoggerFactory.getLogger(EJBAnnotationWorker.class);

	private BusinessServicesLocator locator;

	public EJBAnnotationWorker() {
		locator = new BusinessServicesLocator(logger);
	}

	@Override
	public void transform(PlasticClass plasticClass, TransformationSupport support, MutableComponentModel model) {

		for (PlasticField field : plasticClass.getFieldsWithAnnotation(EJB.class)) {
			final EJB annotation = field.getAnnotation(EJB.class);

			if (StringUtil.isNotEmpty(annotation.name())) {
				throw new RuntimeException(
						"This implementation of the @EJB annotation does not support the name parameter. Found name = \""
								+ annotation.name() + "\".");
			}
			else if (StringUtil.isNotEmpty(annotation.beanName())) {
				throw new RuntimeException(
						"This implementation of the @EJB annotation does not support the beanName parameter. Found beanName = \""
								+ annotation.beanName() + "\".");
			}
			else if (StringUtil.isNotEmpty(annotation.mappedName())) {
				throw new RuntimeException(
						"This implementation of the @EJB annotation does not support the mappedName parameter. Found mappedName = \""
								+ annotation.mappedName() + "\".");
			}

			String fieldType = field.getTypeName();
			Object injectionValue = locator.getService(fieldType);

			if (injectionValue != null) {
				field.inject(injectionValue);
				field.claim(annotation);
			}
		}
	}

}
