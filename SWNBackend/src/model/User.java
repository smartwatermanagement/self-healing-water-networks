package model;

import java.util.List;

public class User {
	private int id;
	private String name;
	private String phoneNumber;
	private String userName;
	List<Notification> notifications;
	
	
	
	public User(int id, String name, String phoneNumber, String userName,
			List<Notification> notifications) {
		super();
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.userName = userName;
		this.notifications = notifications;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<Notification> getNotifications() {
		return notifications;
	}
	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
	
	
	
	

}
