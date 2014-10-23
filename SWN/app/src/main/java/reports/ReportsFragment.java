package reports;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.swn.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import model.DummyDataCreator;

public class ReportsFragment extends Fragment {


    private static final String LOG_TAG = ReportsFragment.class.getSimpleName();
    private int datePicker;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_reports_tabs, container, false);

        FragmentTabHost tabHost = (FragmentTabHost) rootView.findViewById(R.id.tabHost);
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("By Aggregation"),
                AggregationBasedReportFragment.class, DummyDataCreator.getDummyDataForAggregationReports());
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("By Time"),
                TimeBasedReportFragment.class, DummyDataCreator.getDummyDataForTimeReports());

        rootView.findViewById(R.id.from).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePicker = R.id.from;
                DialogFragment dialogFragment = new FromToDatePickerFragment();
                dialogFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        rootView.findViewById(R.id.to).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePicker = R.id.to;
                DialogFragment dialogFragment = new FromToDatePickerFragment();
                dialogFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.storage_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        rootView.findViewById(R.id.storage_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment storageDetailsDialogFragment = new StorageDetailsDialogFragment();
                storageDetailsDialogFragment.show(getActivity().getSupportFragmentManager(), "storageDetails");
            }
        });

        rootView.findViewById(R.id.water_quality_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment waterQualityDetailsDialogFragment = new WaterQualityDetailsDialogFragment();
                waterQualityDetailsDialogFragment.show(getActivity().getSupportFragmentManager(), "waterQualityDetails");
            }
        });

        rootView.findViewById(R.id.location_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Uri geoLocation = Uri.parse("geo:0.0?").buildUpon().appendQueryParameter("q", "12.844759,77.662399(Main Tank)").appendQueryParameter("z", "23").build();

                    Intent mapIntent = new Intent();
                    mapIntent.setAction(Intent.ACTION_VIEW);

                    mapIntent.setData(geoLocation);
                    if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                    else {
                        Log.e(LOG_TAG, "No suitable application to open specified location ");
                    }
            }
        });
        return tabHost;
    }

    public class FromToDatePickerFragment extends DialogFragment implements  DatePickerDialog.OnDateSetListener {

        public FromToDatePickerFragment() {
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
            String dateString = null;
            try {
                SimpleDateFormat fromFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat toFormat = new SimpleDateFormat("MMM dd, yyyy");
                dateString = toFormat.format(fromFormat.parse(new Integer(day).toString() + "/" + new Integer(month).toString() + "/" + new Integer(year)));
            } catch (ParseException e) {
                // This shouldn't happen as we are using a date picker
                e.printStackTrace();
            }

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
            textView.setText(dateString.toCharArray(), 0, dateString.length());
        }
    }
}
