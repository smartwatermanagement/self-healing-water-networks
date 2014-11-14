package reports.asyncTask;

import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;

import com.example.android.swn.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lecho.lib.hellocharts.model.ArcValue;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.view.PieChartView;
import model.Aggregation;
import reports.subFragments.AggregationBasedReportFragment;
import utils.Utils;

/**
 * Created by kempa on 13/11/14.
 */
public class UsageFetcher extends AsyncTask<String, Void, String> {

    private final static String LOG_TAG = UsageFetcher.class.getSimpleName();
    private View rootView;
    private AggregationBasedReportFragment.OnAggregationPieSelectedListener onAggregationPieSelectedListener;

    public UsageFetcher(View rootView, AggregationBasedReportFragment.OnAggregationPieSelectedListener onAggregationPieSelectedListener) {
        this.rootView = rootView;
        this.onAggregationPieSelectedListener = onAggregationPieSelectedListener;
    }

    @Override
    protected String doInBackground(String... uri) {
        return Utils.fetchGetResponse(uri[0]);
    }

    @Override
    public void onPostExecute(String responseString) {
        super.onPostExecute(responseString);

        if (responseString.equals(""))
            return;

        // Parse response string
        String aggregationName = null;
        final List<Aggregation> aggregations = new LinkedList<Aggregation>();
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            aggregationName = jsonObject.getString("aggregationName");
            JSONObject usageBreakUpJSON = jsonObject.getJSONObject("usageBreakUp");
            Iterator<String> keys = usageBreakUpJSON.keys();

            while (keys.hasNext()) {
                int id = Integer.parseInt(keys.next());
                JSONObject value = new JSONObject(usageBreakUpJSON.getString(String.valueOf(id)));
                aggregations.add(new Aggregation(id, value.getString("name"), value.getInt("usage")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Prepare and set the pie chart
        // Need to write a colorpicker : http://stackoverflow.com/questions/236936/how-pick-colors-for-a-pie-chart
        int color[] = {Color.BLUE, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.GREEN};
        int colorIndex = 0;
        List<ArcValue> arcValues = new ArrayList<ArcValue>();

        int totalUsage = 0;
        for (Aggregation aggregation : aggregations) {
            int usage = aggregation.getConsumption();
            totalUsage += usage;
        }
        for (Aggregation aggregation : aggregations) {
            float usageFraction = (float)aggregation.getConsumption() / totalUsage;
            ArcValue arcValue = new ArcValue(usageFraction);
            arcValue.setLabel((aggregation.getName() + " - " + ((int) (usageFraction * 100)) + "%").toCharArray());
            arcValue.setColor(color[colorIndex++]);
            arcValues.add(arcValue);
        }

        PieChartData pieChartData = new PieChartData(arcValues);
        pieChartData.setHasCenterCircle(true);
        pieChartData.setHasLabels(true);
        pieChartData.setCenterText1(aggregationName);
        pieChartData.setValueLabelsTextColor(Color.BLACK); // Not working
        pieChartData.setCenterText2(totalUsage + " litres");


        PieChartView pieChartView = (PieChartView)(rootView.findViewById(R.id.piechart));
        pieChartView.setOnValueTouchListener(new PieChartView.PieChartOnValueTouchListener() {
            @Override
            public void onValueTouched(int selectedArc, ArcValue value) {
                Aggregation childAggregation = aggregations.get(selectedArc);

                onAggregationPieSelectedListener.onAggregationPieSelected(childAggregation.getId());
              /*  if (child instanceof Aggregation && ((Aggregation)child).getChildren().size() > 0 && ((Aggregation)child).getChildren().get(0) instanceof Aggregation)
                    aggregationPieSelectedListener.onAggregationPieSelected(child);
                else
                    Toast.makeText(getActivity().getBaseContext(), "No more detail available", Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onNothingTouched() {

            }
        });
        pieChartView.setPieChartData(pieChartData);
    }
}
