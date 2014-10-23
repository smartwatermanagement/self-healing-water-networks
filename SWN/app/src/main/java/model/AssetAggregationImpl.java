package model;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by kumudini on 10/23/14.
 */
public class AssetAggregationImpl implements Aggregation, Serializable {
    private int asset_id;
    private double latitude;
    private double longitude;
    private Map<String, String> propertyValue;
    private int issueCount;
    private AggregationImpl parent;


    public AssetAggregationImpl(int asset_id, double latitude, double longitude,
                                Map<String, String> propertyValue, int issueCount, AggregationImpl parent) {
        this.asset_id = asset_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.propertyValue = propertyValue;
        this.issueCount = issueCount;
        this.parent = parent;
    }
    public int getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(String asset_id) {
        asset_id = asset_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Map<String, String> getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Map<String, String> propertyValue) {
        this.propertyValue = propertyValue;
    }

    public int getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(int issueCount) {
        this.issueCount = issueCount;
    }

    public AggregationImpl getParent() {
        return parent;
    }
}
