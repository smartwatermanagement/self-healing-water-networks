package reports;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.android.swn.R;

import utils.Utils;

/**
 * Created by kumudini on 9/23/14.
 */
public class PopulationReportFragment extends Fragment{

    private View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_report_tab, container, false);
        WebView myWebView = (WebView) rootView.findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        Utils.loadChart("water-population-chart.html", myWebView, getActivity());

        return rootView;
    }
}
