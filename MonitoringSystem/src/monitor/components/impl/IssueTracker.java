package monitor.components.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.Constants;
import model.Threshold;
import monitor.components.IIssueTracker;

public  class IssueTracker implements IIssueTracker{

	private final String INSERT_THRESHOLD_ISSUE = "INSERT INTO issues(asset_id, type, details) VALUES(?,?,?)";
	private final String INSERT_NOTIFICATION = "INSERT INTO notifications(user_id, issue_id) VALUES(?, ?)";
	private final String SUBSCRIPTIONS_QUERY = "SELECT user_id FROM subsription WHERE issueType = ? and aggregation_id = ?";
	private final String PARENT_AGGREGATION_QUERY = "SELECT parent_id FROM aggregations WHERE id=?";
	private final String ASSET_PARENT_QUERY = "SELECT parent_id FROM aggregations WHERE id in (SELECT aggregation_id FROM"
			+ "assets WHERE asset_id = ?)";

	@Override
	public int createThresholdBreachIssue(Threshold threshold, String value)  {
		int id = 0;

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<String> parentAggregations = new ArrayList<String>();

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(Constants.dbUrl + Constants.dbName, Constants.dbUsername, Constants.dbPassword);
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

			while(parentId != null){
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
					parentId = resultSet.getInt("parent_id");
					if(parentId != null)
						parentAggregations.add(parentId + "");
				}
				statement.close();
				resultSet.close();
				
			}
			
			


		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		



		return id;
	}


}
