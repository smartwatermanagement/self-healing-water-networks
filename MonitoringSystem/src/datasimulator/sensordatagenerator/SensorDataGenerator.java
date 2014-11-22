package datasimulator.sensordatagenerator;

import java.util.Map;

import dao.SensorDAO;

public abstract class SensorDataGenerator implements Runnable{
	
	protected String sensorType;
	protected int sensorId;

	@Override
	public void run() {
		
		while(true){
			Map<String, String> dataPoint = generateDataPoint();
			(new SensorDAO()).insert(sensorType, sensorId, dataPoint);
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
