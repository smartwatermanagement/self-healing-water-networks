package reports.asyncTask;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import reports.tabhostFragments.usageTrends.UsageTrends;
import utils.Utils;

/**
 * Created by kempa on 21/11/14.
 */
public class UsageTrendsFetchTask extends AsyncTask<String, Void, String> {

    private static final String LOG_TAG = UsageTrendsFetchTask.class.getSimpleName();
    private UsageTrends.UsageTrendsFetchTaskCompletionListener taskCompletionListener;

    public UsageTrendsFetchTask(UsageTrends.UsageTrendsFetchTaskCompletionListener taskCompletionListener) {
        this.taskCompletionListener = taskCompletionListener;
    }
    @Override
    protected String doInBackground(String... uri) {
        return Utils.fetchGetResponse(uri[0]);
    }

    @Override
    protected void onPostExecute(String responseString) {
        super.onPostExecute(responseString);

        if (responseString.equals(""))
            return;

        // Parse response string
        final List<Integer> days = new LinkedList<Integer>();
        final List<Integer> consumption = new LinkedList<Integer>();
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            JSONObject usageTrendsJson = jsonObject.getJSONObject("usageTrends");
            Iterator<String> keys = usageTrendsJson.keys();

            for (int day = 0;  keys.hasNext(); day++) {
                String date = keys.next();
                days.add(day);
                consumption.add(usageTrendsJson.getInt(date));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        taskCompletionListener.onUsageTrendsFetchTaskCompletionListener(days, consumption);
    }
}
