package main.java.com.proterra.APIPojo.BusFaultsPojo;

public class FilterPojo {


	private TermsPojo terms;
	private RangePojo range;
	private QueryStringPojo queryString;

	public TermsPojo getTerms() {
		return terms;
	}

	public void setTerms(TermsPojo terms) {
		this.terms = terms;
	}

	public RangePojo getRange() {
		return range;
	}

	public void setRange(RangePojo range) {
		this.range = range;
	}

	public QueryStringPojo getQueryString() {
		return queryString;
	}

	public void setQueryString(QueryStringPojo queryString) {
		this.queryString = queryString;
	}
}
