package jumpstart.web.state.theapp;

import java.io.Serializable;

import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.User.PageStyle;

@SuppressWarnings("serial")
public class Visit implements Serializable {

	private Long myUserId = null;
	private String myLoginId = null;
	private PageStyle pageStyle = null;
	private String dateInputPattern = null;
	private String dateViewPattern = null;
	private String dateListPattern = null;
	
	public Visit(User user) {
		myUserId = user.getId();
		cacheUsefulStuff(user);
	}

	public void noteChanges(User user) {
		if (user == null) {
			throw new IllegalArgumentException();
		}
		else if (user.getId().equals(myUserId)) {
			cacheUsefulStuff(user);
		}
	}

	private void cacheUsefulStuff(User user) {
		myLoginId = user.getLoginId();
		pageStyle = user.getPageStyle();
		dateInputPattern = user.getDateInputPattern();
		dateViewPattern = user.getDateViewPattern();
		dateListPattern = user.getDateListPattern();
	}

	public Long getMyUserId() {
		return myUserId;
	}

	public String getMyLoginId() {
		return myLoginId;
	}

	public PageStyle getPageStyle() {
		return pageStyle;
	}

	public String getDateInputPattern() {
		return dateInputPattern;
	}

	public String getDateViewPattern() {
		return dateViewPattern;
	}

	public String getDateListPattern() {
		return dateListPattern;
	}

}
