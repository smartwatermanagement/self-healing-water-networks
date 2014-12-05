package networkHealth.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import utils.Utils;

/**
 * Created by kumudini on 11/25/14.
 */
public class AggregationFetchTask extends AsyncTask<String, Void, String>{

    private AggregationFetchTaskCompletionListener aggregationFetchTaskCompletionListener;
    private static final String LOG_TAG = AggregationFetchTask.class.getSimpleName();

    public AggregationFetchTask(AggregationFetchTaskCompletionListener aggregationFetchTaskCompletionListener){
        this.aggregationFetchTaskCompletionListener = aggregationFetchTaskCompletionListener;
    }

        @Override
        protected String doInBackground(String... uri) {
            return Utils.fetchGetResponse(uri[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.length() == 0)
                return; // No Http response

            aggregationFetchTaskCompletionListener.onAggregationFetchTaskCompletion(result);

        }

    @Override
    protected void onCancelled(String s) {
        Log.d(LOG_TAG, AggregationFetchTask.class.getSimpleName() + " got cancelled");
    }

    public interface AggregationFetchTaskCompletionListener{
        public void onAggregationFetchTaskCompletion(String json);
    }

}
