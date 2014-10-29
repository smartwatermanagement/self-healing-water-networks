package dao;

import java.util.List;

import model.Asset;
import model.Threshold;

public interface IThresholdDAO {
	
	public List<Threshold> findAll();
	public List<Threshold> findByAsset(Asset asset);

}