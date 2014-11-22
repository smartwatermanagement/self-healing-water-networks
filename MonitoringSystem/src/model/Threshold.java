package model;

public class Threshold {
	private int id;
	private int assetId;
	private String property;
	private String value;
	private String operator;
	
	
	public Threshold(int id, int assetId, String property, String value, String operator) {
		this.id = id;
		this.property = property;
		this.value = value;
		this.operator = operator;
		this.id = assetId;
	}



	public int getAssetId() {
		return assetId;
	}



	public void setAssetId(int assetId) {
		this.assetId = assetId;
	}



	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getProperty() {
		return property;
	}


	public void setProperty(String property) {
		this.property = property;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public String getOperator() {
		return operator;
	}


	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
	
	

}
