package action.nonrest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sensorsDataDao.SensorDataDAO;
import model.Aggregation;
import model.SWNNode;
import model.SensorType;
import model.WaterNetwork;
import action.nonrest.jsonModel.JSONAggregationUsage;

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
	private String fromDate;
	private String toDate;
	private int aggregationId;

	// Output
	private Map<Integer, JSONAggregationUsage> usageBreakUp = new HashMap<>();
	private String aggregationName = null;

	//http://192.168.13.2:8080/SWNBackend/service/usageBreakUpByStorageAndAggregation?aggregationId=2
	public String usageBreakUpByStorageAndAggregation()
	{
		AggregationDAO aggregationDAO = new AggregationDAO();
		Aggregation aggregation = aggregationDAO.findByIdLazy(aggregationId);
		
		if (aggregation == null)
			return SUCCESS;
		
		aggregationName = aggregation.getName();
		for (int childAggregationId : aggregation.getAggregationIds())
		{
			Aggregation childAggregation = aggregationDAO
					.findByIdLazy(childAggregationId);
			String usageString = String.valueOf(getUsage(
					childAggregation.getId(), fromDate, toDate));
			if (usageString.equals(String.valueOf(NO_DATA)))
				usageString = "No Data";
			usageBreakUp.put(childAggregation.getId(), new JSONAggregationUsage(childAggregation.getName(), usageString));
		}
		return SUCCESS;
	}

	// http://localhost:8080/SWNBackend/service/waterUsageBreakUp?storageId=2
	public String usageBreakUpByStorage()
	{
		// Get node having asset with given storage id
		SWNNode node = WaterNetwork.getInstance().getNode(storageId);
		if (node == null)
			return SUCCESS;

		aggregationName = (new AggregationDAO()).findByIdLazy(
				node.getAsset().getAggregationId()).getName();
		for (SWNNode childNode : node.getChildren())
		{
			String usageString = String.valueOf(getUsage(childNode, fromDate,
					toDate));
			if (usageString.equals(String.valueOf(NO_DATA)))
				usageString = "No Data";
			Aggregation aggregation = (new AggregationDAO())
					.findByIdLazy(childNode.getAsset().getAggregationId());
			usageBreakUp.put(aggregation.getId(), new JSONAggregationUsage(aggregation.getName(), usageString));

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

	private int getUsage(int aggregationId, String from, String to)
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
	private int getUsage(SWNNode entryNode, String from, String to)
	{
		// Base case
		if (entryNode.getAsset().hasFlowSensor())
		{
			List<Map<String, Object>> flowData = (new SensorDataDAO()).getDataByDate(SensorType.FLOW.label(), 
					entryNode.getAsset().flowSensorId(), from, to);
			
			int flow = 0;
			for (Map<String, Object> dataPoint : flowData) {
				flow += Double.parseDouble((String) dataPoint.get(SensorType.FLOW.label()));
			}
			
			return flow;
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

	public Map<Integer, JSONAggregationUsage> getUsageBreakUp()
	{
		return usageBreakUp;
	}

	public void setUsageBreakUp(Map<Integer, JSONAggregationUsage> usageBreakUp)
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

	public String getFromDate()
	{
		return fromDate;
	}

	public void setFromDate(String fromDate)
	{
		this.fromDate = fromDate;
	}

	public String getToDate()
	{
		return toDate;
	}

	public void setToDate(String toDate)
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