package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import utils.ConnectionPool;
import model.Aggregation;
import model.Asset;
import model.Issue;
import model.IssueType;
import model.Status;
import dao.IIssueDAO;

public class IssueDAO implements IIssueDAO{
	
	private static final String findAllQuery = "SELECT * FROM issues";
	private static final String findByIdQuery = "SELECT * FROM issues WHERE id=?";

	public Issue findById(int id) {
		Issue issue = null;
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(findByIdQuery);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()){
				IssueType type = IssueType.valueOf(resultSet.getString("type"));
				Status status = Status.valueOf(resultSet.getString("status"));
				Timestamp creationTime = resultSet.getTimestamp("created_at");
				Timestamp updationTime = resultSet.getTimestamp("updated_at");
				String details = resultSet.getString("details");
				int assetId = resultSet.getInt("asset_id");
				int aggregationId = resultSet.getInt("aggregation_id");
				issue = new Issue(id, type, status, creationTime, updationTime, details, assetId, aggregationId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return issue;
	}

	public List<Issue> findAll() {
		List<Issue> issues = new ArrayList<Issue>();
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(findAllQuery);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				int id = resultSet.getInt("id");
				IssueType type = IssueType.valueOf(resultSet.getString("type"));
				Status status = Status.valueOf(resultSet.getString("status"));
				Timestamp creationTime = resultSet.getTimestamp("created_at");
				Timestamp updationTime = resultSet.getTimestamp("updated_at");
				String details = resultSet.getString("details");
				Asset asset = (new AssetDAO()).findById(resultSet.getInt("asset_id"));
				int assetId = resultSet.getInt("asset_id");
				int aggregationId = resultSet.getInt("aggregation_id");
				Issue issue = new Issue(id, type, status, creationTime, updationTime, details, assetId, aggregationId);
				issues.add(issue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return issues;
	}


}
