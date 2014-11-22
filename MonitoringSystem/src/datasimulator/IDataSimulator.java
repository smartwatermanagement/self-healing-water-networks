package datasimulator;
import java.util.ArrayList;
import java.util.List;

import datasimulator.sensordatagenerator.SensorDataGenerator;


public abstract class IDataSimulator implements Runnable{
	protected List<SensorDataGenerator> sensorDataGenerators = new ArrayList<SensorDataGenerator>();
	

}
