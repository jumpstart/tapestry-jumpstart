package jumpstart.web.pages.examples.state;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.InjectPage;

public class PassingDataBetweenPages {

	@InjectPage
	private PassingByActivationContext byActivationContextPage;

	@InjectPage
	private PassingByQueryString byQueryStringPage;
	
	@InjectPage
	private PassingByPersist byPersistPage;

	Object onSuccessFromUseActivationContext() {
		byActivationContextPage.set("Humpty", "Dumpty");
		return byActivationContextPage;
	}

	Object onSuccessFromUseQueryString() {
		Link link = byQueryStringPage.set("Humpty", "Dumpty");
		return link;
	}

	Object onSuccessFromUsePersist() {
		byPersistPage.set("Humpty", "Dumpty");
		return byPersistPage;
	}
}
