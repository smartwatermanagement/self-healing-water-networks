package datasimulator.sensordatagenerator;

import java.util.Map;

import sensorsDataDao.SensorDataDAO;

public abstract class SensorDataGenerator implements Runnable{
	
	protected String sensorType;
	protected String sensorId;

	@Override
	public void run() {
		
		while(true){
			Map<String, String> dataPoint = generateDataPoint();
			(new SensorDataDAO()).insert(sensorType, sensorId, dataPoint);
			try {
				Thread.sleep(1000000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/* 
	 * To Be overridden by subclasses.
	 */
	public abstract Map<String, String> generateDataPoint();


}
