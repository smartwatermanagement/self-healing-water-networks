package model;

import android.app.Activity;
import android.os.Bundle;

import com.example.android.swn.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by kempa on 23/10/14.
 */
public class DummyDataCreator {

    public static List<Notification> getDummyNotifications(Activity activity) {
        List<Notification> notifications = new LinkedList<Notification>();
        notifications.add(new Notification("Water Requirement for Tomorrow", "13:00:00 hours, Sept 10, 2014",  new WaterRequirementNotificationDetails(activity, "1000 liters", "2000 liters","1000 liters", "1000 liters" ), false, R.drawable.water_requirement, false, "15:00:00 hours, Sept 10, 2014"));
        notifications.add(new Notification("Water Garden", "13:00:00 hours, Sept 10, 2014",  new WaterGardenNotificationDetails(activity, "5.0 ppm", "33 C","50 ", "Sunny", "3.0" ), false, R.drawable.tree, false, "15:00:00 hours, Sept 10, 2014"));
        notifications.add(new Notification("Alert", "13:00:00 hours, Sept 10, 2014",  new AlertNotificationDetails(activity, "Sump", "Water Level","1000 liters", "2000 liters" ), false, R.drawable.alert,  false, "15:00:00 hours, Sept 10, 2014"));
        notifications.add(new Notification("Water Requirement for Tomorrow", "13:00:00 hours, Sept 10, 2014",  new WaterRequirementNotificationDetails(activity, "1000 liters", "1000 liters","1000 liters", "1000 liters" ), true, R.drawable.water_requirement, false, "15:00:00 hours, Sept 10, 2014"));
        notifications.add(new Notification("Leak Alert", "13:00:00 hours, Sept 10, 2014",  new LeakNotificationDetails(activity, "Pipe 123" ), true,  R.drawable.leaky_tap,  false, "15:00:00 hours, Sept 10, 2014"));
        notifications.add(new Notification("Water Garden", "13:00:00 hours, Sept 10, 2014",
                new WaterGardenNotificationDetails(activity, "5.0 ppm", "33 C","50 ", "Sunny", "3.0" ), false, R.drawable.tree
                , false, "15:00:00 hours, Sept 10, 2014"));
        return notifications;
    }

    public static Bundle getDummyDataForAggregationReports() {
        // Dummy data
        AggregationImpl iiitb = new AggregationImpl("IIIT-B", 100000, null);
        iiitb.addChild(new AggregationImpl("MH1", 10000, iiitb));
        iiitb.addChild(new AggregationImpl("MH2", 20000, iiitb));
        AggregationImpl wh = new AggregationImpl("WH", 30000, iiitb);
        wh.addChild(new AggregationImpl("1st Floor", 7000, wh));
        wh.addChild(new AggregationImpl("2nd Floor", 11000, wh));
        wh.addChild(new AggregationImpl("3rd Floor", 4000, wh));
        wh.addChild(new AggregationImpl("4th Floor", 8000, wh));
        iiitb.addChild(wh);
        iiitb.addChild(new AggregationImpl("Cafeteria", 5000, iiitb));
        iiitb.addChild(new AggregationImpl("Academic Block", 5000, iiitb));
        iiitb.addChild(new AggregationImpl("Lawns", 30000, iiitb));

        // TODO: Is this a serializable??? What about parcelable?
        Bundle bundle = new Bundle();
        bundle.putSerializable("aggregation", iiitb);
        return bundle;
    }

    public static Bundle getDummyDataForTimeReports() {
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

        Bundle bundle = new Bundle();
        bundle.putSerializable("xAxisValues", days);
        bundle.putSerializable("yAxisValues", consumption);
        return bundle;
    }

}
