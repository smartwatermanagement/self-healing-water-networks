package utils;

/**
 * Created by kempa on 12/11/14.
 */
public class BackendURI {
    public static final String PROTOCOL = "http";
    public static final String HOST = "192.168.13.2";
    public static final String PORT = "8080";
    public static final String APP = "SWNBackend";
    public static final String JSON_EXT = "json";

    public static final String GET_ASSETS = "asset.json";
    public static final String GET_TOP_AGGREGATION = "aggregation.json";
    public static final String GET_USAGE = "service/usageBreakUp";

    private static String getPrefix() {
        if (PORT.equals(""))
            return PROTOCOL + "://" + HOST + "/" + APP + "/";
        else
            return PROTOCOL + "://" + HOST + ":" + PORT + "/"+ APP + "/";
    }

    public static String getAssetsURI() {
        return getPrefix() + GET_ASSETS;
    }

    public static String getTopAggregationURI() {
        return getPrefix() + GET_TOP_AGGREGATION;
    }

    public static String getUsageURI(int storageId) { return getPrefix() + GET_USAGE + "?storageId=" + String.valueOf(storageId); }
}
