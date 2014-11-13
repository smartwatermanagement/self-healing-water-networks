package utils;

/**
 * Created by kempa on 12/11/14.
 */
public class BackendURI {
    public static final String PROTOCOL = "http";
    public static final String HOST = "192.16.13.2";
    public static final String PORT = "8080";
    public static final String APP = "SWNBackend";
    public static final String JSON_EXT = "json";

    public static final String GET_ASSETS = "asset.json";

    private static String getPrefix() {
        if (PORT.equals(""))
            return PROTOCOL + "://" + HOST + "/" + APP + "/";
        else
            return PROTOCOL + "://" + HOST + ":" + PORT + "/"+ APP + "/";
    }

    public static String getAssetsURI() {
        return getPrefix() + GET_ASSETS;
    }
}
