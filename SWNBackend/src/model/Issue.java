package model;

import java.sql.Date;

public class Issue {
	private int id;
	private IssueType type;
	private Status status;
	private Date creationTime;
	private Date updationTime;
	private String details;
	
	
	
	public Issue(int id, IssueType type, Status status, Date creationTime,
			Date updationTime, String details) {
		this.id = id;
		this.type = type;
		this.status = status;
		this.creationTime = creationTime;
		this.updationTime = updationTime;
		this.details = details;
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
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Date getUpdationTime() {
		return updationTime;
	}
	public void setUpdationTime(Date updationTime) {
		this.updationTime = updationTime;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	
	

}
