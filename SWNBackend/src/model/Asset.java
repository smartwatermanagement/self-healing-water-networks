package model;

import java.util.List;
import java.util.Map;

public class Asset
{
	private int id;
	private Map<String, String> propertyValueMap;
	private String name;
	private int issueCount;
	private AssetType type;
	private Double latitude;
	private Double longitude;
	private List<Threshold> thresholds;
	private int aggregationId;
	private List<Sensor> sensors;

	public Asset(int id, Map<String, String> propertyValueMap, String name,
			int issueCount, AssetType type, Double latitude, Double longitude,
			List<Threshold> thresholds, int aggregationId)
	{
		super();
		this.id = id;
		this.propertyValueMap = propertyValueMap;
		this.name = name;
		this.issueCount = issueCount;
		this.type = type;
		this.latitude = latitude;
		this.longitude = longitude;
		this.thresholds = thresholds;
		this.setAggregationId(aggregationId);
	}

	public List<Threshold> getThresholds()
	{
		return thresholds;
	}

	public void setThresholds(List<Threshold> thresholds)
	{
		this.thresholds = thresholds;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Map<String, String> getPropertyValueMap()
	{
		return propertyValueMap;
	}

	public void setPropertyValueMap(Map<String, String> propertyValueMap)
	{
		this.propertyValueMap = propertyValueMap;
	}

	public int getIssueCount()
	{
		return issueCount;
	}

	public void setIssueCount(int issueCount)
	{
		this.issueCount = issueCount;
	}

	public AssetType getType()
	{
		return type;
	}

	public void setType(AssetType type)
	{
		this.type = type;
	}

	public Double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(Double latitude)
	{
		this.latitude = latitude;
	}

	public Double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(Double longitude)
	{
		this.longitude = longitude;
	}

	public int getAggregationId()
	{
		return aggregationId;
	}

	public void setAggregationId(int aggregationId)
	{
		this.aggregationId = aggregationId;
	}

	public List<Sensor> getSensors()
	{
		return sensors;
	}

	public void setSensors(List<Sensor> sensors)
	{
		this.sensors = sensors;
	}

	public boolean hasFlowSensor()
	{
		for (Sensor sensor : sensors)
			if (sensor.getType() == SensorType.FLOW)
				return true;
		return false;
	}

	public int getFlowSensorId()
	{
		for (Sensor sensor : sensors)
			if (sensor.getType() == SensorType.FLOW)
				return sensor.getId();
		throw new RuntimeException("Asset, " + id
				+ ", does not have a flow sensor");
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
