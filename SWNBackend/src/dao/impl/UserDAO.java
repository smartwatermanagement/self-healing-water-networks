package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import utils.ConnectionPool;
import model.Aggregation;
import model.Asset;
import model.Issue;
import model.IssueType;
import model.Status;
import model.User;
import dao.IUserDAO;

public class UserDAO implements IUserDAO{
	
	private static final String findByUserNamePasswordQuery = "SELECT * FROM users WHERE username=? and password=?";
	private static final String insertUserQuery = "INSERT INTO users(name, phone_number, username, password) VALUES(?,?,?,?)";
	private static final String setSubscriptionQuery = "INSERT INTO subscriptions(issue_type, user_id,"
			+ " aggregation_id) values(?,?,?)";

	public User findByUserNamePassword(String username, String password) {
		User user = null;
		Connection connection= null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(findByUserNamePasswordQuery);
			statement.setString(1, username);
			statement.setString(2, password);
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()){
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String phoneNumber = resultSet.getString("phone_number");
				user = new User(id, name, phoneNumber, username, null);
				user.setNotifications((new NotificationDAO()).findByUser(user));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return user;
				
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

	public boolean setUser(User user, String password) {
		boolean result = false;
		Connection connection= null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(insertUserQuery);
			statement.setString(1, user.getName());
			statement.setString(2, user.getPhoneNumber());
			statement.setString(3, user.getUserName());
			statement.setString(4, password);
			statement.executeUpdate();
			result = true;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return result;
	}

	public boolean setSubscriptionForUser(User user, IssueType type,
			Aggregation aggregation) {
		boolean result = false;
		Connection connection= null;
		PreparedStatement statement = null;
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(setSubscriptionQuery);
			statement.setString(1,type.toString());
			statement.setInt(2, user.getId());
			statement.setInt(3, aggregation.getId());
			statement.executeUpdate();
			result = true;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return result;
	}
	

}
