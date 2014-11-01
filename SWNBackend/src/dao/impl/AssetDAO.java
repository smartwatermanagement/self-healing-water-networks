package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
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
		Map<String, String> propertyValues = new HashMap<String, String>();
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(findByIdQuery);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()){
				AssetType type = AssetType.valueOf(resultSet.getString("type").toUpperCase());
				int issueCount = resultSet.getInt("issue_count");
				double latitude = resultSet.getDouble("latitude");
				double longitude = resultSet.getDouble("longitude");
				asset = new Asset(id, null, issueCount, type, latitude, longitude,null);
				propertyValues.put(resultSet.getString("property"), resultSet.getString("value"));
			}

			while(resultSet.next()){
				propertyValues.put(resultSet.getString("property"), resultSet.getString("value"));
			}
			asset.setPropertyValueMap(propertyValues);
			asset.setThresholds((new ThresholdDAO()).findByAsset(asset));

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return asset;
	}

	public List<Asset> findByAggregation(Aggregation aggregation) {

		Map<Integer, Asset> assetsMap = new HashMap<Integer, Asset>();
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(findByAggregationQuery);
			statement.setInt(1, aggregation.getId());
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				if(!assetsMap.containsKey(resultSet.getInt("id"))){
					Asset asset;
					Map<String, String> propertyValues = new HashMap<String, String>();
					int currentId = resultSet.getInt("id");
					AssetType type = AssetType.valueOf(resultSet.getString("type").toUpperCase());
					int issueCount = resultSet.getInt("issue_count");
					double latitude = resultSet.getDouble("latitude");
					double longitude = resultSet.getDouble("longitude");
					asset = new Asset(currentId, null, issueCount, type, latitude, longitude,null);
					propertyValues.put(resultSet.getString("property"), resultSet.getString("value"));
					asset.setPropertyValueMap(propertyValues);
					asset.setThresholds((new ThresholdDAO()).findByAsset(asset));
					assetsMap.put(currentId, asset);
				}
				else{
					assetsMap.get(resultSet.getInt("id")).getPropertyValueMap()
					.put(resultSet.getString("property"), resultSet.getString("value"));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return new ArrayList<Asset>(assetsMap.values());
	}

	public List<Asset> findAll() {

		Map<Integer, Asset> assetsMap = new HashMap<Integer, Asset>();
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(findAllQuery);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				if(!assetsMap.containsKey(resultSet.getInt("id"))){
					Asset asset;
					Map<String, String> propertyValues = new HashMap<String, String>();
					int currentId = resultSet.getInt("id");
					AssetType type = AssetType.valueOf(resultSet.getString("type").toUpperCase());
					int issueCount = resultSet.getInt("issue_count");
					double latitude = resultSet.getDouble("latitude");
					double longitude = resultSet.getDouble("longitude");
					asset = new Asset(currentId, null, issueCount, type, latitude, longitude,null);
					propertyValues.put(resultSet.getString("property"), resultSet.getString("value"));
					asset.setPropertyValueMap(propertyValues);
					asset.setThresholds((new ThresholdDAO()).findByAsset(asset));
					assetsMap.put(currentId, asset);
				}
				else{
					assetsMap.get(resultSet.getInt("id")).getPropertyValueMap()
					.put(resultSet.getString("property"), resultSet.getString("value"));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return new ArrayList<Asset>(assetsMap.values());
	}

}
