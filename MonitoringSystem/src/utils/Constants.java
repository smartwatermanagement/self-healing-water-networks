package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import model.Threshold;

public class Constants {
	
	// Sensor database settings
	public final static String sensorDbUrl = "http://localhost:8086";
	public final static String sensorDbUsername = "root";
	public final static String sensorDbPassword = "root";
	public final static String sensorDbName = "sensordata";
	
	// Database settings
	public final static String dbUrl = "jdbc:mysql://localhost:3306/";
	public final static String dbUsername = "root";
	public final static String dbPassword = "123456";
	public final static String dbName = "swndb";
	
	// Sensor Ids
	public static String[] flowSensorIds;
	public static String[] levelSensorIds;
	public static String[] qualitySensorIds;
	
	//Sensor id - Asset Id Map
	public static Map<String, String> sensorAssetMap = new HashMap<String, String>();
	
	// Thresholds 
	public static Map<String, Map<String, Threshold>> thresholds = new HashMap<String, Map<String, Threshold>>();
	
	// Logger
	final static Logger logger = Logger.getLogger(Constants.class);
	
	// Issue types in database
	public final static String THRESHOLD_ISSUE_TYPE = "threshold_breach";
	
	
	
	static{
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dbUrl + dbName, dbUsername, dbPassword);
            statement = connection.createStatement();
            
            // Read Sensor Ids.
            resultSet = statement.executeQuery("SELECT * FROM sensors");
            List<String> flowSensors = new ArrayList<String>();
            List<String> levelSensors = new ArrayList<String>();
            List<String> qualitySensors = new ArrayList<String>();
            
            while(resultSet.next()){
            	if(resultSet.getString("type").equals("flow"))
            		flowSensors.add(resultSet.getInt("id") + "");
            	else if(resultSet.getString("type").equals("level"))
            		levelSensors.add(resultSet.getInt("id") + "");
            	else if(resultSet.getString("type").equals("quality"))
            		qualitySensors.add(resultSet.getInt("id") + "");
            	sensorAssetMap.put(resultSet.getInt("id") + "",resultSet.getInt("asset_id") + "");
            }
            flowSensorIds = flowSensors.toArray(new String[flowSensors.size()]);
            levelSensorIds = levelSensors.toArray(new String[levelSensors.size()]);
            qualitySensorIds = qualitySensors.toArray(new String[qualitySensors.size()]);
            
            logger.debug( flowSensors.size() + " Flow sensor ids read.");
            logger.debug( levelSensors.size() + " Level sensor ids read.");
            logger.debug( qualitySensors.size() + " Quality sensor ids read.");
            
            resultSet.close();
            
            int count = 0;
            
            // Read Thresholds
            resultSet = statement.executeQuery("SELECT * FROM thresholds");
            while(resultSet.next()){
            	Map<String, Threshold> thresholdMap;
            	if((thresholdMap = thresholds.get(resultSet.getInt("asset_id") + "")) == null)
            		thresholdMap = new HashMap<String, Threshold>();
            	thresholdMap.put(resultSet.getString("property"), new Threshold(resultSet.getInt("id"),
            			resultSet.getInt("asset_id"),resultSet.getString("property"),
            			resultSet.getString("operator"),resultSet.getString("value")));
            	
            	thresholds.put(resultSet.getInt("asset_id") + "", thresholdMap);
            	count++;
            }
            logger.debug(count + " thresholds read.");
            

        } catch (SQLException ex) {
        	ex.printStackTrace();

        }catch(ClassNotFoundException ex){
        	ex.printStackTrace();
        }
		finally {
            try {
                if (resultSet != null) {
                	resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException ex) {
            	ex.printStackTrace();
            }
        }
	}

	
	
}
