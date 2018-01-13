package jumpstart.web.services;

import java.util.Map;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beanvalidator.ClientConstraintDescriptor;
import org.apache.tapestry5.internal.beanvalidator.BaseCCD;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.DisplayBlockContribution;
import org.apache.tapestry5.services.EditBlockContribution;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.DataConstants;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.services.security.WhitelistAnalyzer;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.upload.services.UploadSymbols;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.slf4j.Logger;

import jumpstart.business.validation.constraints.Letters;
import jumpstart.util.JodaTimeUtil;
import jumpstart.web.translators.MoneyTranslator;
import jumpstart.web.translators.YesNoTranslator;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to configure and extend
 * Tapestry, or to place your own service definitions. See http://tapestry.apache.org/5.3.4/tapestry-ioc/module.html
 */
public class AppModule {
	private static final String UPLOADS_PATH = "jumpstart.upload-path";

	@Inject
	@Symbol(SymbolConstants.PRODUCTION_MODE)
	@Property(write = false)
	private static boolean productionMode;

	// Add 2 services to those provided by Tapestry.
	// - CountryNames, and SelectIdModelFactory are used by pages which ask Tapestry to @Inject them.

	public static void bind(ServiceBinder binder) {
		binder.bind(CountryNames.class);
		binder.bind(SelectIdModelFactory.class, SelectIdModelFactoryImpl.class);
	}

	// Tell Tapestry about our custom translators and their message file.
	// We do this by contributing configuration to Tapestry's TranslatorAlternatesSource service, FieldValidatorSource
	// service, and ComponentMessagesSource service.

	@SuppressWarnings("rawtypes")
	public static void contributeTranslatorAlternatesSource(MappedConfiguration<String, Translator> configuration,
			ThreadLocale threadLocale) {
		configuration.add("yesno", new YesNoTranslator("yesno"));
		configuration.add("money2", new MoneyTranslator("money2", 2, threadLocale));
	}

	public void contributeComponentMessagesSource(OrderedConfiguration<String> configuration) {
		configuration.add("myTranslationMessages", "jumpstart/web/translators/TranslationMessages");
	}

	// Tell Tapestry about the client-side (javascript) validators that corresponds to each server-side Bean Validator.

	public static void contributeClientConstraintDescriptorSource(final JavaScriptSupport javaScriptSupport,
			final Configuration<ClientConstraintDescriptor> configuration) {

		configuration.add(new BaseCCD(Letters.class) {

			public void applyClientValidation(MarkupWriter writer, String message, Map<String, Object> attributes) {
				javaScriptSupport.require("beanvalidation/letters");
				writer.attributes(DataConstants.VALIDATION_ATTRIBUTE, true, "data-validate-letters", true,
						"data-letters-message", message);
			}

		});

	}

	// Tell Tapestry about our custom ValueEncoders.
	// We do this by contributing configuration to Tapestry's ValueEncoderSource service.

	// @SuppressWarnings("rawtypes")
	// public static void contributeValueEncoderSource(MappedConfiguration<Class, Object> configuration) {
	// configuration.addInstance(Person.class, PersonEncoder.class);
	// }

	// Tell Tapestry which locales we support, and tell Tapestry to use its jQuery implementation for its JavaScript.
	// We do this by contributing configuration to Tapestry's ApplicationDefaults service.

