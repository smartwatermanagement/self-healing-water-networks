package action.nonrest;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Aggregation;
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
	private int aggregationId;

	// Output
	private Map<String, String> usageBreakUp = new HashMap<>();
	private String aggregationName = null;

	// http://localhost:8080/SWNBackend/service/waterUsageBreakUp?aggregationId=2
	public String usageBreakupByAggregation()
	{
		AggregationDAO aggregationDAO = new AggregationDAO();
		Aggregation aggregation = aggregationDAO.findByIdLazy(aggregationId);
		aggregationName = aggregation.getName();

		for (int childAggregationId : aggregation.getAggregationIds())
		{
			Aggregation childAggregation = aggregationDAO
					.findByIdLazy(childAggregationId);
			String usageString = String.valueOf(getUsage(
					childAggregation.getId(), fromDate, toDate));
			if (usageString.equals(String.valueOf(NO_DATA)))
				usageString = "No Data";
			usageBreakUp.put(childAggregation.getName(), usageString);
		}
		return SUCCESS;
	}

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

	/*************************************************************************/

	/**
	 * Calculates water usage for the specified aggregation in the specified
	 * time frame
	 * 
	 * @param aggregationId
	 * @param from
	 * @param to
	 * @return
	 */

	private int getUsage(int aggregationId, Date from, Date to)
	{
		int usage = 0;

		// The nodes through which water flows into this aggregation
		List<SWNNode> entryNodes = WaterNetwork.getInstance().getEntryNodes(
				aggregationId);
		if (entryNodes.isEmpty())
			throw new RuntimeException(
					"SWN configuration error : No water entry points for aggregation "
							+ (new AggregationDAO())
									.findByIdLazy(aggregationId).getName());

		for (SWNNode entryNode : entryNodes)
		{
			int tmpUsage = getUsage(entryNode, from, to);
			if (tmpUsage == NO_DATA)
				return NO_DATA;
			usage += tmpUsage;
		}
		return usage;
	}

	/**
	 * Calculates water usage w.r.t to the flow through the specified node
	 * 
	 * @param entryNode
	 * @param from
	 * @param to
	 * @return
	 */
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

	public int getAggregationId()
	{
		return aggregationId;
	}

	public void setAggregationId(int aggregationId)
	{
		this.aggregationId = aggregationId;
	}
}