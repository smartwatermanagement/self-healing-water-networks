package action.nonrest.jsonModel;


public class JSONAggregationUsage
{
	public String name;
	public String usage;
	
	public JSONAggregationUsage(String name, String usage)
	{
		this.name = name;
		this.usage = usage;
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUsage()
	{
		return usage;
	}

	public void setUsage(String usage)
	{
		this.usage = usage;
	}
}