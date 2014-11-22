package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDBFactory;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Serie;

import utils.Constants;


public class SensorDAO {
	
	private final String SENSOR_ID = "sensor_id";
	private final String QUERY = "SELECT * FROM @ WHERE sensor_id = @ and time > @ and time < @";
	
	public void insert(String sensorType, String sensorId, Map<String, String> propertyValueMap){
		
		InfluxDB influxDB = InfluxDBFactory.connect(Constants.sensorDbUrl, Constants.sensorDbUsername, Constants.sensorDbPassword);
		String dbName = Constants.sensorDbName;

		Object[] keyObjects = propertyValueMap.keySet().toArray();
		String[] keys = new String[keyObjects.length + 1];
		keys[0] = SENSOR_ID;
		
		List<String> values = new ArrayList<>(propertyValueMap.values());
		values.add(0, sensorId);

		
		for(int i = 1; i < keys.length; i++){
			keys[i] = keyObjects[i - 1].toString();
			System.out.println(" key " + keys[i] + " " + i);
		}
		
		Serie serie = new Serie.Builder(sensorType)
		            .columns(keys)
		            .values(values.toArray())
		            .build();
		
		influxDB.write(dbName, TimeUnit.MILLISECONDS, serie);
	}
	
	public List<Map<String, Object>> getDataByDate(String sensorType, int sensorId, String startDate, String endDate){
		
		
		InfluxDB influxDB = InfluxDBFactory.connect(Constants.sensorDbUrl, Constants.sensorDbUsername, Constants.sensorDbPassword);
		String dbName = Constants.sensorDbName;
		
		String query = QUERY.replaceFirst("@", sensorType);
		query = query.replaceFirst("@", "'" + sensorId + "'");
		query = query.replaceFirst("@", "'" + startDate + "'");
		query = query.replaceFirst("@", "'" + endDate + "'");
		
		System.out.println("Query is " + query);
		
		List<Serie> result = influxDB.query(
				dbName,
				query,
				TimeUnit.MILLISECONDS);
		
		if(result.get(0) == null)
			return null;
		return result.get(0).getRows();
		
	}
	
	
	/*public static void main(String[] args){
		Map<String, String>  propertyValueMap = new HashMap<String, String>();
		propertyValueMap.put("flow", "20");
		(new SensorDAO()).insert("flow", "2", propertyValueMap);
		List<Map<String, Object>> result = (new SensorDAO()).getDataByDate("flow", 20, "2013-08-12 23:32:01", "2013-08-12 23:32:01");
		for(Map<String, Object> map: result){
			for (Map.Entry<String, Object> entry : map.entrySet())
			{
				System.out.println(entry.getKey() + " " + entry.getValue());
			}
		}
	}*/
	

}
