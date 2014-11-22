package datasimulator.sensordatagenerator.impl;

import java.util.HashMap;
import java.util.Map;

import datasimulator.sensordatagenerator.SensorDataGenerator;

public class LevelSensorDataGenerator extends SensorDataGenerator{
	
	private final String[] properties = {"level"};
	private final int[] low = {1};
	private final int[] high = {100};
	private final int[] sensorIds = {100,13,90,14};


	@Override
	public Map<String, String> generateDataPoint() {
		
		sensorType = "level";
		sensorId = sensorIds[(int)(Math.random() * sensorIds.length)];
		Map<String, String> propertyValueMap = new HashMap<String, String>();
		int i = 0;
		
		for(String property: properties){
			propertyValueMap.put(property, "" + (low[i] + Math.random() * (high[i] - low[i])));
			i++;
		}
		
		return propertyValueMap;
	}

}
