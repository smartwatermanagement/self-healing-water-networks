package dao;

import model.Issue;
import model.Notification;

import java.util.List;

public interface IIssueDAO {
	
	public Issue findById(int id);
	public List<Issue> findAll();
	
	

}
