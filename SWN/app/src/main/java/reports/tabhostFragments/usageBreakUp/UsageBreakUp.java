package reports.tabhostFragments.usageBreakUp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import utils.Utils;

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
        pieChart = new PieChart();
        getChildFragmentManager().beginTransaction()
                .add(R.id.piechart_container, pieChart).commit();

        filter = new Filter();
        final ProgressDialog  progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(true);
        progressBar.setMessage(getString(R.string.storage_fetch_in_progress));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
        new StorageFetchTask(new StorageFetchTask.StorageFetchTaskCompletionListener() {
            @Override
            public void onStorageFetchTaskCompletionListener(List<Asset> storageAssets) {
                filter.populateStorageFilter(storageAssets);
                progressBar.dismiss();
            }
        }).execute(BackendURI.getAssetsURI());
        getChildFragmentManager().beginTransaction().add(R.id.filter, filter).commit();
    }

    @Override
    public void onPieChartFragmentInteraction(int aggregationId) {
        // TODO: Back button not working : Android bug

        View filterView = getChildFragmentManager().findFragmentById(R.id.filter).getView();
        String from = ((TextView) filterView.findViewById(R.id.from)).getText().toString();
        String to = ((TextView) filterView.findViewById(R.id.to)).getText().toString();

        final ProgressDialog  progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(true);
        progressBar.setMessage(getString(R.string.usage_breakup_fetch_in_progress));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
        new UsageFetchTask(new UsageFetchTask.UsageFetchTaskCompletionListener() {
            @Override
            public void onUsageFetchTaskCompletionListener(String aggregationName,
                                                         List<Aggregation> childAggregations) {
                if (childAggregations.size() > 0) {
                    UsageBreakUp.this.pieChart = new PieChart();

                    android.support.v4.app.FragmentManager childFragmentManager = UsageBreakUp.this.getChildFragmentManager();
                    FragmentTransaction transaction = childFragmentManager.beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.setBreadCrumbTitle(aggregationName);
                    transaction.replace(R.id.piechart_container, pieChart);
                    transaction.commit();
                    childFragmentManager.executePendingTransactions();// TODO : Why is this needed?
                    UsageBreakUp.this.pieChart.populate(aggregationName, childAggregations);
                    progressBar.dismiss();
                }
                else
                    Toast.makeText(getActivity(), R.string.no_details, Toast.LENGTH_SHORT).show();

            }
        }).execute(BackendURI.getGetUsageByStorageAndAggregationURI(aggregationId,
                Utils.getDateString(from, Utils.APP_DATE_FORMAT, Utils.REST_API_DATE_FORMAT),
                Utils.getDateString(to, Utils.APP_DATE_FORMAT, Utils.REST_API_DATE_FORMAT)));
    }


    @Override
    public void onFilterFragmentInteraction(int storageId, String from, String to) {

        final ProgressDialog  progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(true);
        progressBar.setMessage(getString(R.string.usage_breakup_fetch_in_progress));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
        new UsageFetchTask(new UsageFetchTask.UsageFetchTaskCompletionListener() {
            @Override
            public void onUsageFetchTaskCompletionListener(String aggregationName,
                                                         List<Aggregation> childAggregations) {
                if (childAggregations.size() > 0)
                    pieChart.populate(aggregationName, childAggregations);
                else
                    Toast.makeText(getActivity(), R.string.no_details, Toast.LENGTH_SHORT).show();
                progressBar.dismiss();

            }
        }).execute(BackendURI.getGetUsageByStorageURI(storageId, from, to));
    }
}
