package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import utils.ConnectionPool;
import model.Asset;
import model.Issue;
import model.Notification;
import model.Threshold;
import dao.IIssueDAO;
import dao.IThresholdDAO;

public class ThresholdDAO implements IThresholdDAO{
	
	private static final String findById = "SELECT * FROM thresholds WHERE id=?"; 
	private static final String findByAssetQuery = "SELECT * FROM thresholds WHERE asset_id=?";


	public List<Threshold> findByAsset(Asset asset) {
		List<Threshold> thresholds = new ArrayList<Threshold>();
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(findByAssetQuery);
			statement.setInt(1, asset.getId());
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				int id = resultSet.getInt("id");
				String property = resultSet.getString("property");
				String value = resultSet.getString("value");
				String operator = resultSet.getString("operator");
				thresholds.add(new Threshold(id, property, value, operator));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			ConnectionPool.freeConnection(connection);
		}
		return thresholds;
	}


	public Threshold findById() {
		// TODO Auto-generated method stub
		return null;
	}

}
