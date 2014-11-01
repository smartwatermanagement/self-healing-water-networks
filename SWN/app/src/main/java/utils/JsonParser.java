package utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.IssueState;
import model.Notification;

/**
 * Created by kumudini on 11/1/14.
 */
public class JsonParser {

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
            notification.setRead(object.getBoolean("read"));
            notification.setDate(object.getJSONObject("issue").getString("creationTime"));
           // notification.setDetails(object.getJSONObject("issue").getString("details"));
            notification.setResolvedDate(object.getJSONObject("issue").getString("updationTime"));
            notification.setTitle(object.getJSONObject("issue").getString("type"));
            notification.setStatus(IssueState.valueOf(object.getJSONObject("issue").getString("status").toUpperCase()));
            Log.d("JsonParser", notification.getTitle());
            notification.setImage(Settings.issueImageMap.get(notification.getTitle()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notification;
    }


}
