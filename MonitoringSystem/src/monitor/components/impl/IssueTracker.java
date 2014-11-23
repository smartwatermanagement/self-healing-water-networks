package monitor.components.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import utils.Constants;
import model.Threshold;
import monitor.components.IIssueTracker;

public  class IssueTracker implements IIssueTracker{

	private final String INSERT_THRESHOLD_ISSUE = "INSERT INTO issues(asset_id, type, details) VALUES(?,?,?)";
	private final String INSERT_NOTIFICATION = "INSERT INTO notifications(user_id, issue_id) VALUES(?, ?)";
	private final String SUBSCRIPTIONS_QUERY = "SELECT user_id FROM subscriptions WHERE issueType = ? and aggregation_id = ?";
	private final String PARENT_AGGREGATION_QUERY = "SELECT parent_id FROM aggregations WHERE id=?";
	private final String ASSET_PARENT_QUERY = "SELECT parent_id FROM aggregations WHERE id in (SELECT aggregation_id FROM"
			+ " assets WHERE id = ?)";
	private final Logger logger = Logger.getLogger(getClass());

	@Override
	public int createThresholdBreachIssue(Threshold threshold, String value)  {
		int id = 0;

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<String> parentAggregations = new ArrayList<String>();
		List<Integer> subscribedUserIds = new ArrayList<Integer>();
		

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(Constants.dbUrl + Constants.dbName, Constants.dbUsername, Constants.dbPassword);
			
			statement = connection.prepareStatement(INSERT_THRESHOLD_ISSUE, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, threshold.getAssetId());
			statement.setString(2, Constants.THRESHOLD_ISSUE_TYPE);
			statement.setString(3, threshold.getJSON(value));
			statement.executeUpdate();
			
			resultSet = statement.getGeneratedKeys();
			if(resultSet.next())
				id = resultSet.getInt(1);
			
			statement.close();
			resultSet.close();
			
			logger.debug("Threshold Breach Issue with id " + id + " created.");
			
			statement = connection.prepareStatement(ASSET_PARENT_QUERY);
			statement.setInt(1, threshold.getAssetId());
			resultSet = statement.executeQuery();
			Integer parentId = null;

			
			while(resultSet.next()){
				parentId = resultSet.getInt("parent_id");
				if(parentId != null)
					parentAggregations.add(parentId + "");
			}


			statement.close();
			resultSet.close();


			
			while(parentId != 0){
				statement = connection.prepareStatement(PARENT_AGGREGATION_QUERY);
				statement.setInt(1, parentId);
				resultSet = statement.executeQuery();
				while(resultSet.next()){
					parentId = resultSet.getInt("parent_id");
					if(parentId != null)
						parentAggregations.add(parentId + "");
				}
				statement.close();
				resultSet.close();	

			}
			
			for(String parentAggregation: parentAggregations){
				statement = connection.prepareStatement(SUBSCRIPTIONS_QUERY);
				statement.setString(1, Constants.THRESHOLD_ISSUE_TYPE);
				statement.setInt(2, Integer.parseInt(parentAggregation));
				
				resultSet = statement.executeQuery();
				while(resultSet.next()){
					subscribedUserIds.add(resultSet.getInt("user_id"));
				}
				statement.close();
				resultSet.close();
				
			}
			
			for(Integer subscribedUser: subscribedUserIds){
				statement = connection.prepareStatement(INSERT_NOTIFICATION);
				statement.setInt(1, subscribedUser);
				statement.setInt(2, id);
				statement.execute();
				logger.debug("Notification for user " + subscribedUser + " and issue id " + id + " created.");
				
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		
		return id;
	}


}
