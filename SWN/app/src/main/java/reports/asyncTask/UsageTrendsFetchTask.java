package reports.asyncTask;

import android.os.AsyncTask;

import java.util.LinkedList;

import reports.tabhostFragments.usageTrends.UsageTrends;

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
        // return Utils.fetchGetResponse(uri[0]); // TODO
        return "";
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        // TODO: Parse the response and get the values

        LinkedList<Integer> days = new LinkedList<Integer>();
        days.add(1);
        days.add(2);
        days.add(3);
        days.add(4);
        days.add(5);

        LinkedList<Float> consumption = new LinkedList<Float>();
        consumption.add((float)1);
        consumption.add((float)2.2);
        consumption.add((float)2.9);
        consumption.add((float)3.5);
        consumption.add((float)4.2);

        taskCompletionListener.onUsageTrendsFetchTaskCompletionListener(days, consumption);
    }
}
