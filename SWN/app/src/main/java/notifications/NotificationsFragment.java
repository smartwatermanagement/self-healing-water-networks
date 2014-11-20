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
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.android.swn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.IssueState;
import model.Notification;
import model.NotificationDetails;
import utils.BackendURI;
import utils.JsonParser;
import utils.NotificationsArrayAdapter;
import utils.Utils;



public class NotificationsFragment extends Fragment {

    static NotificationDetails notificationDetails;
    static Map<String, Integer> imageTitleMap = new HashMap<String, Integer>();
    private static final String LOG_TAG = NotificationsFragment.class.getSimpleName();
    private static List<Notification> notificationCache = new ArrayList<Notification>();


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
        final String LOG_TAG = this.getClass().getSimpleName();


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.fragment_notifications_listview, container, false);

            // Get a reference to the ListView, and attach this adapter to it.
            final ListView listView = (ListView) rootView.findViewById(R.id.listViewNotifications);


            // Fetch notifications from server only if cache is empty.
            if(notificationCache.isEmpty()) {
                // AsyncTask to get notifications from server
                new AsyncTask<String, Void, String>() {

                    @Override
                    protected String doInBackground(String... uri) {
                        return Utils.fetchGetResponse(uri[0]);
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);

                        if (result.length() == 0)
                            return; // No Http response

                        List<Notification> notifications = new ArrayList<Notification>();
                        Log.d(LOG_TAG, "json is " + result);
                        try {
                            notificationCache = JsonParser.parseNotifications(new JSONArray(result));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (Notification notification : notificationCache) {
                            if (getRequiredIssueState().contains(notification.getStatus()))
                                notifications.add(notification);
                        }
                        adapter = new NotificationsArrayAdapter<Notification>(
                                getActivity(), // The current context (this activity)
                                R.layout.list_item_notifications, // The name of the layout ID.
                                notifications);
                        listView.setAdapter(adapter);

                    }
                }.execute(BackendURI.getNotificationURI());
            }
            else{
                List<Notification> notifications = new ArrayList<Notification>();
                for (Notification notification : notificationCache) {
                    if (getRequiredIssueState().contains(notification.getStatus()))
                        notifications.add(notification);
                }
                adapter = new NotificationsArrayAdapter<Notification>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_notifications, // The name of the layout ID.
                        notifications);
                listView.setAdapter(adapter);
            }


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
                    final int position = i;

                    // AsyncTask to update read status of notification
                    new AsyncTask<String, Void, Void>() {

                        @Override
                        protected Void doInBackground(String... uri) {
                            String xmlData;
                            XmlSerializer serializer = Xml.newSerializer();
                            StringWriter writer = new StringWriter();
                            try {
                                serializer.setOutput(writer);
                                serializer.startDocument("UTF-8",true);
                                serializer.startTag("", "model.Notification");
                                serializer.startTag("", "id");
                                serializer.text(adapter.getItem(position).getId() + "");
                                serializer.endTag("", "id");
                                serializer.startTag("", "read");
                                serializer.text("true");
                                serializer.endTag("", "read");
                                serializer.endTag("", "model.Notification");
                                serializer.endDocument();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            xmlData = writer.toString();
                            Log.d("Notification Fragment" , xmlData);
                            Utils.sendUpdateHttpRequest(uri[0], xmlData);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void nothing) {
                            super.onPostExecute(nothing);
                            refresh();
                        }
                    }.execute(BackendURI.getNotificationUpdateURI(adapter.getItem(i).getId()));

                    (new DetailsDialogFragment()).show(getChildFragmentManager(), "Tag");
                }
            });


            return rootView;
        }

        // Fetches notifications from server.
        public void refresh(){
            final ListView listView = (ListView)getView().findViewById(R.id.listViewNotifications);

            // AsyncTask to get notifications from server
            new AsyncTask<String, Void, String>() {

                @Override
                protected String doInBackground(String... uri) {
                    return Utils.fetchGetResponse(uri[0]);
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);

                    if (result.length() == 0)
                        return; // No Http response

                    List<Notification> notifications = new ArrayList<Notification>();
                    Log.d(LOG_TAG, "json is " + result);
                    try {
                        notificationCache = JsonParser.parseNotifications(new JSONArray(result));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (Notification notification : notificationCache) {
                        if (getRequiredIssueState().contains(notification.getStatus()))
                            notifications.add(notification);
                    }
                    adapter = new NotificationsArrayAdapter<Notification>(
                            getActivity(), // The current context (this activity)
                            R.layout.list_item_notifications, // The name of the layout ID.
                            notifications);
                    listView.setAdapter(adapter);

                }
            }.execute(BackendURI.getNotificationURI());

        }

        // To be overridden by sub classes
        protected List<IssueState> getRequiredIssueState(){ return null;}

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

                    // AsyncTask to delete notification
                    new AsyncTask<String, Void, Void>() {

                        @Override
                        protected Void doInBackground(String... uri) {
                            Utils.sendDeleteHttpRequest(uri[0]);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void nothing) {
                            super.onPostExecute(nothing);
                            adapter.remove(adapter.getItem(position));
                            adapter.notifyDataSetChanged();
                            adapter.notifyDataSetInvalidated();
                            refresh();
                        }
                    }.execute(BackendURI.getNotificationDeleteURI(adapter.getItem(position).getId()));


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
        protected List<IssueState> getRequiredIssueState(){
            List<IssueState> issueStates = new ArrayList<IssueState>();
            issueStates.add(IssueState.RESOLVED);
            return issueStates;
        }

    }
    public static class AllNotificationsFragment extends BaseNotificationsFragment{

        ArrayAdapter<Notification> adapter;
        final List notificationList = new ArrayList();

        public AllNotificationsFragment(){

        }
        @Override
        protected List<IssueState> getRequiredIssueState(){
            List<IssueState> issueStates = new ArrayList<IssueState>();
            issueStates.add(IssueState.RESOLVED);
            issueStates.add(IssueState.OPEN);
            issueStates.add(IssueState.IN_PROGRESS);
            return issueStates;
        }
    }

    public static class PendingNotificationsFragment extends BaseNotificationsFragment{

        public PendingNotificationsFragment(){

        }

        @Override
        protected List<IssueState> getRequiredIssueState(){
            List<IssueState> issueStates = new ArrayList<IssueState>();
            issueStates.add(IssueState.OPEN);
            issueStates.add(IssueState.IN_PROGRESS);
            return issueStates;
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
            if(notificationDetails != null)
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
