package datasimulator.sensordatagenerator.impl;

import java.util.HashMap;
import java.util.Map;

import datasimulator.sensordatagenerator.SensorDataGenerator;

public class FlowSensorDataGenerator extends SensorDataGenerator{
	private final String[] properties = {"flow"};
	private final int[] low = {1};
	private final int[] high = {100};
	private final int[] sensorIds = {45,23,90,11,15};

	@Override
	public Map<String, String> generateDataPoint() {
		
		sensorType = "flow";
		sensorId = sensorIds[(int)(Math.random() * sensorIds.length)];
		Map<String, String> propertyValueMap = new HashMap<String, String>();
		int i = 0;
		
		for(String property: properties){
			propertyValueMap.put(property, "" + (low[i] +  Math.random() * (high[i] - low[i])));
			i++;
		}
		
		return propertyValueMap;
	}

}