package reports;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.android.swn.R;

import java.util.Calendar;

import utils.Utils;

public class RegionReportFragment extends Fragment {

    private static final String LOG_TAG = RegionReportFragment.class.getSimpleName();


    private View rootView;
    private int datePicker;

    public RegionReportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_region_report, container, false);

        rootView.findViewById(R.id.from).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePicker = R.id.from;
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        rootView.findViewById(R.id.to).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePicker = R.id.to;
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        WebView myWebView = (WebView) rootView.findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        Utils.loadChart("water-usage-chart.html", myWebView, getActivity());

        return rootView;
    }





    public class DatePickerFragment extends DialogFragment implements  DatePickerDialog.OnDateSetListener {

        public DatePickerFragment() {
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            String date = new Integer(day).toString() + "/" + new Integer(month).toString() + "/" + new Integer(year);
            TextView textView = null;
            switch (datePicker) {
                case R.id.from :
                    textView = (TextView) rootView.findViewById(R.id.from);
                    break;
                case R.id.to:
                    textView = (TextView) rootView.findViewById(R.id.to);
                    break;
                default:
                    Log.e(LOG_TAG, "Date being set from unknown view");
            }
            textView.setText(date.toCharArray(), 0, date.length());
        }
    }
}