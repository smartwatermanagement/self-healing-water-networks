package reports.tabhostFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.swn.R;

import model.DummyDataCreator;
import reports.tabhostFragments.usageBreakUp.UsageBreakUp;
import reports.tabhostFragments.usageTrends.UsageTrends;

public class Reports extends Fragment {

    private static final String LOG_TAG = Reports.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View rootView = inflater.inflate(R.layout.fragment_reports, container, false);

        // Set up the two tabs
        FragmentTabHost tabHost = (FragmentTabHost) rootView.findViewById(R.id.tabHost);
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(getString(R.string.usage_breakUp_tab_title)),
                UsageBreakUp.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator(getString(R.string.usage_trend_tab_title)),
                UsageTrends.class, new DummyDataCreator().getDummyDataForTimeReports());

        return tabHost;
    }
/*
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
    }*/
}
