package datasimulator.sensordatagenerator.impl;

import java.util.HashMap;
import java.util.Map;

import datasimulator.sensordatagenerator.SensorDataGenerator;

public class QualitySensorDataGenerator extends SensorDataGenerator {
	private final String[] properties = {"Dissolved Oxygen", "pH", "ORP", "tubidity", "temperature", "nitrates", "conductivity", "chlorine"};
	private final int[] low = {1,2,3,10,9,11,12,5};
	private final int[] high = {100,100,50,40,30,20,35,50};
	private final int[] sensorIds = {1,2,10,3,23,11};

	
	@Override
	public Map<String, String> generateDataPoint() {
		
		sensorType = "quality";
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
