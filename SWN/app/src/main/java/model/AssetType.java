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
        if (type.equals("storage")) {
            return STORAGE;
        } else if (type.equals("source")) {
            return SOURCE;
        } else if (type.equals("outlet")) {
            return OUTLET;
        } else if (type.equals("pump")) {
            return PUMP;
        } else if (type.equals("recycling_plant")) {
            return RECYCLING_PLANT;
        } else if (type.equals("connection")) {
            return CONNECTION;
        } else {
            throw new RuntimeException("Unknown asset type");
        }
	}

	public String getLabel()
	{
		return label;
	}
}
