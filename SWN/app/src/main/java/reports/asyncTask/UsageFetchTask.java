package reports.asyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import model.Aggregation;
import utils.Utils;

/**
 * Created by kempa on 13/11/14.
 */
public class UsageFetchTask extends AsyncTask<String, Void, String> {

    private final static String LOG_TAG = UsageFetchTask.class.getSimpleName();
    private UsageFetchTaskCompletionListener usageFetchTaskCompletionListener;
    private ProgressDialog progressDialog;

    public UsageFetchTask(UsageFetchTaskCompletionListener usageFetchTaskCompletionListener) {
        this.usageFetchTaskCompletionListener = usageFetchTaskCompletionListener;
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

        usageFetchTaskCompletionListener
                .onUsageFetchTaskCompletionListener(aggregationName, aggregations);
    }

    public interface UsageFetchTaskCompletionListener {
        public void onUsageFetchTaskCompletionListener(String aggregationName,
                                                       List<Aggregation> childAggregations);
    }
}