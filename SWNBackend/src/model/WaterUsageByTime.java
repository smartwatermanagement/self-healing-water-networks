package model;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

public class WaterUsageByTime extends ActionSupport
{
	List<Date> dates = new LinkedList<>();
	List<Double> usage = new LinkedList<>();
	public List<Date> getDates()
	{
		return dates;
	}
	public void setDates(List<Date> dates)
	{
		this.dates = dates;
	}
	public List<Double> getUsage()
	{
		return usage;
	}
	public void setUsage(List<Double> usage)
	{
		this.usage = usage;
	}
}
