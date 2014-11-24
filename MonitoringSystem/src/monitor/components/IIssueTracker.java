package monitor.components;

import model.Threshold;

/*
 * Provides an api to create issues.
 * Also, creates notifications by consulting the subscription table for technicians.
 */
public interface IIssueTracker{
	public int createThresholdBreachIssue(Threshold threshold, String value);
	public int createWaterRequirementIssue(int requirement, int available, int lastDayUsage);
	public int createLeakIssue(int assetId);
	public void updateIssueStatus(int issueId, String status);

}
