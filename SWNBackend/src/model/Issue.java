package model;


import java.sql.Timestamp;

public class Issue {
	private int id;
	private IssueType type;
	private Status status;
	private String creationTime;
	private String updationTime;
	private String details;
	private int assetId;
	private int aggregationId;
	
	public Issue(){
		
	}
	
	
	public Issue(int id, IssueType type, Status status, String creationTime,
			String updationTime, String details, int assetId, int aggregationId) {
		this.id = id;
		this.type = type;
		this.status = status;
		this.creationTime = creationTime;
		this.updationTime = updationTime;
		this.details = details;
		this.assetId = assetId;
		this.aggregationId = aggregationId;
	}
	

	public int getAssetId() {
		return assetId;
	}


	public void setAssetId(int assetId) {
		this.assetId = assetId;
	}


	public int getAggregationId() {
		return aggregationId;
	}

	public void setAggregationId(int aggregationId) {
		this.aggregationId = aggregationId;
	}




	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public IssueType getType() {
		return type;
	}
	public void setType(IssueType type) {
		this.type = type;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}


	public String getCreationTime() {
		return creationTime;
	}


	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}


	public String getUpdationTime() {
		return updationTime;
	}


	public void setUpdationTime(String updationTime) {
		this.updationTime = updationTime;
	}
	
	
	

}
