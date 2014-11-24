package monitor.components.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

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
	private final Logger logger = Logger.getLogger(getClass());

	@Override
	public void run() {
		SensorDataDAO sensorDao = new SensorDataDAO();
		Map<String, String> lastRead = new HashMap<String, String>();
		lastRead.put(FLOW_SENSOR_TYPE, "1416676524101");
		lastRead.put(LEVEL_SENSOR_TYPE, "1416676524101");
		lastRead.put(QUALITY_SENSOR_TYPE, "1416676524101");

		while(true){
			List<Map<String, Object>> flowData = sensorDao.getDataByDate(FLOW_SENSOR_TYPE, lastRead.get(FLOW_SENSOR_TYPE));
			List<Map<String, Object>> levelData = sensorDao.getDataByDate(LEVEL_SENSOR_TYPE, lastRead.get(LEVEL_SENSOR_TYPE));
			List<Map<String, Object>> qualityData = sensorDao.getDataByDate(QUALITY_SENSOR_TYPE, lastRead.get(QUALITY_SENSOR_TYPE));

		/*	logger.debug( flowData.size() + " rows of flow data, " + levelData.size() + " rows of level data, " + qualityData.size() +
					" rows of quality data read");*/
			
			Long lastReadFlow = latestRecordTime(flowData);
			Long lastReadLevel = latestRecordTime(levelData);
			Long lastReadQuality = latestRecordTime(qualityData);
			
			
			// Update last read
			if(!lastReadFlow.toString().equals(lastRead.get(FLOW_SENSOR_TYPE)))
				lastRead.put(FLOW_SENSOR_TYPE, lastReadFlow.toString());
			else flowData = null;
			if(!lastReadLevel.toString().equals(lastRead.get(LEVEL_SENSOR_TYPE)))
				lastRead.put(LEVEL_SENSOR_TYPE, lastReadLevel.toString());
			else levelData = null;
			if(!lastReadQuality.toString().equals(lastRead.get(QUALITY_SENSOR_TYPE)))
				lastRead.put(QUALITY_SENSOR_TYPE, lastReadQuality.toString());
			else qualityData = null;
			
			/*logger.debug(" Last Read updated " + " flow - " + lastRead.get(FLOW_SENSOR_TYPE) + " level- " + lastRead.get(LEVEL_SENSOR_TYPE)
					+ " quality- " + lastRead.get(QUALITY_SENSOR_TYPE));*/

			// Check for threshold breach
			if(flowData != null){
				for(Map<String, Object> flowDataRow : flowData){
					String sensorId = flowDataRow.get("sensor_id").toString();
					String assetId = Constants.sensorAssetMap.get(sensorId);
					Map<String, Threshold> thresholds = Constants.thresholds.get(assetId);
					if(thresholds != null){
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

			if(levelData != null){
				for(Map<String, Object> levelDataRow : levelData){
					String sensorId = levelDataRow.get("sensor_id").toString();
					String assetId = Constants.sensorAssetMap.get(sensorId);
					Map<String, Threshold> thresholds = Constants.thresholds.get(assetId);
					if(thresholds != null){
						for(Entry<String, Object> entry:levelDataRow.entrySet()){
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

			if(qualityData != null){
				for(Map<String, Object> qualityDataRow : qualityData){
					String sensorId = qualityDataRow.get("sensor_id").toString();
					String assetId = Constants.sensorAssetMap.get(sensorId);
					Map<String, Threshold> thresholds = Constants.thresholds.get(assetId);
					if(thresholds != null){
						for(Entry<String, Object> entry:qualityDataRow.entrySet()){
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

	}
	
	private Long latestRecordTime(List<Map<String, Object>> records){
		Long result = 0L;
		
		for(Map<String, Object> record: records){
			Long temp = new Double((Double)record.get("time")).longValue();
			if( temp > result)
				result = temp;
		}
		return result;
	}
	
	

}
