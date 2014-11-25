package utils;

import android.net.Uri;

import java.util.Calendar;

/**
 * Created by kempa on 12/11/14.
 */
public class BackendURI {
    public static final String SCHEME = "http";
    public static String AUTHORITY = "192.168.13.72";
    public static final String PORT = "8080";
    public static final String APP = "SWNBackend";

    public static final String GET_ASSETS = "asset.json";
    public static final String GET_TOP_AGGREGATION = "aggregation.json";
    public static final String GET_USAGE_BY_STORAGE = "service/usageBreakUpByStorage";
    public static final String GET_USAGE_BY_STORAGE_AND_AGGREGATION = "service/usageBreakUpByStorageAndAggregation";
    public static final String GET_USAGE_TRENDS = "service/usageTrends";
    public static final String NOTIFICATION = "notification.json";
    public static final String AGGREGATION = "aggregation.json";

    private static String getURL(String path) {
        return SCHEME + "://" + AUTHORITY + ":" + PORT + "/" + APP + "/" + path;
    }

    public static String getAssetsURI() {
        return getURL(GET_ASSETS);
    }

    public static String getTopAggregationURI() {
        return getURL(GET_TOP_AGGREGATION);
    }

    public static String getGetUsageByStorageURI(int storageId, String from, String to) {

        // TODO Roll up and roll down the to and from date as influxdb does not have <= and >= operators. Need to shift this to SensorsDAO
        Calendar c = Utils.getCalender(from, Utils.REST_API_DATE_FORMAT);
        c.roll(Calendar.DATE, false);
        from = Utils.getDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH), Utils.REST_API_DATE_FORMAT);
        c = Utils.getCalender(to, Utils.REST_API_DATE_FORMAT);
        c.roll(Calendar.DATE, true);
        to = Utils.getDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH), Utils.REST_API_DATE_FORMAT);

        return Uri.parse(getURL(GET_USAGE_BY_STORAGE)).buildUpon()
                .appendQueryParameter("storageId", String.valueOf(storageId))
                .appendQueryParameter("fromDate", from)
                .appendQueryParameter("toDate", to)
                .toString();
    }

    public static String getGetUsageByStorageAndAggregationURI(int aggregationId, String from, String to) {

        // TODO Roll up and roll down the to and from date as influxdb does not have <= and >= operators. Need to shift this to SensorsDAO
        Calendar c = Utils.getCalender(from, Utils.REST_API_DATE_FORMAT);
        c.roll(Calendar.DATE, false);
        from = Utils.getDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH), Utils.REST_API_DATE_FORMAT);
        c = Utils.getCalender(to, Utils.REST_API_DATE_FORMAT);
        c.roll(Calendar.DATE, true);
        to = Utils.getDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH), Utils.REST_API_DATE_FORMAT);

        return Uri.parse(getURL(GET_USAGE_BY_STORAGE_AND_AGGREGATION)).buildUpon()
                .appendQueryParameter("aggregationId", String.valueOf(aggregationId))
                .appendQueryParameter("fromDate", from)
                .appendQueryParameter("toDate", to)
                .toString();
    }

    public static String getNotificationURI(){
        return getURL(NOTIFICATION);
    }
    public static String getNotificationDeleteURI(int id){
        return getURL("notification/" + id + ".json");
    }
    public static String getNotificationUpdateURI(int id){
        return getURL("notification/" + id + ".xml");
    }
    public static String getAggregationURI(){
        return getURL(AGGREGATION);
    }

    public static String getUsageTrendsByStorage(int storageId, String from, String to) {

        // TODO Roll up and roll down the to and from date as influxdb does not have <= and >= operators. Need to shift this to SensorsDAO
        Calendar c = Utils.getCalender(from, Utils.REST_API_DATE_FORMAT);
        c.roll(Calendar.DATE, false);
        from = Utils.getDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH), Utils.REST_API_DATE_FORMAT);
        c = Utils.getCalender(to, Utils.REST_API_DATE_FORMAT);
        c.roll(Calendar.DATE, true);
        to = Utils.getDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH), Utils.REST_API_DATE_FORMAT);

        return Uri.parse(getURL(GET_USAGE_TRENDS)).buildUpon()
                .appendQueryParameter("storageId", String.valueOf(storageId))
                .appendQueryParameter("fromDate", from)
                .appendQueryParameter("toDate", to)
                .toString();
    }
}
