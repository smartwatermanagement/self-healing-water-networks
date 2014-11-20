package dao;

import java.util.List;

import model.Aggregation;

public interface IAggregationDAO {
	
	public Aggregation findByIdEager(int id);
	public Aggregation findByIdLazy(int id);
	public Aggregation findTopLevelAggregation();
	public List<Aggregation> findAllChildrenAggregations(Aggregation aggregation);
	
	public boolean setAggregation(Aggregation aggregation);

}
