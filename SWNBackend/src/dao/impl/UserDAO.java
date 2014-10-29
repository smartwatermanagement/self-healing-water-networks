package dao.impl;

import java.util.List;

import model.Aggregation;
import model.IssueType;
import model.User;
import dao.IUserDAO;

public class UserDAO implements IUserDAO{

	public User findByUserNamePassword(String userName, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	public User findById(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public User findByUserName(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<User> findUsersByAggregation(Aggregation aggregation) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<User> findUsersByIssueType(IssueType type) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<User> findUsersByAggregationAndIssueType(
			Aggregation aggregation, IssueType type) {
		// TODO Auto-generated method stub
		return null;
	}

	public int setUser(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean setSubscriptionForUser(User user, IssueType type,
			Aggregation aggregation) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
