package networkHealth.asynctasks;

import android.os.AsyncTask;

import utils.Utils;

/**
 * Created by kumudini on 11/25/14.
 */
public class AggregationFetchTask extends AsyncTask<String, Void, String>{

    AggregationFetchTaskCompletionListener aggregationFetchTaskCompletionListener;

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

    public interface AggregationFetchTaskCompletionListener{
        public void onAggregationFetchTaskCompletion(String json);
    }

}
