package monitor;
import monitor.components.IIssueTracker;
import monitor.components.ILeakDetector;
import monitor.components.IThresholdBreachDetector;
import monitor.components.IWaterRequirementPredictor;
import datasimulator.IDataSimulator;
import datasimulator.impl.DataSimulator;


public class Monitor {
	private IDataSimulator dataSimulator;
	private IIssueTracker issueTracker;
	private ILeakDetector leakDetector;
	private IThresholdBreachDetector thresholdBreachDetector;
	private IWaterRequirementPredictor waterRequirementPredictor;
	
	public Monitor(){
		dataSimulator = new DataSimulator();
		dataSimulator.run();
	}
	
	public static void main(String[] args){
		Monitor monitor = new Monitor();
		
	}
	
	

}
