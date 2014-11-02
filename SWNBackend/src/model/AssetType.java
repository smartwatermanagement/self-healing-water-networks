package model;

public enum AssetType {
	STORAGE("storage"),
	OUTLET("outlet"),
	SOURCE("source"),
	PUMP("pump"),
	RECYCLING_PLANT("recycling_plant"),
	CONNECTION("connection");
	
	private String label;
	
	AssetType(String label) {
		this.label = label;
	}
	
	public static AssetType getType(String type) {
		switch(type)
		{
		case "storage" : return STORAGE;
		case "source" : return SOURCE;
		case "outlet" : return OUTLET;
		case "pump" : return PUMP;
		case "recycling_plant" : return RECYCLING_PLANT;
		case "connection" : return CONNECTION;
		default : throw new RuntimeException("Unknown asset type");
		}
	}

	public String getLabel()
	{
		return label;
	}
}
