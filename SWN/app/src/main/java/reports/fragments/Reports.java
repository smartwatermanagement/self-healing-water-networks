package reports.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.swn.R;

import java.util.Calendar;
import java.util.Date;

import lecho.lib.hellocharts.view.PieChartView;
import model.DummyDataCreator;
import reports.fragments.asyncTask.StorageFetcher;
import reports.fragments.asyncTask.UsageFetcher;
import reports.fragments.dialogFragments.DatePicker;
import reports.fragments.dialogFragments.StorageDetails;
import reports.fragments.dialogFragments.WaterQualityDetails;
import reports.fragments.subFragments.UsageBreakUp;
import reports.fragments.subFragments.UsageTrends;
import utils.BackendURI;
import utils.StorageArrayAdapter;
import utils.Utils;

public class Reports extends Fragment implements
        UsageBreakUp.OnAggregationPieSelectedListener{

    private static final String LOG_TAG = Reports.class.getSimpleName();
    private Reports self = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View rootView = inflater.inflate(R.layout.fragment_reports, container, false);

        // Set up the two tabs
        FragmentTabHost tabHost = (FragmentTabHost) rootView.findViewById(R.id.tabHost);
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        setUpFromDateFilter(rootView);

        setUpToDateFilter(rootView);

        setUpStorageFilter(rootView);

        setUpStorageDetails(rootView);

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("By Aggregation"),
                UsageBreakUp.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("By Time"),
                UsageTrends.class, new DummyDataCreator().getDummyDataForTimeReports());

        return tabHost;
    }

    /**
     * Called by the RegionReportFragment when a 'pie' in the piechart representing the water usage across aggregations is selected
     * The aggregation for which a new piechart enclosed in a RegionReportFragment must be drawn
     */
    @Override
    public void onAggregationPieSelected(int aggregationId) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        UsageBreakUp usageBreakUp = new UsageBreakUp();
        Bundle bundle = new Bundle();
        bundle.putSerializable("aggregationId", aggregationId);
        usageBreakUp.setArguments(bundle);
        transaction.replace(R.id.regionreport, usageBreakUp).commit();
    }

    /**
     * Initialize and set listeners for the the 'from' date field
     * @param rootView
     */
    private void setUpFromDateFilter(View rootView) {
        final TextView fromDatetextView = (TextView) rootView.findViewById(R.id.from);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        fromDatetextView.setText(Utils.getFormattedDateString(c.getTime()));
        rootView.findViewById(R.id.from).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // max date
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int day = c.get(Calendar.DAY_OF_MONTH);

                DialogFragment dialogFragment = new DatePicker(fromDatetextView, year, month, day);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });
    }

    private void setUpToDateFilter(View rootView) {
        final TextView toDatetextView = (TextView) rootView.findViewById(R.id.to);
        toDatetextView.setText(Utils.getFormattedDateString(new Date()));
        toDatetextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //max fate
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int day = c.get(Calendar.DAY_OF_MONTH);

                DialogFragment dialogFragment = new DatePicker(toDatetextView, year, month, day);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });
    }

    /**
     * Initialize and set listeners for the storage filter
     * @param rootView
     */
    private void setUpStorageFilter(final View rootView) {
        final StorageArrayAdapter<String> storageArrayAdapter = new StorageArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        storageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        spinner.setAdapter(storageArrayAdapter);
        new StorageFetcher(storageArrayAdapter).execute(BackendURI.getAssetsURI());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                PieChartView pieChartView = (PieChartView)rootView.findViewById(R.id.piechart);
                new UsageFetcher(pieChartView, self).execute(BackendURI.getGetUsageByStorageURI(storageArrayAdapter.getStorageId(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(), "Nothing selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpStorageDetails(View rootView) {
        rootView.findViewById(R.id.storage_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment storageDetailsDialogFragment = new StorageDetails();
                storageDetailsDialogFragment.show(getActivity().getSupportFragmentManager(), "storageDetails");
            }
        });


        rootView.findViewById(R.id.water_quality_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment waterQualityDetailsDialogFragment = new WaterQualityDetails();
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
                } else {
                    Log.e(LOG_TAG, "No suitable application to open specified location ");
                }
            }
        });
    }
}
