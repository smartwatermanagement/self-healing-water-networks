package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import utils.ConnectionPool;
import model.Aggregation;
import model.Issue;
import model.Notification;
import dao.IAggregationDAO;
import dao.IIssueDAO;

public class AggregationDAO implements IAggregationDAO{
	
	private static final String findTopLevelQuery = "SELECT * FROM aggregations WHERE parent_id is NULL";
	private static final String findById = "SELECT * FROM aggregations WHERE id=?";
	private static final String findByParentQuery = "SELECT * FROM aggregations WHERE parent_id=?";

	public List<Aggregation> findAllTopLevelAggregations() {
		List<Aggregation> aggregations = new ArrayList<Aggregation>();
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(findTopLevelQuery);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				Aggregation aggregation = new Aggregation(id, name, null, null);
				List<Aggregation> children = findAllChildrenAggregations(aggregation);
				aggregation.setAggregations(children);
				aggregation.setAssets((new AssetDAO()).findAssestsByAggregation(aggregation));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return aggregations;
	}

	public List<Aggregation> findAllChildrenAggregations(Aggregation aggregation) {
		List<Aggregation> aggregations = new ArrayList<Aggregation>();
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(findByParentQuery);
			statement.setInt(1, aggregation.getId());
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				Aggregation childAggregation = new Aggregation(id, name, null, null);
				List<Aggregation> children = findAllChildrenAggregations(childAggregation);
				childAggregation.setAggregations(children);
				childAggregation.setAssets((new AssetDAO()).findAssestsByAggregation(childAggregation));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return aggregations;
	}

	public boolean setAggregation(Aggregation aggregation) {
		return false;
	}

	public Aggregation findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
