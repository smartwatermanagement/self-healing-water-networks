package datasimulator.sensordatagenerator;

import java.util.Map;

import sensorsDataDao.SensorDataDAO;

public abstract class SensorDataGenerator implements Runnable{
	
	@Override
	public void run() {
		
		while(true){
			Map<String, String> dataPoint = generateDataPoint();
			(new SensorDataDAO()).insert(getSensorType(), getSensorId(), dataPoint);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/* 
	 * To Be overridden by subclasses.
	 */
	public abstract Map<String, String> generateDataPoint();
	public abstract String getSensorType();
	public abstract String getSensorId();


}
