package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.Aggregation;
import model.Notification;

/**
 * Created by kumudini on 11/1/14.
 */
public class JsonParser {

    public static List<Notification> parseNotifications(JSONArray array){
        List<Notification> notificationList = new ArrayList<Notification>();
        return notificationList;
    }

    public static Notification parseNotification(JSONObject array){
        Notification notification = null;
        return notification;
    }

    public static List<Asset> parseNotifications(JSONArray array){
        List<Notification> notificationList = new ArrayList<Notification>();
        return notificationList;
    }

    public static Asset parseNotification(JSONObject array){
        Notification notification = null;
        return notification;
    }
    public static List<Aggregation> parseNotifications(JSONArray array){
        List<Notification> notificationList = new ArrayList<Notification>();
        return notificationList;
    }

    public static Aggregation parseNotification(JSONObject array){
        Notification notification = null;
        return notification;
    }

}
