package jumpstart.web.pages.examples.styling;

public class StylingLinksAndSubmits2 {

	private String yourChoice;
	
	public void set(String yourChoice) {
		this.yourChoice = yourChoice;
	}
	
	String onPassivate() {
		return yourChoice;
	}

	void onActivate(String yourChoice) {
		this.yourChoice = yourChoice;
	}

	public String getYourChoice() {
		return yourChoice;
	}
}
