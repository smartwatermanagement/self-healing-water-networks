package utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.Asset;
import model.IssueState;
import model.Notification;

/**
 * Created by kumudini on 11/1/14.
 */
public class JsonParser {

    private static final String LOG_TAG = JsonParser.class.getSimpleName();
    public static List<Notification> parseNotifications(JSONArray array){
        List<Notification> notificationList = new ArrayList<Notification>();
        for(int i = 0; i < array.length();i++){
            try {
                Notification notification = parseNotification(array.getJSONObject(i));
                notificationList.add(notification);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return notificationList;
    }

    public static Notification parseNotification(JSONObject object){
        Notification notification = new Notification();
        try {
            notification.setId(object.getInt("id"));
            notification.setRead(object.getBoolean("read"));
            notification.setDate(object.getJSONObject("issue").getString("creationTime"));
           // notification.setDetails(object.getJSONObject("issue").getString("details"));
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