	public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration) {
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en_US,en_GB,fr");
		configuration.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER, "jquery");
	}

	// Tell Tapestry how to detect and protect pages that require security.
	// We do this by contributing a custom ComponentRequestFilter to Tapestry's ComponentRequestHandler service.
	// - ComponentRequestHandler is shown in
	// http://tapestry.apache.org/request-processing.html#RequestProcessing-Overview
	// - Based on http://tapestryjava.blogspot.com/2009/12/securing-tapestry-pages-with.html

	public void contributeComponentRequestHandler(OrderedConfiguration<ComponentRequestFilter> configuration) {
		configuration.addInstance("PageProtectionFilter", PageProtectionFilter.class);
	}

	// Tell Tapestry how to handle WildFly 11's classpath URLs - WildFly uses a "virtual file system".
	// We do this by overriding Tapestry's ClasspathURLConverter service.
	// See "Running Tapestry on JBoss" (sic) in http://wiki.apache.org/tapestry/Tapestry5HowTos .

	@SuppressWarnings("rawtypes")
	public static void contributeServiceOverride(MappedConfiguration<Class, Object> configuration) {
		// configuration.add(ClasspathURLConverter.class, new ClasspathURLConverterJBoss7());
		configuration.add(ClasspathURLConverter.class, new ClasspathURLConverterWildFly11());
	}

	// Tell Tapestry how to handle @EJB in page and component classes.
	// We do this by contributing configuration to Tapestry's ComponentClassTransformWorker service.
	// - Based on http://wiki.apache.org/tapestry/JEE-Annotation.

	@Primary
	public static void contributeComponentClassTransformWorker(
			OrderedConfiguration<ComponentClassTransformWorker2> configuration) {
		configuration.addInstance("EJB", EJBAnnotationWorker.class, "before:Property");
	}

	// Tell Tapestry how to handle pages annotated with @WhitelistAccessOnly, eg. Tapestry's ServiceStatus and
	// PageCatalog.
	// The default WhitelistAnalyzer allows localhost only and only in non-production mode.
	// Our aim is to make the servicestatus page available to ALL clients when not in production mode.
	// We do this by contributing our own WhitelistAnalyzer to Tapestry's ClientWhitelist service.

	public static void contributeClientWhitelist(OrderedConfiguration<WhitelistAnalyzer> configuration) {
		if (!productionMode) {
			configuration.add("NonProductionWhitelistAnalyzer", new WhitelistAnalyzer() {
				@Override
				public boolean isRequestOnWhitelist(Request request) {
					if (request.getPath().startsWith("/core/servicestatus")) {
						return true;
					}
					else {
						// This is copied from org.apache.tapestry5.internal.services.security.LocalhostOnly
						String remoteHost = request.getRemoteHost();
						return remoteHost.equals("localhost") || remoteHost.equals("127.0.0.1")
								|| remoteHost.equals("0:0:0:0:0:0:0:1%0") || remoteHost.equals("0:0:0:0:0:0:0:1");
					}
				}
			}, "before:*");
		}
	}

	// Tell Tapestry how to build our Filer service (used in the FileUpload example).
	// Annotate it with EagerLoad to force resolution of symbols at startup rather than when it is first used.

	@EagerLoad
	public static IFiler buildFiler(Logger logger, @Inject @Symbol(UPLOADS_PATH) final String uploadsPath,
			@Inject @Symbol(UploadSymbols.FILESIZE_MAX) final long fileSizeMax) {
		return new Filer(logger, UPLOADS_PATH, uploadsPath, UploadSymbols.FILESIZE_MAX, fileSizeMax);
	}

	// Tell Tapestry how to coerce Joda Time types to and from Java Date types for the TypeCoercers example.
	// We do this by contributing configuration to Tapestry's TypeCoercer service.
	// - Based on http://tapestry.apache.org/typecoercer-service.html

	@SuppressWarnings("rawtypes")
	public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration) {

		// From java.util.Date to DateMidnight

		Coercion<java.util.Date, DateMidnight> toDateMidnight = new Coercion<java.util.Date, DateMidnight>() {
			public DateMidnight coerce(java.util.Date input) {
				// TODO - confirm this conversion always works, esp. across timezones
				return JodaTimeUtil.toDateMidnight(input);
			}
		};

		configuration.add(new CoercionTuple<>(java.util.Date.class, DateMidnight.class, toDateMidnight));

		// From DateMidnight to java.util.Date

		Coercion<DateMidnight, java.util.Date> fromDateMidnight = new Coercion<DateMidnight, java.util.Date>() {
			public java.util.Date coerce(DateMidnight input) {
				// TODO - confirm this conversion always works, esp. across timezones
				return JodaTimeUtil.toJavaDate(input);
			}
		};

		configuration.add(new CoercionTuple<>(DateMidnight.class, java.util.Date.class, fromDateMidnight));

		// From java.util.Date to LocalDate

		Coercion<java.util.Date, LocalDate> toLocalDate = new Coercion<java.util.Date, LocalDate>() {
			public LocalDate coerce(java.util.Date input) {
				// TODO - confirm this conversion always works, esp. across timezones
				return JodaTimeUtil.toLocalDate(input);
			}
		};

		configuration.add(new CoercionTuple<>(java.util.Date.class, LocalDate.class, toLocalDate));

		// From LocalDate to java.util.Date

		Coercion<LocalDate, java.util.Date> fromLocalDate = new Coercion<LocalDate, java.util.Date>() {
			public java.util.Date coerce(LocalDate input) {
				// TODO - confirm this conversion always works, esp. across timezones
				return JodaTimeUtil.toJavaDate(input);
			}
		};

		configuration.add(new CoercionTuple<>(LocalDate.class, java.util.Date.class, fromLocalDate));
	}

	// Tell Tapestry how its BeanDisplay and BeanEditor can handle the JodaTime types.
	// We do this by contributing configuration to Tapestry's DefaultDataTypeAnalyzer and BeanBlockSource services.
	// - Based on http://tapestry.apache.org/beaneditform-guide.html .

	public static void contributeDefaultDataTypeAnalyzer(
			@SuppressWarnings("rawtypes") MappedConfiguration<Class, String> configuration) {
		configuration.add(DateTime.class, "dateTime");
		configuration.add(DateMidnight.class, "dateMidnight");
		configuration.add(LocalDateTime.class, "localDateTime");
		configuration.add(LocalDate.class, "localDate");
		configuration.add(LocalTime.class, "localTime");
	}

	public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration) {

		configuration.add(new DisplayBlockContribution("dateTime", "infra/AppPropertyDisplayBlocks", "dateTime"));
		configuration
				.add(new DisplayBlockContribution("dateMidnight", "infra/AppPropertyDisplayBlocks", "dateMidnight"));
		configuration
				.add(new DisplayBlockContribution("localDateTime", "infra/AppPropertyDisplayBlocks", "localDateTime"));
		configuration.add(new DisplayBlockContribution("localDate", "infra/AppPropertyDisplayBlocks", "localDate"));
		configuration.add(new DisplayBlockContribution("localTime", "infra/AppPropertyDisplayBlocks", "localTime"));

		configuration.add(new EditBlockContribution("dateMidnight", "infra/AppPropertyEditBlocks", "dateMidnight"));
		configuration.add(new EditBlockContribution("localDate", "infra/AppPropertyEditBlocks", "localDate"));

	}

}
