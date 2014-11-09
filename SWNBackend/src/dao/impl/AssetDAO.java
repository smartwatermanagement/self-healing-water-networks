package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import utils.ConnectionPool;
import model.Aggregation;
import model.Asset;
import model.AssetType;
import dao.IAssetDAO;

public class AssetDAO implements IAssetDAO{

	private static final String findByIdQuery = "SELECT * FROM assets WHERE id=?";
	private static final String findByAggregationQuery = "SELECT * FROM assets WHERE aggregation_id=?";
	private static final String findAllQuery = "SELECT * FROM assets";

	public Asset findById(int id) {

		Asset asset = null;
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(findByIdQuery);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()){
				AssetType type = AssetType.valueOf(resultSet.getString("type").toUpperCase());
				String name = resultSet.getString("name");
				int issueCount = resultSet.getInt("issue_count");
				double latitude = resultSet.getDouble("latitude");
				double longitude = resultSet.getDouble("longitude");
				int aggregationId = resultSet.getInt("aggregation_id");
				asset = new Asset(id, null, name, issueCount, type, latitude, longitude, null, aggregationId);
			}
			asset.setPropertyValueMap(getPropertyValuesOfAsset(id));
			asset.setThresholds((new ThresholdDAO()).findByAsset(asset));
			asset.setSensors((new SensorDAO()).findByAssetId(asset.getId()));

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return asset;
	}

	public List<Asset> findByAggregation(Aggregation aggregation) {

		Connection connection = null;
		PreparedStatement statement = null;
		List<Asset> assets = new LinkedList<>();

		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(findByAggregationQuery);
			statement.setInt(1, aggregation.getId());
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
			
					Asset asset;
					int currentId = resultSet.getInt("id");
					String name = resultSet.getString("name");
					AssetType type = AssetType.valueOf(resultSet.getString("type").toUpperCase());
					int issueCount = resultSet.getInt("issue_count");
					double latitude = resultSet.getDouble("latitude");
					double longitude = resultSet.getDouble("longitude");
					int aggregationId = resultSet.getInt("aggregation_id");
					asset = new Asset(currentId, null, name, issueCount, type, latitude, longitude,null, aggregationId);
					asset.setPropertyValueMap(getPropertyValuesOfAsset(currentId));
					asset.setThresholds((new ThresholdDAO()).findByAsset(asset));
					asset.setSensors((new SensorDAO()).findByAssetId(asset.getId()));
					assets.add(asset);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return assets;
	}

	public List<Asset> findAll() {

		List<Asset> assets = new LinkedList<>();
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(findAllQuery);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
					Asset asset;
					int currentId = resultSet.getInt("id");
					String name = resultSet.getString("name");
					AssetType type = AssetType.valueOf(resultSet.getString("type").toUpperCase());
					int issueCount = resultSet.getInt("issue_count");
					double latitude = resultSet.getDouble("latitude");
					double longitude = resultSet.getDouble("longitude");
					int aggregationId = resultSet.getInt("aggregation_id");
					asset = new Asset(currentId, null, name, issueCount, type, latitude, longitude,null, aggregationId);
					asset.setPropertyValueMap(getPropertyValuesOfAsset(currentId));
					asset.setThresholds((new ThresholdDAO()).findByAsset(asset));
					asset.setSensors((new SensorDAO()).findByAssetId(asset.getId()));
					assets.add(asset);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return assets;
	}
	
	public List<Asset> findByType(String type) {

		Connection connection = null;
		PreparedStatement statement = null;
		List<Asset> assets = new LinkedList<>();
		final String query = "select * from assets where type = ?";
		
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, type);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				int issueCount = resultSet.getInt("issue_count");
				double latitude = resultSet.getDouble("latitude");
				double longitude = resultSet.getDouble("longitude");
				int aggregationId = resultSet.getInt("aggregation_id");
				Asset asset = new Asset(id, null, name, issueCount, AssetType.getType(type), latitude, longitude, null, aggregationId);
				asset.setPropertyValueMap(getPropertyValuesOfAsset(id));
				asset.setThresholds((new ThresholdDAO()).findByAsset(asset));
				asset.setSensors((new SensorDAO()).findByAssetId(asset.getId()));
				assets.add(asset);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return assets;
	}
	
	public List<Asset> findConnectedAssets(int fromId) {
		Connection connection = null;
		PreparedStatement statement = null;
		List<Asset> assets = new LinkedList<>();
		String query = "select to_id from connections where from_id = ?";
		
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, fromId);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				assets.add(findById(resultSet.getInt("to_id")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return assets;
	}
	
	private Map<String, String> getPropertyValuesOfAsset(int id) {
		
		Connection connection = null;
		PreparedStatement statement = null;
		Map<String, String> propertyValues = new HashMap<String, String>();
		String query = "select property, value from asset_property_value_map where asset_id = ?";
		
		try {
		connection = ConnectionPool.getConnection();
		statement = connection.prepareStatement(query);
		statement.setInt(1, id);
		ResultSet resultSet = statement.executeQuery();
		
		while(resultSet.next()){
			propertyValues.put(resultSet.getString("property"), resultSet.getString("value"));
		}
		return propertyValues;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.freeConnection(connection);
		}
		return propertyValues;
	}

}
