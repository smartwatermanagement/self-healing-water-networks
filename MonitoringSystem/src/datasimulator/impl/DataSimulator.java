package datasimulator.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import monitor.components.impl.IssueTracker;
import utils.Constants;
import datasimulator.IDataSimulator;
import datasimulator.sensordatagenerator.SensorDataGenerator;
import datasimulator.sensordatagenerator.impl.FlowSensorDataGenerator;
import datasimulator.sensordatagenerator.impl.LevelSensorDataGenerator;
import datasimulator.sensordatagenerator.impl.QualitySensorDataGenerator;

public class DataSimulator extends IDataSimulator{

	@Override
	public void run() {
		sensorDataGenerators.add(new FlowSensorDataGenerator());
		sensorDataGenerators.add(new QualitySensorDataGenerator());
		sensorDataGenerators.add(new LevelSensorDataGenerator());
		List<Thread> threads = new ArrayList<Thread>();
		
		for(SensorDataGenerator sensorDataGenerator: sensorDataGenerators){
			Thread thread = new Thread(sensorDataGenerator);
			threads.add(thread);
			thread.start();
		}
		for(Thread thread: threads){
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	


}
