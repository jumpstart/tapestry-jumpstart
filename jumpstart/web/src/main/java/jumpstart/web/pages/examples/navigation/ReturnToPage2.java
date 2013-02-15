package jumpstart.web.pages.examples.navigation;

import jumpstart.web.pages.Index;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ReturnToPage2 {

	// The activation context

	private String messageFromCaller;

	// Screen fields

	@Persist
	private Link linkBackToCaller;

	// Generally useful bits and pieces

	@Inject
	private ComponentResources componentResources;

	// The code

	public void set(String messageFromCaller, Link linkBackToCaller) {
		this.messageFromCaller = messageFromCaller;
		this.linkBackToCaller = linkBackToCaller;
	}

	String onPassivate() {
		return messageFromCaller;
	}

	void onActivate(String messageFromCaller) {
		this.messageFromCaller = messageFromCaller;
	}

	Link onReturn() {
		// Create a copy of the return link, because the original is about to be discarded.
		String basePath = linkBackToCaller.getBasePath();
		Link linkBackToCallerTmp = linkBackToCaller.copyWithBasePath(basePath);
		
		componentResources.discardPersistentFieldChanges();
		return linkBackToCallerTmp;
	}

	Object onGoHome() {
		componentResources.discardPersistentFieldChanges();
		return Index.class;
	}

	public String getMessageFromCaller() {
		return messageFromCaller;
	}

	public Link getLinkBackToCaller() {
		return linkBackToCaller;
	}
}
