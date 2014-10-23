package model;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by kumudini on 10/23/14.
 */
public class AssetAggregationImpl implements Aggregation, Serializable {
    private String Asset_id;
    private String latitude;
    private String longitude;
    private Map<String, String> propertyValue;
    private int issueCount;
    private AggregationImpl parent;


    public AssetAggregationImpl(String asset_id, String latitude, String longitude,
                                Map<String, String> propertyValue, int issueCount, AggregationImpl parent) {
        Asset_id = asset_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.propertyValue = propertyValue;
        this.issueCount = issueCount;
        this.parent = parent;
    }

    public String getAsset_id() {
        return Asset_id;
    }

    public void setAsset_id(String asset_id) {
        Asset_id = asset_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
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
