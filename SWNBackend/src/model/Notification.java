package model;

public class Notification {
	private int id;
	private boolean read;
	private Issue issue;
	
	public Notification(){
		
	}
	public Notification(int id, boolean read, Issue issue) {
		super();
		this.id = id;
		this.read = read;
		this.issue = issue;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	public Issue getIssue() {
		return issue;
	}
	public void setIssue(Issue issue) {
		this.issue = issue;
	}
	
	

}
