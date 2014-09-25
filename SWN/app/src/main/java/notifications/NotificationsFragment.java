package notifications;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.android.swn.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.NotificationsArrayAdapter;

public class NotificationsFragment extends Fragment {

    static String notificationDetails;
    static Map<String, Integer> imageTitleMap = new HashMap<String, Integer>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_notifications_tabs, container, false);

        imageTitleMap.put("Water Requirement for Tomorrow", R.drawable.water_requirement);
        imageTitleMap.put("Water Garden", R.drawable.tree);
        imageTitleMap.put("Leak Alert", R.drawable.leaky_tap);
        imageTitleMap.put("Alert", R.drawable.alert);

        FragmentTabHost tabHost = (FragmentTabHost) rootView.findViewById(R.id.tabHostNotifications);
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontentnotifications);

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("All"),
                AllNotificationsFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Read"),
                ResolvedNotificationsFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Unread"),
                PendingNotificationsFragment.class, null);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {

            }
        });

        return tabHost;
    }

    public static class ResolvedNotificationsFragment extends Fragment{
        public ResolvedNotificationsFragment(){

        }
        ArrayAdapter<String> adapter;
        final List notificationList = new ArrayList();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.fragment_notifications_listview, container, false);
            notificationList.add("Water Requirement for Tomorrow- 1000L");
            notificationList.add("Water Garden-Soil moisture: 10, Weather Prediction:Sunny");
            notificationList.add("Alert-Sump 1: Quality:100");
            notificationList.add("Water Requirement for Tomorrow- 2000L");
            notificationList.add("Leak Alert-Pipe1");
            notificationList.add("Water Garden");
            adapter = new NotificationsArrayAdapter<String>(
                    getActivity(), // The current context (this activity)
                    R.layout.list_item_notifications, // The name of the layout ID.
                    notificationList, imageTitleMap);


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
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    notificationDetails = adapter.getItem(i);
                    (new DetailsDialogFragment()).show(getChildFragmentManager(), "Tag");
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

            View rootView = inflater.inflate(R.layout.fragment_notifications_listview, container, false);
            // Get a reference to the ListView, and attach this adapter to it.
            ListView listView = (ListView) rootView.findViewById(R.id.listViewNotifications);

            notificationList.add("Water Requirement for Tomorrow- 1000L");
            notificationList.add("Water Garden-Soil moisture: 10, Weather Prediction:Sunny");
            notificationList.add("Alert-Sump 1: Quality:100");
            notificationList.add("Water Requirement for Tomorrow- 2000L");
            notificationList.add("Leak Alert-Pipe1");
            notificationList.add("Water Garden");
            adapter = new NotificationsArrayAdapter<String>(
                    getActivity(), // The current context (this activity)
                    R.layout.list_item_notifications,
                    notificationList, imageTitleMap);



            listView.setAdapter(adapter);

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
                    notificationDetails = adapter.getItem(i);
                    (new DetailsDialogFragment()).show(getChildFragmentManager(), "Tag");
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


            View rootView = inflater.inflate(R.layout.fragment_notifications_listview, container, false);

            // Get a reference to the ListView, and attach this adapter to it.
            ListView listView = (ListView) rootView.findViewById(R.id.listViewNotifications);

            notificationList.add("Water Requirement for Tomorrow- 1000L");
            notificationList.add("Water Garden-Soil moisture: 10, Weather Prediction:Sunny");
            notificationList.add("Alert-Sump 1: Quality:100");
            notificationList.add("Water Requirement for Tomorrow- 2000L");
            notificationList.add("Leak Alert-Pipe1");
            notificationList.add("Water Garden-Soil moisture: 20, Weather Prediction:Sunny");
            adapter = new NotificationsArrayAdapter<String>(
                    getActivity(), // The current context (this activity)
                    R.layout.list_item_notifications, // The name of the layout ID.
                    notificationList, imageTitleMap);

            listView.setAdapter(adapter);

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
                    notificationDetails = adapter.getItem(i);
                    (new DetailsDialogFragment()).show(getChildFragmentManager(), "Tag");
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

    public static class DetailsDialogFragment extends DialogFragment {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(notificationDetails)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
        return builder.create();
        }
    }
}
