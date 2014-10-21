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
import java.util.LinkedList;
import java.util.List;

import lecho.lib.hellocharts.model.ArcValue;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.view.PieChartView;

public class RegionReportFragment extends Fragment {

    private static final String LOG_TAG = RegionReportFragment.class.getSimpleName();
    public static final String ASSET_PATH = "file:///android_asset/";

    public RegionReportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report_region_piechart, container, false);

        // Dummy data
        Aggregation iiitb = new Aggregation("IIIT-B", 100000);
        iiitb.addChild(new Aggregation("MH1", 10000));
        iiitb.addChild(new Aggregation("MH2", 20000));
        iiitb.addChild(new Aggregation("WH1", 30000));
        iiitb.addChild(new Aggregation("Cafeteria", 5000));
        iiitb.addChild(new Aggregation("Academic Block", 5000));
        iiitb.addChild(new Aggregation("Lawns", 30000));

        // Need to write a colorpicker : http://stackoverflow.com/questions/236936/how-pick-colors-for-a-pie-chart
        int color[] = {Color.BLUE, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.GREEN};
        int colorIndex = 0;
        List<ArcValue> arcValues = new ArrayList<ArcValue>();
        for(Aggregation aggregation : iiitb.children) {
            ArcValue arcValue = new ArcValue((float)aggregation.consumption / iiitb.consumption);
            arcValue.setLabel((aggregation.name + " - " + ((int)((float)aggregation.consumption / iiitb.consumption * 100)) + "%").toCharArray());
            arcValue.setColor(color[colorIndex++]);
            arcValues.add(arcValue);
        }

        PieChartData pieChartData = new PieChartData(arcValues);
        pieChartData.setHasCenterCircle(true);
        pieChartData.setHasLabels(true);
        pieChartData.setCenterText1(iiitb.name);
        pieChartData.setValueLabelsTextColor(Color.BLACK); // Not working
        pieChartData.setCenterText2( iiitb.consumption + " litres");

        PieChartView pieChartView = (PieChartView) rootView.findViewById(R.id.chart);
        pieChartView.setOnValueTouchListener(new PieChartView.PieChartOnValueTouchListener() {
            @Override
            public void onValueTouched(int selectedArc, ArcValue value) {
                Toast.makeText(getActivity().getBaseContext(), "PieChartListener is listening... Yea...", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingTouched() {

            }
        });
        pieChartView.setPieChartData(pieChartData);

        return rootView;
    }

    public class Aggregation {
        private String name;
        private List<Aggregation> children;
        private int consumption;

        public Aggregation(String name, int consumption) {
            this.name = name;
            this.consumption = consumption;
            children = new LinkedList<Aggregation>();
        }

        public void addChild(Aggregation aggregation) {
            children.add(aggregation);
        }
    }
}