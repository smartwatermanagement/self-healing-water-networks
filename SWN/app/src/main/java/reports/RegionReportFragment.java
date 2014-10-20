package reports;

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

public class RegionReportFragment extends Fragment {

    private static final String LOG_TAG = RegionReportFragment.class.getSimpleName();
    public static final String ASSET_PATH = "file:///android_asset/";

    public RegionReportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report_region_piechart, container, false);

        List<ArcValue> arcValues = new ArrayList<ArcValue>();
        arcValues.add(new ArcValue((float)0.25));
        arcValues.add(new ArcValue((float)0.25));
        arcValues.add(new ArcValue((float)0.25));
        arcValues.add(new ArcValue((float)0.25));
        PieChartData pieChartData = new PieChartData(arcValues);

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
}