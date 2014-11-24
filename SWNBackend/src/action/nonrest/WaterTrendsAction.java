package action.nonrest;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.SWNNode;
import model.SensorType;
import model.WaterNetwork;
import sensorsDataDao.SensorDataDAO;

import com.opensymphony.xwork2.ActionSupport;

public class WaterTrendsAction extends ActionSupport
{
	/**
	 * Action method which gives the water usage trend for the given storage id
	 */

	// Input parameters
	private int storageId;
	private String fromDate;
	private String toDate;

	// Output
	private Collection<TrendPoint> usageTrends = new LinkedList<>();

	public Collection<TrendPoint> getUsageTrends()
	{
		return usageTrends;
	}

	public void setUsageTrends(Collection<TrendPoint> usageTrends)
	{
		this.usageTrends = usageTrends;
	}

	// http://localhost:8080/SWNBackend/service/waterTrends?storageId=2
	public String execute()
	{
		// Get node having asset with given storage id
		SWNNode node = WaterNetwork.getInstance().getNode(storageId);
		if (node == null)
			return SUCCESS;

		HashMap<Date, Integer> trends = usageTrends(node, fromDate,
				toDate, new HashMap<Date, Integer>());
		List<Date> dates = new LinkedList<>(trends.keySet());
		Collections.sort(dates);
		for (Date date : dates)
			usageTrends.add(new TrendPoint(date, trends.get(date)));
		return SUCCESS;
	}

	private HashMap<Date, Integer> usageTrends(SWNNode entryNode,
			String fromDate, String toDate, HashMap<Date, Integer> trends)
	{
		// Base case
		if (entryNode.getAsset().hasFlowSensor())
		{
			System.out.println("Flow sensor : " + entryNode.getAsset().flowSensorId());
			List<Map<String, Object>> flowData = (new SensorDataDAO())
					.getDataByDate(SensorType.FLOW.label(), entryNode
							.getAsset().flowSensorId(), fromDate, toDate);
			System.out.println(flowData.size() + " entries");

			for (Map<String, Object> dataPoint : flowData)
			{
				Date date = new Date(
						((Double) dataPoint.get("time")).longValue());
				int flow = Integer.parseInt((String) dataPoint
						.get(SensorType.FLOW.label()));
				if (trends.get(date) != null)
					trends.put(date, flow);
				else
					trends.put(date, trends.get(date) + flow);
			}

			return trends;
		}
		else if (entryNode.getChildren().isEmpty())
			return trends;

		// Recursively find trends
		for (SWNNode node : entryNode.getChildren())
		{
			trends = usageTrends(node, fromDate, toDate, trends);
		}
		return trends;
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
}

class TrendPoint
{
	private Date date;

	public TrendPoint(Date date, int usage)
	{
		super();
		this.date = date;
		this.usage = usage;
	}
	
	public String toString()
	{
		return "date : " + date.toString() + ", usage : " + usage;
	}

	public int getUsage()
	{
		return usage;
	}

	public void setUsage(int usage)
	{
		this.usage = usage;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	private int usage;
}