package reports.tabhostFragments.usageBreakUp.subFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.swn.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.ArcValue;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.view.PieChartView;
import model.Aggregation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PieChart.OnPieChartFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PieChart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieChart extends Fragment {

    private static final String LOG_TAG = PieChart.class.getSimpleName();

    private OnPieChartFragmentInteractionListener mListener;
    private View rootView;

    public static PieChart newInstance() {
        PieChart fragment = new PieChart();
        return fragment;
    }

    public PieChart() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            mListener = (OnPieChartFragmentInteractionListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getParentFragment().toString()
                    + " must implement OnPieChartFragmentInteractionListener");
        }

        rootView = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        return rootView;
    }

    public void populate(String aggregationName, final List<Aggregation> childAggregations) {
        // Prepare and set the pie chart
        // Need to write a colorpicker : http://stackoverflow.com/questions/236936/how-pick-colors-for-a-pie-chart
        int color[] = {Color.BLUE, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.GREEN};
        int colorIndex = 0;
        List<ArcValue> arcValues = new ArrayList<ArcValue>();

        int totalUsage = 0;
        for (Aggregation aggregation : childAggregations) {
            int usage = aggregation.getConsumption();
            totalUsage += usage;
        }
        for (Aggregation aggregation : childAggregations) {
            float usageFraction = (float)aggregation.getConsumption() / totalUsage;
            ArcValue arcValue = new ArcValue(usageFraction);
            arcValue.setLabel((aggregation.getName() + " - " + ((int) (usageFraction * 100)) + "%").toCharArray());
            arcValue.setColor(color[colorIndex++]);
            arcValues.add(arcValue);
        }

        if (childAggregations.size() <= 0)
            return;
            PieChartData pieChartData = new PieChartData(arcValues);
            pieChartData.setHasCenterCircle(true);
            pieChartData.setHasLabels(true);
            pieChartData.setCenterText1(aggregationName);
            pieChartData.setValueLabelsTextColor(Color.BLACK); // Not working
            pieChartData.setCenterText2(totalUsage + " " + "litres");

            PieChartView pieChartView = (PieChartView) rootView.findViewById(R.id.piechartview);
            pieChartView.setOnValueTouchListener(new PieChartView.PieChartOnValueTouchListener() {
                @Override
                public void onValueTouched(int selectedArc, ArcValue value) {
                    Aggregation childAggregation = childAggregations.get(selectedArc);
                    mListener.onPieChartFragmentInteraction(childAggregation.getId());
                }

                @Override
                public void onNothingTouched() {

                }
            });
            pieChartView.setPieChartData(pieChartData);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPieChartFragmentInteractionListener {
        public void onPieChartFragmentInteraction(int aggregationId);
    }

}