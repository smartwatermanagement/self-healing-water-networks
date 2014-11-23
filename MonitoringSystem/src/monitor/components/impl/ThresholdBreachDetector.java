package monitor.components.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import utils.Constants;
import sensorsDataDao.SensorDataDAO;
import model.Threshold;
import monitor.components.IIssueTracker;
import monitor.components.IThresholdBreachDetector;

public class ThresholdBreachDetector implements IThresholdBreachDetector{
	
	private final String FLOW_SENSOR_TYPE = "flow";
	private final String LEVEL_SENSOR_TYPE = "level";
	private final String QUALITY_SENSOR_TYPE = "quality";
	private IIssueTracker issueTracker = new IssueTracker();

	@Override
	public void run() {
		SensorDataDAO sensorDao = new SensorDataDAO();
		Map<String, String> lastRead = new HashMap<String, String>();

		while(true){
			List<Map<String, Object>> flowData = sensorDao.getDataByDate(FLOW_SENSOR_TYPE, lastRead.get(FLOW_SENSOR_TYPE));
			List<Map<String, Object>> levelData = sensorDao.getDataByDate(LEVEL_SENSOR_TYPE, lastRead.get(LEVEL_SENSOR_TYPE));
			List<Map<String, Object>> qualityData = sensorDao.getDataByDate(QUALITY_SENSOR_TYPE, lastRead.get(QUALITY_SENSOR_TYPE));
			
			// Update last read
			lastRead.put(FLOW_SENSOR_TYPE, flowData.get(flowData.size() - 1).get("time").toString());
			lastRead.put(LEVEL_SENSOR_TYPE, flowData.get(levelData.size() - 1).get("time").toString());
			lastRead.put(QUALITY_SENSOR_TYPE, flowData.get(qualityData.size() - 1).get("time").toString());
			
			// Check for threshold breach
			for(Map<String, Object> flowDataRow : flowData){
				String sensorId = flowDataRow.get("sensor_id").toString();
				String assetId = Constants.sensorAssetMap.get(sensorId);
				Map<String, Threshold> thresholds = Constants.thresholds.get(assetId);
				for(Entry<String, Object> entry:flowDataRow.entrySet()){
					if(!(entry.getKey().equals("sensor_id") || entry.getKey().equals("time"))){
						Threshold threshold = thresholds.get(entry.getKey());
						if(threshold != null && threshold.compare(entry.getValue().toString())){
							issueTracker.createThresholdBreachIssue(threshold, entry.getValue().toString());
						}
					}
					
				}
			}
			
			
		}

	}

}
