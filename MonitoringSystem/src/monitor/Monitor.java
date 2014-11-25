package monitor;
import java.util.ArrayList;
import java.util.List;

import monitor.components.IIssueTracker;
import monitor.components.ILeakDetector;
import monitor.components.IThresholdBreachDetector;
import monitor.components.IWaterRequirementPredictor;
import monitor.components.impl.DummyLeakDetector;
import monitor.components.impl.ThresholdBreachDetector;
import monitor.components.impl.DummyWaterRequirementPredictor;
import datasimulator.IDataSimulator;
import datasimulator.impl.DataSimulator;


public class Monitor {
	private IDataSimulator dataSimulator;
	private IIssueTracker issueTracker;
	private ILeakDetector leakDetector;
	private IThresholdBreachDetector thresholdBreachDetector;
	private IWaterRequirementPredictor waterRequirementPredictor;
	
	public Monitor(){
		List<Thread> threads = new ArrayList<Thread>();
			
		Thread thread1 = new Thread(dataSimulator = new DataSimulator());
		Thread thread2 = new Thread(thresholdBreachDetector = new ThresholdBreachDetector());
		Thread thread3 = new Thread(waterRequirementPredictor = new DummyWaterRequirementPredictor());
		Thread thread4 = new Thread(leakDetector = new DummyLeakDetector());
		threads.add(thread1);
		threads.add(thread2);
		threads.add(thread3);
		threads.add(thread4);
		
		for(Thread thread: threads)
			thread.start();
		for(Thread thread: threads){
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		Monitor monitor = new Monitor();
		
	}
	
	

}
