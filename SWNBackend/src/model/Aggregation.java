package model;

import java.util.List;

public class Aggregation implements IAggregation {
	private int id;
	private String name;
	List<Integer> aggregationIds;
	List<Integer> assetIds;
	List<Aggregation> aggregations;
	List<Asset> assets;
	
	public Aggregation(int id, String name, List<Integer> aggregationIds, 
			List<Integer> assetIds, List<Aggregation> aggregations,
			List<Asset> assets) {
		super();
		this.id = id;
		this.name = name;
		this.aggregationIds = aggregationIds;
		this.assetIds = assetIds;
		this.aggregations = aggregations;
		this.assets = assets;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Aggregation> getAggregations() {
		return aggregations;
	}
	public void setAggregations(List<Aggregation> aggregations) {
		this.aggregations = aggregations;
	}
	public List<Asset> getAssets() {
		return assets;
	}
	public void setAssets(List<Asset> assets) {
		this.assets = assets;
	}
	public List<Integer> getAggregationIds() {
		return aggregationIds;
	}
	public void setAggregationIds(List<Integer> aggregationIds) {
		this.aggregationIds = aggregationIds;
	}
	public List<Integer> getAssetIds() {
		return assetIds;
	}
	public void setAssetIds(List<Integer> assetIds) {
		this.assetIds = assetIds;
	}
	
	
	

}
