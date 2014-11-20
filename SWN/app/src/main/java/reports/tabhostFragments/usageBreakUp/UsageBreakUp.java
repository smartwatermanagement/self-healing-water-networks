package reports.tabhostFragments.usageBreakUp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.swn.R;

import java.util.List;

import model.Aggregation;
import model.Asset;
import reports.asyncTask.StorageFetchTask;
import reports.asyncTask.UsageFetchTask;
import reports.tabhostFragments.usageBreakUp.subFragments.Filter;
import reports.tabhostFragments.usageBreakUp.subFragments.PieChart;
import utils.BackendURI;

public class UsageBreakUp extends Fragment implements PieChart.OnPieChartFragmentInteractionListener,
        Filter.OnFilterFragmentInteractionListener {

    private static final String LOG_TAG = UsageBreakUp.class.getSimpleName();
    private View rootView;

    private Filter filter;
    private PieChart pieChart;

    public UsageBreakUp() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_usage_break_up, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (rootView.findViewById(R.id.piechart_container) != null) {
            if (savedInstanceState == null) {
                // TODO

                pieChart = new PieChart();
                getChildFragmentManager().beginTransaction()
                        .add(R.id.piechart_container, pieChart).commit();
            }
        }

        filter = new Filter();
        new StorageFetchTask(new StorageFetchTask.StorageFetchTaskCompletionListener() {
            @Override
            public void onStorageFetchTaskCompletionListener(List<Asset> storageAssets) {
                filter.populateStorageFilter(storageAssets);
            }
        }).execute(BackendURI.getAssetsURI());
        getChildFragmentManager().beginTransaction().add(R.id.filter, filter).commit();
    }

    @Override
    public void onPieChartFragmentInteraction(int aggregationId) {
        final android.support.v4.app.FragmentManager childFragmentManager = getChildFragmentManager();
        new UsageFetchTask(new UsageFetchTask.UsageFetchTaskCompletionListener() {
            @Override
            public void onUsageFetchTaskCompletionListener(String aggregationName,
                                                         List<Aggregation> childAggregations) {
                if (childAggregations.size() > 0) {
                    final PieChart pieChart = new PieChart();
                    FragmentTransaction transaction = childFragmentManager.beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.fragment_pie_chart, pieChart).commit();
                    childFragmentManager.executePendingTransactions();// TODO : Why is this needed?
                    pieChart.populate(aggregationName, childAggregations);
                }
                else
                    Toast.makeText(getActivity(), R.string.no_details, Toast.LENGTH_SHORT).show();

            }
        }).execute(BackendURI.getGetUsageByStorageAndAggregationURI(aggregationId));
    }

    @Override
    public void onFilterFragmentInteraction(int storageId, String from, String to) {
        new UsageFetchTask(new UsageFetchTask.UsageFetchTaskCompletionListener() {
            @Override
            public void onUsageFetchTaskCompletionListener(String aggregationName,
                                                         List<Aggregation> childAggregations) {
                if (childAggregations.size() > 0)
                    pieChart.populate(aggregationName, childAggregations);
                else
                    Toast.makeText(getActivity(), R.string.no_details, Toast.LENGTH_SHORT).show();

            }
        }).execute(BackendURI.getGetUsageByStorageURI(storageId, from, to));
    }

    /*
    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment = (fm.findFragmentById(R.id.fragment_filter));
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }*/
}