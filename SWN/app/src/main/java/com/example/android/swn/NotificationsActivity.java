package com.example.android.swn;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.tabHostNotifications);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontentnotifications);

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("All"),
                AllNotificationsFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Resolved"),
                ResolvedNotificationsFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Pending"),
                PendingNotificationsFragment.class, null);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {

            }
        });
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
        ArrayAdapter<String> adapter;
        final List notificationList = new ArrayList();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            notificationList.add("Notification 1");
            notificationList.add("Notification 2");
            notificationList.add("Notification 3");
            notificationList.add("Notification 4");
            notificationList.add("Notification 5");
            notificationList.add("Notification 6");
            adapter = new ArrayAdapter<String>(
                    getActivity(), // The current context (this activity)
                    R.layout.list_item_notifications, // The name of the layout ID.
                    R.id.list_item_notification_textview, // The ID of the textview to populate.
                    notificationList);
            View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

            // Get a reference to the ListView, and attach this adapter to it.
            ListView listView = (ListView) rootView.findViewById(R.id.listViewNotifications);
            listView.setAdapter(adapter);

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {
                    deleteNotification(position);
                    return true;
                }
            });


            return rootView;
        }

        private void deleteNotification(int position){
            final int pos = position;
            boolean returnValue;
            AlertDialog.Builder alert = new AlertDialog.Builder(
                    getActivity());

            alert.setTitle("Delete");
            alert.setMessage("Do you want delete this item?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    notificationList.remove(pos);
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
    public static class AllNotificationsFragment extends Fragment{

        ArrayAdapter<String> adapter;
        final List notificationList = new ArrayList();

        public AllNotificationsFragment(){

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            notificationList.add("Notification 1");
            notificationList.add("Notification 2");
            notificationList.add("Notification 3");
            notificationList.add("Notification 4");
            notificationList.add("Notification 5");
            notificationList.add("Notification 6");
            adapter = new ArrayAdapter<String>(
                            getActivity(), // The current context (this activity)
                            R.layout.list_item_notifications, // The name of the layout ID.
                            R.id.list_item_notification_textview, // The ID of the textview to populate.
                            notificationList);
            View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

            // Get a reference to the ListView, and attach this adapter to it.
            ListView listView = (ListView) rootView.findViewById(R.id.listViewNotifications);
            listView.setAdapter(adapter);

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {
                    deleteNotification(position);
                    return true;
                }
            });


            return rootView;
        }

        private void deleteNotification(int position){
            final int pos = position;
            boolean returnValue;
            AlertDialog.Builder alert = new AlertDialog.Builder(
                    getActivity());

            alert.setTitle("Delete");
            alert.setMessage("Do you want delete this item?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    notificationList.remove(pos);
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
    public static class PendingNotificationsFragment extends Fragment{

        public PendingNotificationsFragment(){

        }
        ArrayAdapter<String> adapter;
        final List notificationList = new ArrayList();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            notificationList.add("Notification 1");
            notificationList.add("Notification 2");
            notificationList.add("Notification 3");
            notificationList.add("Notification 4");
            notificationList.add("Notification 5");
            notificationList.add("Notification 6");
            adapter = new ArrayAdapter<String>(
                    getActivity(), // The current context (this activity)
                    R.layout.list_item_notifications, // The name of the layout ID.
                    R.id.list_item_notification_textview, // The ID of the textview to populate.
                    notificationList);
            View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

            // Get a reference to the ListView, and attach this adapter to it.
            ListView listView = (ListView) rootView.findViewById(R.id.listViewNotifications);
            listView.setAdapter(adapter);

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int position, long arg3) {
                    deleteNotification(position);
                    return true;
                }
            });


            return rootView;
        }

        private void deleteNotification(int position){
            final int pos = position;
            boolean returnValue;
            AlertDialog.Builder alert = new AlertDialog.Builder(
                    getActivity());

            alert.setTitle("Delete");
            alert.setMessage("Do you want delete this item?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    notificationList.remove(pos);
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
}
