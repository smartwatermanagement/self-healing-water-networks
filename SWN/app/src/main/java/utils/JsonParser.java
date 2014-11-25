package utils;

import android.content.Context;
import android.util.Log;

import com.example.android.swn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.Aggregation;
import model.Asset;
import model.IAggregation;
import model.IssueState;
import model.LeakNotificationDetails;
import model.Notification;
import model.ThresholdBreachNotificationDetails;
import model.WaterRequirementNotificationDetails;

/**
 * Created by kumudini on 11/1/14.
 */
public class JsonParser {

    private static final String LOG_TAG = JsonParser.class.getSimpleName();


    public static List<Notification> parseNotifications(JSONArray array, Context context){
        List<Notification> notificationList = new ArrayList<Notification>();
        for(int i = 0; i < array.length();i++){
            try {
                Notification notification = parseNotification(array.getJSONObject(i), context);
                notificationList.add(notification);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return notificationList;
    }

    public static Notification parseNotification(JSONObject object, Context context){
        Notification notification = new Notification();
        try {
            notification.setId(object.getInt("id"));
            notification.setRead(object.getBoolean("read"));
            notification.setDate(object.getJSONObject("issue").getString("creationTime"));
            if(object.getJSONObject("issue").getString("type").equals(context.getString(R.string.threshold_issue_type))) {
                notification.setDetails(parseThresholdDetails(new JSONObject(object.getJSONObject("issue").getString("details")), context));
            }
            else if(object.getJSONObject("issue").getString("type").equals(context.getString(R.string.water_requirement_prediction_issue_type))) {
                notification.setDetails(parseWaterRequirementDetails(new JSONObject(object.getJSONObject("issue").getString("details")), context));
            }
            else if(object.getJSONObject("issue").getString("type").equals(context.getString(R.string.leak_issue_type))){
                notification.setDetails(new LeakNotificationDetails(context,object.getJSONObject("issue").getInt("assetId") + ""));
            }
            notification.setResolvedDate(object.getJSONObject("issue").getString("updationTime"));
            notification.setTitle(Settings.issueTypeTitleMap.get(object.getJSONObject("issue").getString("type")));
            notification.setStatus(IssueState.valueOf(object.getJSONObject("issue").getString("status").toUpperCase()));
            Log.d("JsonParser", notification.getTitle());
            notification.setImage(Settings.issueImageMap.get(object.getJSONObject("issue").getString("type")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notification;
    }

    public static ThresholdBreachNotificationDetails parseThresholdDetails(JSONObject object, Context context){
        ThresholdBreachNotificationDetails details = new ThresholdBreachNotificationDetails();
        try {
            details.setContext(context);
            details.setCurrentValue(object.getString("current_value"));
            details.setProperty(object.getString("property"));
            details.setThreshold(object.getString("value"));
            details.setAsset(Integer.parseInt(object.getString("asset_id")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return details;
    }

    public static WaterRequirementNotificationDetails parseWaterRequirementDetails(JSONObject object, Context context){
        WaterRequirementNotificationDetails details = null;
        try {
            details = new WaterRequirementNotificationDetails(context, object.getString("last_day_usage"), object.getString("requirement"), object.getString("available"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return details;
    }

    public static Aggregation parseAggregation(JSONObject object){

        Aggregation aggregation = new Aggregation();
        try {
            aggregation.setId(object.getInt("id"));
            aggregation.setIssueCount(object.getInt("issueCount"));
            aggregation.setName(object.getString("name"));
            Log.d("JSONParser", "parsing object " + aggregation.getName());
            List<IAggregation> aggregations = parseAggregationArray(object.getJSONArray("childAggregations"));
            for(int i = 0; i < aggregations.size(); i++)
                ((Aggregation)aggregations.get(i)).setParent(aggregation);

            List<Asset> assets = parseAssets(object.getJSONArray("assets"));
            for(Asset asset:assets) {
                asset.setParent(aggregation);
                aggregations.add(asset);
            }

            aggregation.setChildren(aggregations);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return aggregation;
    }
    public static List<IAggregation> parseAggregationArray(JSONArray array){
        List<IAggregation> aggregations = new ArrayList<IAggregation>();
        for(int i = 0; i < array.length(); i++){
            try {
                aggregations.add(parseAggregation(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return aggregations;
    }


    public static Asset parseAsset(JSONObject object) {
        Asset asset = null;

        try {
            int asset_id = object.getInt("id");
            double latitude = object.getDouble("latitude");
            double longitude = object.getDouble("longitude");
            String name = object.getString("name");
            String type = object.getString("type");
            int issueCount = object.getInt("issueCount");
            asset = new Asset(asset_id,  latitude, longitude, null, issueCount, null, type, name);
            Log.d(LOG_TAG, "Parsing asset " + asset.getName());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return asset;
    }

    public static List<Asset> parseAssets(JSONArray array) {
        List<Asset> assetList = new ArrayList<Asset>();
        for(int i = 0; i < array.length();i++){
            try {
                Asset asset = parseAsset(array.getJSONObject(i));
                assetList.add(asset);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return assetList;
    }
}
