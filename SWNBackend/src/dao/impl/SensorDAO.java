package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import utils.ConnectionPool;
import model.Sensor;
import model.SensorType;

public class SensorDAO
{
	public List<Sensor> findByAssetId(int assetId)
	{
		List<Sensor> sensors = new LinkedList<>();
		String query = "select id, type from sensors where asset_id = ?";
		Connection connection = null;
		PreparedStatement stmt = null;
		try
		{
			connection = ConnectionPool.getConnection();
			stmt = connection.prepareStatement(query);
			stmt.setInt(1, assetId);
			ResultSet resultSet = stmt.executeQuery();
			while(resultSet.next())
			{
				int id = resultSet.getInt("id");
				SensorType type = SensorType.getSensorType(resultSet.getString("type"));
				sensors.add(new Sensor(id, type));
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			ConnectionPool.freeConnection(connection);
		}
		return sensors;
	}

}
