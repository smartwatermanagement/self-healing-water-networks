package com.example.android.swn;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class NotificationsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notifications, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class ResolvedNotificationsFragment extends Fragment{
        public ResolvedNotificationsFragment(){

        }


    }
    public static class AllNotificationsFragment extends Fragment{

        ArrayAdapter<String> adapter;
        public AllNotificationsFragment(){

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            ArrayList notificationList = new ArrayList();
            notificationList.add("Notification 1");
            notificationList.add("Notification 2");
            notificationList.add("Notification 3");
            notificationList.add("Notification 4");
            notificationList.add("Notification 5");
            notificationList.add("Notification 6");
            adapter =
                    new ArrayAdapter<String>(
                            getActivity(), // The current context (this activity)
                            R.layout.list_item_notifications, // The name of the layout ID.
                            R.id.list_item_notification_textview, // The ID of the textview to populate.
                            notificationList);
            View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

            // Get a reference to the ListView, and attach this adapter to it.
            ListView listView = (ListView) rootView.findViewById(R.id.listview_source);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent detailsIntent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, adapter.getItem(i));
                    startActivity(detailsIntent);
                }
            });
            return rootView;
        }
    }
    public static class PendingNotificationsFragment extends Fragment{
        public PendingNotificationsFragment(){

        }
    }
}
