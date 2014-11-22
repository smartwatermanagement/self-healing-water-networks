package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
	
	
	static{
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dbUrl + dbName, dbUsername, dbPassword);
            statement = connection.createStatement();
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
            }
            flowSensorIds = flowSensors.toArray(new String[flowSensors.size()]);
            levelSensorIds = levelSensors.toArray(new String[levelSensors.size()]);
            qualitySensorIds = qualitySensors.toArray(new String[qualitySensors.size()]);

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
