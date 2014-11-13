package action.nonrest;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import model.SWNNode;
import model.WaterNetwork;

import com.opensymphony.xwork2.ActionSupport;

import dao.impl.AggregationDAO;

public class WaterUsageBreakUpAction extends ActionSupport
{
	private final static int NO_DATA = -1;

	/**
	 * Action method which gives the break up of water usage amongst child
	 * aggregations
	 */

	// Input parameters
	private int storageId;
	private Date fromDate;
	private Date toDate;

	// Output
	private Map<String, String> usageBreakUp = new HashMap<>();
	private String aggregationName = null;

	// http://localhost:8080/SWNBackend/service/waterUsageBreakUp?storageId=2
	public String usageBreakUp()
	{
		// Get node having asset with given storage id
		SWNNode node = WaterNetwork.getInstance().getNode(storageId);
		if (node == null)
			return ERROR;

		aggregationName = (new AggregationDAO()).findByIdLazy(
				node.getAsset().getAggregationId()).getName();

		for (SWNNode childNode : node.getChildren())
		{
			String usageString = String.valueOf(getUsage(childNode, fromDate,
					toDate));
			if (usageString.equals(String.valueOf(NO_DATA)))
				usageString = "No Data";
			usageBreakUp.put(
					(new AggregationDAO()).findByIdLazy(
							childNode.getAsset().getAggregationId()).getName(),
					usageString);
		}
		return SUCCESS;
	}

	private int getUsage(SWNNode entryNode, Date from, Date to)
	{
		// Base case
		if (entryNode.getAsset().hasFlowSensor())
		{
			return 20;
			// TODO : ImplementSensorsDataDAO
			/*
			 * return (new SensorsDataDAO()).getFlowData(entryNode.getAsset()
			 * .getFlowSensorId(), fromDate, toDate);
			 */
		}
		else if (entryNode.getChildren().isEmpty())
			return NO_DATA;

		// Recursively find the usage
		int usage = 0;
		for (SWNNode node : entryNode.getChildren())
		{
			int tmpUsage = getUsage(node, from, to);
			if (tmpUsage == NO_DATA)
				return NO_DATA;
			usage += tmpUsage;
		}
		return usage;
	}

	public String getAggregationName()
	{
		return aggregationName;
	}

	public void setAggregationname(String aggregationName)
	{
		this.aggregationName = aggregationName;
	}

	public Map<String, String> getUsageBreakUp()
	{
		return usageBreakUp;
	}

	public void setUsageBreakUp(Map<String, String> usageBreakUp)
	{
		this.usageBreakUp = usageBreakUp;
	}

	public int getStorageId()
	{
		return storageId;
	}

	public void setStorageId(int storageId)
	{
		this.storageId = storageId;
	}

	public Date getFromDate()
	{
		return fromDate;
	}

	public void setFromDate(Date fromDate)
	{
		this.fromDate = fromDate;
	}

	public Date getToDate()
	{
		return toDate;
	}

	public void setToDate(Date toDate)
	{
		this.toDate = toDate;
	}
}