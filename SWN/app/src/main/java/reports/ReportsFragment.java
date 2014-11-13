package reports;

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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.swn.R;

import java.util.Calendar;
import java.util.Date;

import model.Aggregation;
import model.DummyDataCreator;
import model.IAggregation;
import reports.asyncTask.StorageFetcher;
import utils.BackendURI;
import utils.Utils;

public class ReportsFragment extends Fragment implements
        AggregationBasedReportFragment.OnAggregationPieSelectedListener{

    private static final String LOG_TAG = ReportsFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View rootView = inflater.inflate(R.layout.fragment_reports_tabs, container, false);

        // Set up the two tabs
        FragmentTabHost tabHost = (FragmentTabHost) rootView.findViewById(R.id.tabHost);
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("By Aggregation"),
                AggregationBasedReportFragment.class, new DummyDataCreator().getDummyDataForAggregationReports());
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("By Time"),
                TimeBasedReportFragment.class, new DummyDataCreator().getDummyDataForTimeReports());

        setUpFromDateFilter(rootView);

        setUpToDateFilter(rootView);

        setUpStorageFilter(rootView);

        setUpStorageDetails(rootView);

        return tabHost;
    }

    /**
     * Called by the RegionReportFragment when a 'pie' in the piechart representing the water usage across aggregations is selected
     * The aggregation for which a new piechart encolsed in a RegionReportFragment must be drawn
     */
    @Override
    public void onAggregationPieSelected(IAggregation IAggregation) {
        Aggregation aggregation = (Aggregation) IAggregation;
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.setBreadCrumbTitle(aggregation.getParent().getName());

        AggregationBasedReportFragment aggregationBasedReportFragment = new AggregationBasedReportFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("aggregation", aggregation);
        aggregationBasedReportFragment.setArguments(bundle);
        transaction.replace(R.id.regionreport, (Fragment) (aggregationBasedReportFragment)).commit();
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

                DialogFragment dialogFragment = new DatePickerFragment(fromDatetextView, year, month, day);
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

                DialogFragment dialogFragment = new DatePickerFragment(toDatetextView, year, month, day);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });
    }

    /**
     * Initialize and set listeners for the storage filter
     * @param rootView
     */
    private void setUpStorageFilter(View rootView) {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        new StorageFetcher(adapter).execute(BackendURI.getAssetsURI());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view;
                Toast.makeText(getActivity(), "Click detected", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), textView.getText(), Toast.LENGTH_SHORT).show();
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
                } else {
                    Log.e(LOG_TAG, "No suitable application to open specified location ");
                }
            }
        });
    }
}
