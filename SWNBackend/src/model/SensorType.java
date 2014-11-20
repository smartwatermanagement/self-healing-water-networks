package model;

public enum SensorType
{
	FLOW("flow"), LEVEL("level"), QUALITY("quality");
	
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
		case "flow":
			return FLOW;
		case "level":
			return LEVEL;
		case "quality":
			return QUALITY;
		default:
			throw new RuntimeException("Unknown sensor type : " + type);
		}
	}
}
