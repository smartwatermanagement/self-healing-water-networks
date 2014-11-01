package notifications;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.android.swn.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.IssueState;
import model.Notification;
import model.NotificationDetails;
import utils.NotificationsArrayAdapter;


public class NotificationsFragment extends Fragment {

    static NotificationDetails notificationDetails;
    static Map<String, Integer> imageTitleMap = new HashMap<String, Integer>();
    private static final String LOG_TAG = NotificationsFragment.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_notifications_tabs, container, false);


        FragmentTabHost tabHost = (FragmentTabHost) rootView.findViewById(R.id.tabHostNotifications);
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontentnotifications);

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("All"),
                AllNotificationsFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Pending"),
                PendingNotificationsFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Resolved"),
                ResolvedNotificationsFragment.class, null);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {

            }
        });

        return tabHost;
    }

    public static class BaseNotificationsFragment extends Fragment{
        ArrayAdapter<Notification> adapter;
        final String URI = "http://192.16.13.72:8080/SWNBackend/notification.json";
        final String LOGTAG = this.getClass().getSimpleName();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.fragment_notifications_listview, container, false);

            // Get a reference to the ListView, and attach this adapter to it.
            final ListView listView = (ListView) rootView.findViewById(R.id.listViewNotifications);



            // AsyncTask to get notifications from server
            new AsyncTask<String, Void, String>() {

                @Override
                protected String doInBackground(String... uri) {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpResponse response;
                    String responseString = null;

                    try {
                        response = httpclient.execute(new HttpGet(uri[0]));
                        StatusLine statusLine = response.getStatusLine();
                        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            response.getEntity().writeTo(out);
                            out.close();
                            responseString = out.toString();
                        } else {
                            //Closes the connection.
                            response.getEntity().getContent().close();
                            throw new IOException(statusLine.getReasonPhrase());
                        }
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return responseString;
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    List<Notification> notifications = new ArrayList<Notification>();
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    Log.d(LOG_TAG, "json is " + result);
                    notifications = Arrays.asList(gson.fromJson(result, Notification[].class));
                    adapter = new NotificationsArrayAdapter<Notification>(
                            getActivity(), // The current context (this activity)
                            R.layout.list_item_notifications, // The name of the layout ID.
                            notifications);
                    listView.setAdapter(adapter);

                }
            }.execute(URI);


            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {
                    deleteNotification(position);
                    return true;
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    notificationDetails = ((Notification)adapter.getItem(i)).getDetails();
                    ((Notification)adapter.getItem(i)).setRead(true);
                    adapter.notifyDataSetChanged();
                    (new DetailsDialogFragment()).show(getChildFragmentManager(), "Tag");
                }
            });


            return rootView;
        }

        // To be overridden by sub classes
        protected IssueState[] getRequiredIssueState(){ return null;}

        private void deleteNotification(final int position){
            final int pos = position;
            boolean returnValue;
            AlertDialog.Builder alert = new AlertDialog.Builder(
                    getActivity());

            alert.setTitle("Delete");
            alert.setMessage("Do you want delete this item?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.remove(adapter.getItem(position));
                    adapter.notifyDataSetChanged();
                    adapter.notifyDataSetInvalidated();
                }
            });
            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            alert.show();

        }

    }

    public static class ResolvedNotificationsFragment extends BaseNotificationsFragment{
        public ResolvedNotificationsFragment(){

        }

        @Override
        protected IssueState[] getRequiredIssueState(){
            return new IssueState[]{IssueState.RESOLVED};
        }

    }
    public static class AllNotificationsFragment extends BaseNotificationsFragment{

        ArrayAdapter<Notification> adapter;
        final List notificationList = new ArrayList();

        public AllNotificationsFragment(){

        }
        @Override
        protected IssueState[] getRequiredIssueState(){
            return new IssueState[]{IssueState.RESOLVED, IssueState.IN_PROGRESS, IssueState.OPEN};
        }
    }

    public static class PendingNotificationsFragment extends BaseNotificationsFragment{

        public PendingNotificationsFragment(){

        }

        @Override
        protected IssueState[] getRequiredIssueState(){
            return new IssueState[]{IssueState.OPEN, IssueState.IN_PROGRESS};
        }

    }

    public static class DetailsDialogFragment extends DialogFragment {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            ViewGroup layout = (ViewGroup)getActivity().getLayoutInflater().inflate(R.layout.notification_details,
                    null);
            TextView workerName = (TextView)layout.findViewById(R.id.notification_details_assignee_name_textview);
            workerName.setText("Kumudini Kakwani has been assigned this issue");
            TextView workerPhone = (TextView)layout.findViewById(R.id.notification_details_assignee_phone_textview);
            workerPhone.setText("8904642247");
            layout.addView(notificationDetails.getView(layout));

            builder.setView(layout);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            builder.setTitle("Details");
        return builder.create();
        }
    }
}
