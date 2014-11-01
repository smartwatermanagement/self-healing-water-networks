package model;

public enum AssetType {
	STORAGE,
	OUTLET,
	SOURCE,
	PUMP,
	RECYCLING_PLANT;
	
	public static AssetType getType(String type) {
		switch(type)
		{
		case "storage" : return STORAGE;
		case "source" : return SOURCE;
		case "outlet" : return OUTLET;
		case "pump" : return PUMP;
		case "recycling_plant" : return RECYCLING_PLANT;
		default : throw new RuntimeException("Unknown asset type");
		}
	}

}
