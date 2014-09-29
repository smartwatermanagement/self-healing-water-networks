package reports;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.android.swn.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RegionReportFragment extends Fragment {

    private static final String LOG_TAG = RegionReportFragment.class.getSimpleName();
    public static final String ASSET_PATH = "file:///android_asset/";

    public RegionReportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_region_report, container, false);

        WebView myWebView = (WebView) rootView.findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);

        //Utils.loadChart("water-usage-chart.html", myWebView, getActivity()); Not working

        loadChart(myWebView);
        return rootView;
    }

    private void loadChart(WebView myWebView) {
        String content = null;
        try {
            AssetManager assetManager = getActivity().getAssets();
            InputStream in = assetManager.open("water-usage-chart.html");
            byte[] bytes = readFully(in);
            content = new String(bytes, "UTF-8");
        } catch (IOException e) {
            Log.e(LOG_TAG, "An error occurred.", e);
        }
        myWebView.loadDataWithBaseURL(ASSET_PATH, content, "text/html", "utf-8", null);
        myWebView.requestFocusFromTouch();
    }

    private byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }

}