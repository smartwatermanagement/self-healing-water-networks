package utils;

import android.net.Uri;

/**
 * Created by kempa on 12/11/14.
 */
public class BackendURI {
    public static final String SCHEME = "http";
    public static final String AUTHORITY = "192.168.13.2";
    public static final String PORT = "8080";
    public static final String APP = "SWNBackend";

    public static final String GET_ASSETS = "asset.json";
    public static final String GET_TOP_AGGREGATION = "aggregation.json";
    public static final String GET_USAGE = "service/usageBreakUp";

    private static String getURL(String path) {
        return SCHEME + "://" + AUTHORITY + ":" + PORT + "/" + APP + "/" + path;
    }

    public static String getAssetsURI() {
        return getURL(GET_ASSETS);
    }

    public static String getTopAggregationURI() {
        return getURL(GET_TOP_AGGREGATION);
    }

    public static String getUsageURI(int storageId) {
        return Uri.parse(getURL(GET_USAGE)).buildUpon().appendQueryParameter("storageId", String.valueOf(storageId)).toString();
    }
}
