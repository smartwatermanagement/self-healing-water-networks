package utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	
	// Sensor database settings
	public final static String sensorDbUrl = "http://localhost:8086";
	public final static String sensorDbUsername = "root";
	public final static String sensorDbPassword = "root";
	public final static String sensorDbName = "sensordata";
	
	// Sensor Ids
	public static String[] flowSensorIds;
	public static String[] levelSensorIds;
	public static String[] qualitySensorIds;
	
	//Sensor id - Asset Id Map
	public static Map<String, String> sensorAssetMap = new HashMap<String, String>();
}
