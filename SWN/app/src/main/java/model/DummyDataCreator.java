package model;

import android.app.Activity;
import android.os.Bundle;

import com.example.android.swn.R;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by kempa on 23/10/14.
 */
public class DummyDataCreator {

    public List<Notification> getDummyNotifications(Activity activity) {
        List<Notification> notifications = new LinkedList<Notification>();
        notifications.add(new Notification("Water Requirement for Tomorrow", "Sep 11, 2014 at 5:00 PM",  new WaterRequirementNotificationDetails(activity, "100495 litres", "102700 litres","50270 litres", "50000 litres" ), false, R.drawable.water_requirement, IssueState.OPEN, "Sep 10, 2014 at 3:00 PM"));
        notifications.add(new Notification("Water Garden", "Sep 11, 2014 at 1:00 AM",  new WaterGardenNotificationDetails(activity, "5.0 ppm", "3.0 ","50 ", "Sunny", "33 C" ), false, R.drawable.tree, IssueState.OPEN, "Sep 10, 2014 at 3:00 PM"));

        Map<String, String> mainTankProperties = new HashMap<String, String>();
        mainTankProperties.put("storageLevel", "200000");
        mainTankProperties.put("capacity", "300000");
        mainTankProperties.put("PH", "5.2");
        mainTankProperties.put("BOD", "3.24");
        notifications.add(new Notification("Threshold Breach", "Sep 11, 2014 at 1:00 AM",  new ThresholdBreachNotificationDetails(activity, new AssetAggregationImpl(34, 67.78, 46.87,  mainTankProperties, 1, null, "Storage"), "Water Level","1000 litres", "2000 litres" ), true, R.drawable.alert,  IssueState.IN_PROGRESS, "Sep 11, 2014 at 1:00 Am"));

        notifications.add(new Notification("Water Requirement for Tomorrow", "Sep 10, 2014 at 5:00 PM",  new WaterRequirementNotificationDetails(activity, "1000 litres", "1000 litres","1000 litres", "1000 litres" ), true, R.drawable.water_requirement, IssueState.RESOLVED, "Sep 10, 2014 at 3:00 PM"));
        notifications.add(new Notification("Leak Alert", "Sep 10, 2014 at 1:00 PM",  new LeakNotificationDetails(activity, "Pipe 123" ), true,  R.drawable.leaky_tap,  IssueState.RESOLVED, "Sep 10, 2014 at 3:00 PM"));
        notifications.add(new Notification("Water Garden", "Sep 10, 2014 at 11:00 AM",
                new WaterGardenNotificationDetails(activity, "5.0 ppm", "33 C","50 ", "Sunny", "3.0" ), true, R.drawable.tree
                , IssueState.RESOLVED, "Sep 10, 2014 at 3:00 PM"));
        return notifications;
    }

    public Bundle getDummyDataForAggregationReports() {
        // Dummy data
        AggregationImpl iiitb = getDummyAggregationTree();

        // TODO: Is this a serializable??? What about parcelable?
        Bundle bundle = new Bundle();
        bundle.putSerializable("aggregation", iiitb);
        return bundle;
    }

    public Bundle getDummyDataForTimeReports() {
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

    public AggregationImpl getDummyAggregationTree() {
        AggregationImpl iiitb = new AggregationImpl("IIIT-B", 100000, null, 2);
        iiitb.addChild(new AggregationImpl("MH1", 10000, iiitb, 0));
        iiitb.addChild(new AggregationImpl("MH2", 20000, iiitb, 0));
        AggregationImpl wh = new AggregationImpl("WH", 30000, iiitb, 1);
        wh.addChild(new AggregationImpl("1st Floor", 7000, wh, 0));
        wh.addChild(new AggregationImpl("2nd Floor", 11000, wh, 0));
        wh.addChild(new AggregationImpl("3rd Floor", 4000, wh, 0));

        AggregationImpl wh_4th_floor = new AggregationImpl("4th Floor", 8000, wh, 1);
        Map<String, String> tapProperties = new HashMap<String, String>();
        tapProperties.put("leak", "true");
        AssetAggregationImpl tap = new AssetAggregationImpl(57, 45.78, 98.73, tapProperties, 1, wh_4th_floor, "Tap");
        wh_4th_floor.addChild(tap);
        wh.addChild(wh_4th_floor);
        iiitb.addChild(wh);
        iiitb.addChild(new AggregationImpl("Cafeteria", 5000, iiitb,0));
        iiitb.addChild(new AggregationImpl("Academic Block", 5000, iiitb, 0));

        AggregationImpl lawns = new AggregationImpl("Lawns", 30000, iiitb, 1);

        Map<String, String> mainTankProperties = new HashMap<String, String>();
        mainTankProperties.put("Storage", "200000 litres");
        mainTankProperties.put("Capacity", "300000 litres");
        mainTankProperties.put("PH", "5.2");
        mainTankProperties.put("BOD", "3.24 ppm");
        AssetAggregationImpl mainTank = new AssetAggregationImpl(1, 20, 30, mainTankProperties, 1, lawns, "Storage");
        lawns.addChild(mainTank);

        Map<String, String> recyclingPlantProperties = new HashMap<String, String>();
        recyclingPlantProperties.put("Storage", "100000 litres");
        recyclingPlantProperties.put("Capacity", "150000 litres");
        recyclingPlantProperties.put("Inflow", "5 litres/sec");
        recyclingPlantProperties.put("PH", "3");
        recyclingPlantProperties.put("BOD", "0.76 ppm");
        AssetAggregationImpl recyclingPlant = new AssetAggregationImpl(1, 20, 30, recyclingPlantProperties, 0, lawns, "Recycling Plant");
        lawns.addChild(recyclingPlant);

        Map<String, String> pumpProperties = new HashMap<String, String>();
        pumpProperties.put("Outflow", "15 litres/sec");
        pumpProperties.put("ON", "true");
        AssetAggregationImpl pump = new AssetAggregationImpl(1, 20, 30, pumpProperties, 0, lawns, "Pump");
        lawns.addChild(pump);
        iiitb.addChild(lawns);

        return iiitb;


    }

}
