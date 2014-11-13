package reports.asyncTask;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.ArcValue;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.view.PieChartView;

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
        HttpClient httpClient = new DefaultHttpClient();

        String responseString = "";
        HttpResponse httpResponse = null;
        try {
            Log.d(LOG_TAG, uri[0]);
            httpResponse = httpClient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                httpResponse.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                Log.d(LOG_TAG, responseString);
            } else {
                httpResponse.getEntity().getContent().close();
                throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (NoHttpResponseException e) {
            responseString = "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
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
