package reports;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.swn.R;

public class ReportsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_reports_tabs, container, false);

        FragmentTabHost tabHost = (FragmentTabHost) rootView.findViewById(R.id.tabHost);
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Region"),
                RegionReportFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Population"),
                PopulationReportFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Time"),
                TimeReportFragment.class, null);
        return tabHost;

    }
}
