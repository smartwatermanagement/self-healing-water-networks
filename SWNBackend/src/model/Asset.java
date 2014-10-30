package model;

import java.util.List;
import java.util.Map;

public class Asset {
	private int id;
	private Map<String, String> propertyValueMap;
	private int issueCount;
	private AssetType type;
	private Double latitude;
	private Double longitude;
	private List<Threshold> thresholds;
	
	public Asset(int id, Map<String, String> propertyValueMap, int issueCount,
			AssetType type, Double latitude, Double longitude,
			List<Threshold> thresholds) {
		super();
		this.id = id;
		this.propertyValueMap = propertyValueMap;
		this.issueCount = issueCount;
		this.type = type;
		this.latitude = latitude;
		this.longitude = longitude;
		this.thresholds = thresholds;
	}
	
	
	public List<Threshold> getThresholds() {
		return thresholds;
	}


	public void setThresholds(List<Threshold> thresholds) {
		this.thresholds = thresholds;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Map<String, String> getPropertyValueMap() {
		return propertyValueMap;
	}
	public void setPropertyValueMap(Map<String, String> propertyValueMap) {
		this.propertyValueMap = propertyValueMap;
	}
	public int getIssueCount() {
		return issueCount;
	}
	public void setIssueCount(int issueCount) {
		this.issueCount = issueCount;
	}
	public AssetType getType() {
		return type;
	}
	public void setType(AssetType type) {
		this.type = type;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}	
	
}
