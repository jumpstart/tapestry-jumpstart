package jumpstart.business.domain.person.iface;

import java.util.List;

import jumpstart.util.query.SortCriterion;

public class PaginatedResult<T> {
	private int startIndex;
	private int maxResults; 
	private List<SortCriterion> sortCriteria;
	private long totalCount;
	private List<T> result;

	public String toString() {
		final String DIVIDER = ", ";

		StringBuilder buf = new StringBuilder();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("startIndex=" + startIndex + DIVIDER);
		buf.append("maxResults=" + maxResults + DIVIDER);
		buf.append("totalCount=" + totalCount + DIVIDER);
		buf.append("]");
		return buf.toString();
	}

	public PaginatedResult() {
	}

	public PaginatedResult(int startIndex, int maxResults, List<SortCriterion> sortCriteria, long totalCount,
			List<T> result) {
		super();
		this.startIndex = startIndex;
		this.maxResults = maxResults;
		this.sortCriteria = sortCriteria;
		this.totalCount = totalCount;
		this.result = result;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public List<SortCriterion> getSortCriteria() {
		return sortCriteria;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public List<T> getResult() {
		return result;
	}

}
