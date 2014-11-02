package model;

public enum SensorType
{
	FLOW_SENSOR("flow_sensor");
	
	private String label;
	
	SensorType(String label)
	{
		this.label = label;
	}
	
	public String label()
	{
		return label;
	}
	
	public static SensorType getSensorType(String type)
	{
		switch(type)
		{
		case "flow_sensor":
			return FLOW_SENSOR;
		default:
			throw new RuntimeException("Unknown sensor type");
		}
	}
}
