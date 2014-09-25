package utils;

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by kumudini on 9/23/14.
 */
public class Utils {


    private static  final String LOG_TAG = "utils";
    public static final String ASSET_PATH = "file:///android_asset/";

    public static void loadChart(String fileName, WebView myWebView, Activity activity) {
        String content = null;
        try {
            AssetManager assetManager = activity.getAssets();
            InputStream in = assetManager.open(fileName);
            byte[] bytes = readFully(in,activity);
            content = new String(bytes, "UTF-8");
        } catch (IOException e) {
            Log.e(LOG_TAG, "An error occurred.", e);
        }
        myWebView.loadDataWithBaseURL(ASSET_PATH, content, "text/html", "utf-8", null);
        myWebView.requestFocusFromTouch();
    }

    public static byte[] readFully(InputStream in, Activity activity) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String buf;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while((buf = bufferedReader.readLine()) != null) {
            out.write(buf.getBytes());
        }
        return out.toByteArray();
    }
}
