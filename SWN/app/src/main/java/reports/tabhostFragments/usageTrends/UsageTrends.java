package reports.tabhostFragments.usageTrends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.swn.R;

import java.util.List;

import model.Asset;
import reports.asyncTask.StorageFetchTask;
import reports.asyncTask.UsageTrendsFetchTask;
import reports.tabhostFragments.usageBreakUp.subFragments.Filter;
import utils.BackendURI;


/**
 * Created by kumudini on 9/23/14.
 */
public class UsageTrends extends Fragment implements Filter.OnFilterFragmentInteractionListener {

    private View rootView;
    private static final String LOG_TAG = UsageTrends.class.getSimpleName();

    private Filter filter;
    private LineChart lineChart;

    public UsageTrends() {
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_usage_trends, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lineChart = new LineChart();
        getChildFragmentManager().beginTransaction()
                .add(R.id.linechart_container, lineChart).commit();

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
    public void onFilterFragmentInteraction(int storageId, String from, String to) {
        new UsageTrendsFetchTask(new UsageTrendsFetchTaskCompletionListener() {
            @Override
            public void onUsageTrendsFetchTaskCompletionListener(List<String> xAxisValues, List<Integer> yAxisValues) {
                lineChart.draw(xAxisValues, yAxisValues);
            }
        }).execute(BackendURI.getUsageTrendsByStorage(storageId, from, to));

    }

    public interface UsageTrendsFetchTaskCompletionListener{
        public void onUsageTrendsFetchTaskCompletionListener(List<String> xAxisvalues, List<Integer> yAxisValues);
    }
}
