package dao;

import java.util.List;

import model.Aggregation;
import model.IssueType;
import model.User;

public interface IUserDAO {
	
	public User findByUserNamePassword(String userName, String password);
	public User findById(int userId);
	public User findByUserName(String userName);
	public List<User> getAllUsers();
	public List<User> findUsersByAggregation(Aggregation aggregation);
	public List<User> findUsersByIssueType(IssueType type);
	public List<User> findUsersByAggregationAndIssueType(Aggregation aggregation, IssueType type);
	
	public boolean setUser(User user, String password);
	public boolean setSubscriptionForUser(User user, IssueType type, Aggregation aggregation);
}
