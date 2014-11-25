package monitor.components.impl;

import utils.Constants;
import monitor.components.ILeakDetector;

public class DummyLeakDetector implements ILeakDetector{

	@Override
	public void run() {
		
		// Create leak issue
		String assetId = Constants.sensorAssetMap.get(Constants.flowSensorIds[(int)(Math.random() * (Constants.flowSensorIds.length - 1))]);
		(new IssueTracker()).createLeakIssue(Integer.parseInt(assetId));
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}

}
