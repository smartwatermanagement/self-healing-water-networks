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


        FragmentTabHost tabHost = (FragmentTabHost) rootView.findViewById(R.id.tabHostNotifications);
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontentnotifications);

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("All"),
                AllNotificationsFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Pending"),
                ResolvedNotificationsFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Resolved"),
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
        ArrayAdapter<Notification> adapter;
        final List notificationList = new ArrayList();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.fragment_notifications_listview, container, false);
            Notification not1 = new Notification("Water Requirement for Tomorrow", "13:00:00 hours, Sept 10, 2014", "1000 litres", false, R.drawable.water_requirement);
            Notification not2 = new Notification("Water Garden", "2/9/14", "Soil moisture: 10ppm \nWeather Prediction: Sunny", false, R.drawable.tree);
            Notification not3 = new Notification("Alert", "13:00:00 hours, Sept 10, 2014", "Main Tank: BOD < 20 mg/litre \nCurrent Reading: 70 mg/litre", false, R.drawable.alert);
            Notification not4 = new Notification("Water Requirement for Tomorrow", "13:00:00 hours, Sept 10, 2014", "2000 liters", true, R.drawable.water_requirement);
            Notification not5 = new Notification("Leak Alert", "13:00:00 hours, Sept 10, 2014", "Pipe 1, Location: 29.22945, 72.689", true,  R.drawable.leaky_tap);
            Notification not6 = new Notification("Water Garden", "13:00:00 hours, Sept 10, 2014", "1000 litres", false, R.drawable.tree);

            notificationList.add(not1);
            notificationList.add(not2);
            notificationList.add(not3);
            notificationList.add(not4);
            notificationList.add(not5);
            notificationList.add(not6);
            adapter = new NotificationsArrayAdapter<Notification>(
                    getActivity(), // The current context (this activity)
                    R.layout.list_item_notifications, // The name of the layout ID.
                    notificationList);


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
                    notificationDetails = ((Notification)adapter.getItem(i)).getDescription();
                    ((Notification)adapter.getItem(i)).setRead(true);
                    adapter.notifyDataSetChanged();
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

        ArrayAdapter<Notification> adapter;
        final List notificationList = new ArrayList();

        public AllNotificationsFragment(){

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_notifications_listview, container, false);
            // Get a reference to the ListView, and attach this adapter to it.
            ListView listView = (ListView) rootView.findViewById(R.id.listViewNotifications);


            Notification not1 = new Notification("Water Requirement for Tomorrow", "13:00:00 hours, Sept 10, 2014", "1000 litres", false, R.drawable.water_requirement);
            Notification not2 = new Notification("Water Garden", "2/9/14", "Soil moisture: 10ppm \nWeather Prediction: Sunny", false, R.drawable.tree);
            Notification not3 = new Notification("Alert", "13:00:00 hours, Sept 10, 2014", "Main Tank: BOD < 20 mg/litre \nCurrent Reading: 70 mg/litre", false, R.drawable.alert);
            Notification not4 = new Notification("Water Requirement for Tomorrow", "13:00:00 hours, Sept 10, 2014", "2000 liters", true, R.drawable.water_requirement);
            Notification not5 = new Notification("Leak Alert", "13:00:00 hours, Sept 10, 2014", "Pipe 1, Location: 29.22945, 72.689", true,  R.drawable.leaky_tap);
            Notification not6 = new Notification("Water Garden", "13:00:00 hours, Sept 10, 2014", "1000 litres", false, R.drawable.tree);

            notificationList.add(not1);
            notificationList.add(not2);
            notificationList.add(not3);
            notificationList.add(not4);
            notificationList.add(not5);
            notificationList.add(not6);
            adapter = new NotificationsArrayAdapter<Notification>(
                    getActivity(), // The current context (this activity)
                    R.layout.list_item_notifications, // The name of the layout ID.
                    notificationList);



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
                    notificationDetails = ((Notification)adapter.getItem(i)).getDescription();
                    ((Notification)adapter.getItem(i)).setRead(true);
                    adapter.notifyDataSetChanged();
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
        ArrayAdapter<Notification> adapter;
        final List notificationList = new ArrayList();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.fragment_notifications_listview, container, false);

            // Get a reference to the ListView, and attach this adapter to it.
            ListView listView = (ListView) rootView.findViewById(R.id.listViewNotifications);

            Notification not1 = new Notification("Water Requirement for Tomorrow", "13:00:00 hours, Sept 10, 2014", "1000 litres", false, R.drawable.water_requirement);
            Notification not2 = new Notification("Water Garden", "2/9/14", "Soil moisture: 10ppm \nWeather Prediction: Sunny", false, R.drawable.tree);
            Notification not3 = new Notification("Alert", "13:00:00 hours, Sept 10, 2014", "Main Tank: BOD < 20 mg/litre \nCurrent Reading: 70 mg/litre", false, R.drawable.alert);
            Notification not4 = new Notification("Water Requirement for Tomorrow", "13:00:00 hours, Sept 10, 2014", "2000 liters", true, R.drawable.water_requirement);
            Notification not5 = new Notification("Leak Alert", "13:00:00 hours, Sept 10, 2014", "Pipe 1, Location: 29.22945, 72.689", true,  R.drawable.leaky_tap);
            Notification not6 = new Notification("Water Garden", "13:00:00 hours, Sept 10, 2014", "1000 litres", false, R.drawable.tree);

            notificationList.add(not1);
            notificationList.add(not2);
            notificationList.add(not3);
            notificationList.add(not4);
            notificationList.add(not5);
            notificationList.add(not6);
            adapter = new NotificationsArrayAdapter<Notification>(
                    getActivity(), // The current context (this activity)
                    R.layout.list_item_notifications, // The name of the layout ID.
                    notificationList);

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
                    notificationDetails = ((Notification)adapter.getItem(i)).getDescription();
                    ((Notification)adapter.getItem(i)).setRead(true);
                    adapter.notifyDataSetChanged();
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
