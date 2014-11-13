package reports.asyncTask;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.ArcValue;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.view.PieChartView;
import utils.Utils;

/**
 * Created by kempa on 13/11/14.
 */
public class UsageFetcher extends AsyncTask<String, Void, String> {

    private final static String LOG_TAG = UsageFetcher.class.getSimpleName();
    private PieChartView pieChartView;

    public UsageFetcher(PieChartView pieChartView) {
        this.pieChartView = pieChartView;
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
        Map<String, String> usageBreakUp = new HashMap<String, String>();
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            aggregationName = jsonObject.getString("aggregationName");
            JSONObject usageBreakUpJSON = jsonObject.getJSONObject("usageBreakUp");
            Iterator<String> keys = usageBreakUpJSON.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                Log.d(LOG_TAG, key);
                String value = usageBreakUpJSON.getString(key);
                usageBreakUp.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Prepare and set the pie chart
        // Need to write a colorpicker : http://stackoverflow.com/questions/236936/how-pick-colors-for-a-pie-chart
        int color[] = {Color.BLUE, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.GREEN};
        int colorIndex = 0;
        List<ArcValue> arcValues = new ArrayList<ArcValue>();

        float totalUsage = 0;
        for (String name : usageBreakUp.keySet()) {
            float usage = Float.parseFloat(usageBreakUp.get(name));
            totalUsage += usage;
        }
        for (String name : usageBreakUp.keySet()) {
            float usage = Float.parseFloat(usageBreakUp.get(name));
            float usageFraction = usage / totalUsage;
            ArcValue arcValue = new ArcValue(usageFraction);
            arcValue.setLabel((name + " - " + ((int) (usageFraction * 100)) + "%").toCharArray());
            arcValue.setColor(color[colorIndex++]);
            arcValues.add(arcValue);
        }

        PieChartData pieChartData = new PieChartData(arcValues);
        pieChartData.setHasCenterCircle(true);
        pieChartData.setHasLabels(true);
        pieChartData.setCenterText1(aggregationName);
        pieChartData.setValueLabelsTextColor(Color.BLACK); // Not working
        pieChartData.setCenterText2(totalUsage + " litres");

        /*
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
        });*/
        pieChartView.setPieChartData(pieChartData);
    }
}
