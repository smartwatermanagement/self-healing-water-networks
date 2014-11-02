package model;

public class Sensor
{
	private int id;
	private SensorType type;
	
	public Sensor(int id, SensorType type)
	{
		super();
		this.id = id;
		this.type = type;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public SensorType getType()
	{
		return type;
	}
	public void setType(SensorType type)
	{
		this.type = type;
	}
	
}
