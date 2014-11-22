package monitor.components;

/*
 * Provides an api to create issues.
 * Also, creates notifications by consulting the subscription table for technicians.
 */
public interface IIssueTracker{
	public int createIssue();

}
