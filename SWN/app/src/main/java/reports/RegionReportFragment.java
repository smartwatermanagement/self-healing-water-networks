package reports;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.swn.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.ArcValue;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.view.PieChartView;
import model.Aggregation;
import model.AggregationImpl;

public class RegionReportFragment extends Fragment {

    private static final String LOG_TAG = RegionReportFragment.class.getSimpleName();
    public static final String ASSET_PATH = "file:///android_asset/";

    private OnAggregationPieSelectedListener aggregationPieSelectedListener;
    private AggregationImpl aggregationImpl;

    public RegionReportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        aggregationImpl = (AggregationImpl) getArguments().getSerializable("aggregation");

        View rootView = inflater.inflate(R.layout.fragment_report_region_piechart, container, false);


        // Need to write a colorpicker : http://stackoverflow.com/questions/236936/how-pick-colors-for-a-pie-chart
        int color[] = {Color.BLUE, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.GREEN};
        int colorIndex = 0;
        List<ArcValue> arcValues = new ArrayList<ArcValue>();
        for(Aggregation childAggregation : aggregationImpl.getChildren()) {

            if (childAggregation instanceof AggregationImpl) {
                AggregationImpl childAggregationImpl = (AggregationImpl) childAggregation;
                ArcValue arcValue = new ArcValue((float) childAggregationImpl.getConsumption() / aggregationImpl.getConsumption());
                arcValue.setLabel((childAggregationImpl.getName() + " - " + ((int) ((float) childAggregationImpl.getConsumption() / aggregationImpl.getConsumption() * 100)) + "%").toCharArray());
                arcValue.setColor(color[colorIndex++]);
                arcValues.add(arcValue);
            }
        }

        PieChartData pieChartData = new PieChartData(arcValues);
        pieChartData.setHasCenterCircle(true);
        pieChartData.setHasLabels(true);
        pieChartData.setCenterText1(aggregationImpl.getName());
        pieChartData.setValueLabelsTextColor(Color.BLACK); // Not working
        pieChartData.setCenterText2( aggregationImpl.getConsumption() + " litres");

        PieChartView pieChartView = (PieChartView) rootView.findViewById(R.id.piechart);
        pieChartView.setOnValueTouchListener(new PieChartView.PieChartOnValueTouchListener() {
            @Override
            public void onValueTouched(int selectedArc, ArcValue value) {
                Aggregation child = aggregationImpl.getChildren().get(selectedArc);
                if (child instanceof AggregationImpl && ((AggregationImpl)child).getChildren().size() > 0)
                    aggregationPieSelectedListener.onAggregationPieSelected(child);
                else
                    Toast.makeText(getActivity().getBaseContext(), "No more detail available", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingTouched() {

            }
        });
        pieChartView.setPieChartData(pieChartData);

        return rootView;
    }

    /**
     * Listener which informs the parent fragment about a touch event in the piechart
     */
    public interface OnAggregationPieSelectedListener {
        public void onAggregationPieSelected(Aggregation aggregation);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            aggregationPieSelectedListener = (OnAggregationPieSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnPieSelectedListener");
        }
    }
}