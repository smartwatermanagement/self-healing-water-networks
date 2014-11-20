package model;

import java.util.List;

public class Network {
	private List<Aggregation> aggregations;

	public Network(List<Aggregation> aggregations) {
		super();
		this.aggregations = aggregations;
	}

	public List<Aggregation> getAggregation() {
		return aggregations;
	}

	public void setAggregation(List<Aggregation> aggregations) {
		this.aggregations = aggregations;
	}
	
	

}
