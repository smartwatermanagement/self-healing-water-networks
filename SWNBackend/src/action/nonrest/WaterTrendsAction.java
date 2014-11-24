package action.nonrest;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
	private LinkedHashMap<String, Integer> trends = new LinkedHashMap<>();

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

		HashMap<String, Integer> trends = usageTrends(node, fromDate,
				toDate, new HashMap<String, Integer>());
		List<String> dates = new LinkedList<>(trends.keySet());
		Collections.sort(dates);
		for (String date : dates)
			//usageTrends.add(new TrendPoint(date, trends.get(date)));
			this.trends.put(date, trends.get(date));
		return SUCCESS;
	}

	private HashMap<String, Integer> usageTrends(SWNNode entryNode,
			String fromDate, String toDate, HashMap<String, Integer> trends)
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
				double flow = Double.parseDouble((String) dataPoint
						.get(SensorType.FLOW.label()));
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String dateString = simpleDateFormat.format(date);
				
				if (trends.get(dateString) == null)
					trends.put(dateString, (int)flow);
				else
					trends.put(dateString, trends.get(dateString) + (int)flow);
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

	public LinkedHashMap<String, Integer> getTrends()
	{
		return trends;
	}

	public void setTrends(LinkedHashMap<String, Integer> trends)
	{
		this.trends = trends;
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