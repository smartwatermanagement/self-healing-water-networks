package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
				AssetType type = AssetType.valueOf(resultSet.getString("type"));
				int issueCount = resultSet.getInt("issue_count");
				double latitude = resultSet.getDouble("latitude");
				double longitude = resultSet.getDouble("longitude");
				asset = new Asset(id, null, issueCount, type, latitude, longitude,null);
				propertyValues.put(resultSet.getString("property"), resultSet.getString("values"));
			}
			
			while(resultSet.next()){
				propertyValues.put(resultSet.getString("property"), resultSet.getString("values"));
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

	public List<Asset> findAssestsByAggregation(Aggregation aggregation) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Asset> findAllAssets() {
		// TODO Auto-generated method stub
		return null;
	}

}
