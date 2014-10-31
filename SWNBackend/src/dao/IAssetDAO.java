package dao;

import java.util.List;

import model.Aggregation;
import model.Asset;
import model.Issue;

public interface IAssetDAO {
	
	public Asset findById(int id);
	public List<Asset> findAssestsByAggregation(Aggregation aggregation);
	public List<Asset> findAll();
	
	

}
