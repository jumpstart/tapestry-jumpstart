package jumpstart.web.state.examples.state;

public class Count {
	private int timesVisited = 0;

	public int getTimesVisited() {
		return timesVisited;
	}

	public void incrementTimesVisited() {
		timesVisited += 1;
	}
}
