package dao;

import java.util.List;
import model.Aggregation;
import model.Issue;

public interface IAggregationDAO {
	
	public Aggregation findByIdEager(int id);
	public Aggregation findByIdLazy(int id);
	public List<Aggregation> findAllTopLevelAggregations();
	public List<Aggregation> findAllChildrenAggregations(Aggregation aggregation);
	
	public boolean setAggregation(Aggregation aggregation);

}
