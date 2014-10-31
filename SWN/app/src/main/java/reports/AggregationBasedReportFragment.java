package reports;

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
import model.IAggregation;

public class AggregationBasedReportFragment extends Fragment {

    private static final String LOG_TAG = AggregationBasedReportFragment.class.getSimpleName();
   // public static final String ASSET_PATH = "file:///android_asset/";

    private OnAggregationPieSelectedListener aggregationPieSelectedListener;
    private Aggregation aggregation;

    public AggregationBasedReportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        aggregation = (Aggregation) getArguments().getSerializable("aggregation");

        // check if the parent fragment listens to a click event associated with the pie chart
        try {
            aggregationPieSelectedListener = (OnAggregationPieSelectedListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getParentFragment().toString() + " must implement " + OnAggregationPieSelectedListener.class.getSimpleName());
        }

        // Inflate the view
        View rootView = inflater.inflate(R.layout.fragment_aggregation_based_chart, container, false);


        // Prepare and set the pie chart
        // Need to write a colorpicker : http://stackoverflow.com/questions/236936/how-pick-colors-for-a-pie-chart
        int color[] = {Color.BLUE, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.GREEN};
        int colorIndex = 0;
        List<ArcValue> arcValues = new ArrayList<ArcValue>();
        for(IAggregation childIAggregation : aggregation.getChildren()) {

            if (childIAggregation instanceof Aggregation) {
                Aggregation childAggregation = (Aggregation) childIAggregation;
                ArcValue arcValue = new ArcValue((float) childAggregation.getConsumption() / aggregation.getConsumption());
                arcValue.setLabel((childAggregation.getName() + " - " + ((int) ((float) childAggregation.getConsumption() / aggregation.getConsumption() * 100)) + "%").toCharArray());
                arcValue.setColor(color[colorIndex++]);
                arcValues.add(arcValue);
            }
        }

        PieChartData pieChartData = new PieChartData(arcValues);
        pieChartData.setHasCenterCircle(true);
        pieChartData.setHasLabels(true);
        pieChartData.setCenterText1(aggregation.getName());
        pieChartData.setValueLabelsTextColor(Color.BLACK); // Not working
        pieChartData.setCenterText2( aggregation.getConsumption() + " litres");

        PieChartView pieChartView = (PieChartView) rootView.findViewById(R.id.piechart);
        pieChartView.setOnValueTouchListener(new PieChartView.PieChartOnValueTouchListener() {
            @Override
            public void onValueTouched(int selectedArc, ArcValue value) {
                IAggregation child = aggregation.getChildren().get(selectedArc);
                if (child instanceof Aggregation && ((Aggregation)child).getChildren().size() > 0 && ((Aggregation)child).getChildren().get(0) instanceof Aggregation)
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
        public void onAggregationPieSelected(IAggregation IAggregation);
    }
}