package reports.subFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.swn.R;

import reports.asyncTask.UsageFetcher;
import utils.BackendURI;

public class AggregationBasedReportFragment extends Fragment {

    private static final String LOG_TAG = AggregationBasedReportFragment.class.getSimpleName();
   // public static final String ASSET_PATH = "file:///android_asset/";

    private OnAggregationPieSelectedListener aggregationPieSelectedListener;

    public AggregationBasedReportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the view
        View rootView = inflater.inflate(R.layout.fragment_aggregation_based_chart, container, false);

        // check if the parent fragment listens to a click event associated with the pie chart
        try {
            aggregationPieSelectedListener = (OnAggregationPieSelectedListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getParentFragment().toString() + " must implement " + OnAggregationPieSelectedListener.class.getSimpleName());
        }

        if (getArguments() != null)
        if (getArguments().getSerializable("aggregationId") != null) {
            int aggregationId = (Integer) (getArguments().getSerializable("aggregationId"));
            new UsageFetcher(rootView, aggregationPieSelectedListener).execute(BackendURI.getGetUsageByStorageAndAggregationURI(aggregationId));
        }
        return rootView;
    }

    /**
     * Listener which informs the parent fragment about a touch event in the piechart
     */
    public interface OnAggregationPieSelectedListener {
        public void onAggregationPieSelected(int aggregationId);
    }
}