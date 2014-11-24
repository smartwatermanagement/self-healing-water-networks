package monitor.components.impl;

import monitor.components.IWaterRequirementPredictor;

public class WaterRequirementPredictor implements IWaterRequirementPredictor{

	@Override
	public void run() {
		
		while(true){
			int lastDayUsage = (int)(Math.random() * 1000000);
			int predictedRequirement = (int)(Math.random() * 1000000);
			int currentAvailable = (int)(Math.random() * 1000000);
			(new IssueTracker()).createWaterRequirementIssue(predictedRequirement, currentAvailable, lastDayUsage);
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
