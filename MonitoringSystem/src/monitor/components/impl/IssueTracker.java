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

	private final String INSERT_THRESHOLD_ISSUE = "INSERT INTO issues(asset_id, type, details, created_at) VALUES(?,?,?, now())";
	private final String INSERT_NOTIFICATION = "INSERT INTO notifications(user_id, issue_id) VALUES(?, ?)";
	private final String SUBSCRIPTIONS_QUERY = "SELECT user_id FROM subscriptions WHERE issueType = ? and aggregation_id = ?";
	private final String SUBSCRIPTIONS_QUERY_BY_TYPE = "SELECT user_id FROM subscriptions WHERE issueType = ? and aggregation_id is NULL";
	private final String PARENT_AGGREGATION_QUERY = "SELECT parent_id FROM aggregations WHERE id=?";
	private final String ASSET_PARENT_QUERY = "SELECT aggregation_id FROM"
			+ " assets WHERE id = ?";
	private final String INSERT_WATER_REQUiREMENT_ISSUE = "INSERT INTO issues(type, details, created_at) VALUES(?,?, now())";
	private final String INC_ISSUE_COUNT = "UPDATE aggregations SET issue_count = issue_count + 1 WHERE id=?";
	private final String UPDATE_ISSUE_STATUS = "UPDATE issues SET status=? WHERE id=?";
	private final String DEC_ISSUE_COUNT = "UPDATE aggregations SET issues_count = issue_count - 1 WHERE id=?";
	private final String SELECT_ISSUE_QUERY = "SELECT * FROM issues WHERE id=?";
	private final String INSERT_LEAK_ISSUE = "INSERT INTO issues(asset_id, type,created_at) VALUES(?,?,now())";
	private final String INC_ISSUE_COUNT_ASSET = "UPDATE assets SET issue_count = issue_count + 1 WHERE id = ?";
	private final String DEC_ISSUE_COUNT_ASSET = "UPDATE assets SET issue_count = issue_count - 1 WHERE id = ?";

	private final Logger logger = Logger.getLogger(getClass());

	@Override
	public int createThresholdBreachIssue(Threshold threshold, String value)  {
		int id = 0;

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;


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
			Integer parentId = 0;


			while(resultSet.next()){
				parentId = resultSet.getInt("aggregation_id");
			}


			statement.close();
			resultSet.close();
			
			statement = connection.prepareStatement(INC_ISSUE_COUNT_ASSET);
			statement.setInt(1, threshold.getAssetId());
			statement.executeUpdate();


			createNotification(parentId, id, Constants.THRESHOLD_ISSUE_TYPE);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException ex){
			ex.printStackTrace();
		}

		return id;
	}

	@Override
	public int createWaterRequirementIssue(int requirement,
			int available, int lastDayUsage) {

		int id = 0;

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		String detailsJSON = "{'requirement':'" + requirement + "', 'available':'" + available + "', 'last_day_usage': '" + lastDayUsage+ "'}";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(Constants.dbUrl + Constants.dbName, Constants.dbUsername, Constants.dbPassword);

			statement = connection.prepareStatement(INSERT_WATER_REQUiREMENT_ISSUE, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, Constants.WATER_REQUIREMENT_PREDICTION_ISSUE_TYPE);
			statement.setString(2, detailsJSON);
			statement.executeUpdate();

			resultSet = statement.getGeneratedKeys();
			if(resultSet.next())
				id = resultSet.getInt(1);

			statement.close();
			resultSet.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException ex){
			ex.printStackTrace();
		}

		logger.debug("Water requirement prediction issue with id " + id + " created.");
		createNotification(0,id, Constants.WATER_REQUIREMENT_PREDICTION_ISSUE_TYPE);
		return id;
	}

	private void createNotification(Integer parentId, Integer issueId, String issueType){

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		List<String> parentAggregations = new ArrayList<String>();
		List<Integer> subscribedUserIds = new ArrayList<Integer>();

		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(Constants.dbUrl + Constants.dbName, Constants.dbUsername, Constants.dbPassword);
			

			if(parentId != 0){
				parentAggregations.add(parentId + "");

				// Update issue count of parent
				statement = connection.prepareStatement(INC_ISSUE_COUNT);
				statement.setInt(1, parentId);
				statement.executeUpdate();
			}

			while(parentId != 0){
				statement = connection.prepareStatement(PARENT_AGGREGATION_QUERY);
				statement.setInt(1, parentId);
				resultSet = statement.executeQuery();
				while(resultSet.next()){
					parentId = resultSet.getInt("parent_id");
					if(parentId != 0)
						parentAggregations.add(parentId + "");
				}
				statement.close();
				resultSet.close();	

				// Update the issue count of parent
				statement = connection.prepareStatement(INC_ISSUE_COUNT);
				statement.setInt(1, parentId);
				statement.executeUpdate();

			}


			statement = connection.prepareStatement(SUBSCRIPTIONS_QUERY_BY_TYPE);
			statement.setString(1, issueType);

			resultSet = statement.executeQuery();
			while(resultSet.next()){
				subscribedUserIds.add(resultSet.getInt("user_id"));
			}
			statement.close();
			resultSet.close();


			for(String parentAggregation: parentAggregations){
				statement = connection.prepareStatement(SUBSCRIPTIONS_QUERY);
				statement.setString(1, issueType);
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
				statement.setInt(2, issueId);
				statement.execute();
				logger.debug("Notification for user " + subscribedUser + " and issue id " + issueId + " created.");

			}

		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	}

	@Override
	public void updateIssueStatus(int issueId, String status) {

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		int assetId = 0;
		int aggregationId = 0;
		Integer parentId = 0;

		try{

			// Update issue status
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(Constants.dbUrl + Constants.dbName, Constants.dbUsername, Constants.dbPassword);
			statement = connection.prepareStatement(UPDATE_ISSUE_STATUS);
			statement.setString(1, status);
			statement.setInt(2, issueId);
			statement.executeUpdate();

			if(status.equals(Constants.RESOLVED_ISSUE))
			{
				// Get the updated issue's asset and aggregation id
				statement = connection.prepareStatement(SELECT_ISSUE_QUERY);
				statement.setInt(1, issueId);
				resultSet = statement.executeQuery();

				while(resultSet.next()){
					assetId = resultSet.getInt("asset_id");
					aggregationId = resultSet.getInt("aggregation_id");
				}

				statement.close();
				resultSet.close();
				
				// Decrement issue count of asset
				statement = connection.prepareStatement(INC_ISSUE_COUNT_ASSET);
				statement.setInt(1, assetId);
				statement.executeUpdate();


				// If parent aggregation id is null, get it from assets table
				if(aggregationId == 0){
					statement = connection.prepareStatement(ASSET_PARENT_QUERY);
					statement.setInt(1, assetId);
					resultSet = statement.executeQuery();
					while(resultSet.next()){
						aggregationId = resultSet.getInt("aggregation_id");
					}

				}

				// Decrement issue count of the aggregation
				if(aggregationId != 0){
					statement = connection.prepareStatement(DEC_ISSUE_COUNT);
					statement.setInt(1, parentId);
					statement.executeUpdate();
				}


				// Get the parent aggregation until the topmost aggregation is reached, decrement issue count for all.
				while(aggregationId != 0){
					statement = connection.prepareStatement(PARENT_AGGREGATION_QUERY);
					statement.setInt(1, aggregationId);
					resultSet = statement.executeQuery();
					while(resultSet.next()){
						aggregationId = resultSet.getInt("parent_id");
					}
					statement.close();
					resultSet.close();	

					// Update the issue count of parent
					statement = connection.prepareStatement(DEC_ISSUE_COUNT);
					statement.setInt(1, parentId);
					statement.executeUpdate();

				}
			}
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException ex){
			ex.printStackTrace();
		}


	}

	@Override
	public int createLeakIssue(int assetId) {
		int  id = 0;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try{

			// Create leak issue
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(Constants.dbUrl + Constants.dbName, Constants.dbUsername, Constants.dbPassword);
			statement = connection.prepareStatement(INSERT_LEAK_ISSUE, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, assetId);
			statement.setString(2, Constants.LEAK_ISSUE_TYPE);
			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			if(resultSet.next())
				id = resultSet.getInt(1);

			logger.debug("Leak Issue with id " + id + " created.");

			resultSet.close();
			statement = connection.prepareStatement(ASSET_PARENT_QUERY);
			statement.setInt(1, assetId);
			resultSet = statement.executeQuery();
			Integer parentId = 0;


			while(resultSet.next()){
				parentId = resultSet.getInt("aggregation_id");
			}


			statement.close();
			resultSet.close();

			statement = connection.prepareStatement(INC_ISSUE_COUNT_ASSET);
			statement.setInt(1, assetId);
			statement.executeUpdate();
			
			createNotification(parentId, id, Constants.LEAK_ISSUE_TYPE);
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException ex){
			ex.printStackTrace();
		}

		return id;
	}

	public void incrementIssueCount(int assetId){

	}

	public void decrementIssueCount(int assetId){

	}



}
