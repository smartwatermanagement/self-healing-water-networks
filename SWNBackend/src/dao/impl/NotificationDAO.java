package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.ConnectionPool;
import model.Issue;
import model.Notification;
import model.User;
import dao.IIssueDAO;
import dao.INotificationDAO;

public class NotificationDAO implements INotificationDAO{
	
	private static final String deleteQuery = "DELETE FROM notifications WHERE id=?";
	private static final String markReadQuery = "UPDATE notifications SET read_status=? WHERE id=?";
	private static final String notificationsQuery = "SELECT * FROM notifications WHERE user_id=?";

	public List<Notification> findByUser(User user) {
		List<Notification> notifications = new ArrayList<Notification>();
		Connection connection = null;
		PreparedStatement statement = null;
		IIssueDAO issueDao = new IssueDAO();
		
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(notificationsQuery);
			statement.setInt(1, user.getId());
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				int id = resultSet.getInt("id");
				boolean readStatus = resultSet.getBoolean("read_status");
				Issue issue = issueDao.findById(resultSet.findColumn("issue_id"));
				Notification notification = new Notification(id,readStatus, issue);
				notifications.add(notification);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return notifications;
	}

	public void deleteNotification(Notification notification) {
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(deleteQuery);
			statement.setInt(1, notification.getId());
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
	}

	public void markNotificationRead(Notification notification) {
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(markReadQuery);
			statement.setBoolean(1, true);
			statement.setInt(2, notification.getId());
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
	}

}
