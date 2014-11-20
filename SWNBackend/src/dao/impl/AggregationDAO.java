package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Aggregation;
import model.Asset;
import utils.ConnectionPool;
import dao.IAggregationDAO;

public class AggregationDAO implements IAggregationDAO{
	
	private static final String findTopLevelQuery = "SELECT * FROM aggregations WHERE parent_id is NULL";
	private static final String findById = "SELECT * FROM aggregations WHERE id=?";
	private static final String findByParentQuery = "SELECT * FROM aggregations WHERE parent_id=?";

	@Override
	public Aggregation findTopLevelAggregation() {
		Aggregation aggregation = null;
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(findTopLevelQuery);
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()){
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				aggregation = new Aggregation(id, name, null, null, null, null);
				List<Aggregation> children = findAllChildrenAggregations(aggregation);
				aggregation.setChildAggregations(children);
				aggregation.setAssets((new AssetDAO()).findByAggregation(aggregation));
			}
			else
				throw new RuntimeException("No top level aggregation");
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return aggregation;
	}

	@Override
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
				Aggregation childAggregation = new Aggregation(id, name, null, null, null, null);
				List<Aggregation> children = findAllChildrenAggregations(childAggregation);
				childAggregation.setChildAggregations(children);
				childAggregation.setAssets((new AssetDAO()).findByAggregation(childAggregation));
				System.out.println("Finding children aggregations of " + aggregation.getName());
				aggregations.add(childAggregation);
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

	/*
	 * Fill the references to children aggregation and assets., recursively.
	 * Doesn't fill ids. Only use if complete tree of aggregations is required.
	 */
	@Override
	public Aggregation findByIdEager(int id) {
		Aggregation aggregation = null;
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(findById);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				String name = resultSet.getString("name");
				aggregation = new Aggregation(id, name, null, null, null, null);
				List<Aggregation> children = findAllChildrenAggregations(aggregation);
				aggregation.setChildAggregations(children);
				aggregation.setAssets((new AssetDAO()).findByAggregation(aggregation));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return aggregation;
	}
	
	
	/*
	 * Fill the ids, and not the references to the children aggregation and assets.
	 */
	@Override
	public Aggregation findByIdLazy(int id) {
		Aggregation aggregation = null;
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(findById);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				String name = resultSet.getString("name");
				aggregation = new Aggregation(id, name, null, null, null, null);
				aggregation.setAggregationIds(findAllChildrenAggregationIds(aggregation));
				aggregation.setAssetIds(findAllChildrenAssetIds(aggregation));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return aggregation;
	}
	
	private List<Integer> findAllChildrenAggregationIds(Aggregation aggregation){
		List<Integer> aggregationIds = new ArrayList<Integer>();
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(findByParentQuery);
			statement.setInt(1, aggregation.getId());
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				aggregationIds.add(resultSet.getInt("id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return aggregationIds;
		
	}
	private List<Integer> findAllChildrenAssetIds(Aggregation aggregation){
		List<Integer> assetIds = new ArrayList<Integer>();
		List<Asset> assets = (new AssetDAO()).findByAggregation(aggregation);
		for(Asset asset: assets)
			assetIds.add(asset.getId());
		return assetIds;
		
	}
	

}
