package jumpstart.util.query;

public enum SortDirection {
	ASCENDING, DESCENDING, UNSORTED;
	
	public String toStringForJpql() {
		if (this == ASCENDING) {
			return "";
		}
		else if (this == DESCENDING) {
			return " desc";
		}
		else {
			return "";
		}
	}
}