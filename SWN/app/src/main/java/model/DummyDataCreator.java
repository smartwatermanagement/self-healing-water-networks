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

    public static List<Notification> getDummyNotifications(Activity activity) {
        List<Notification> notifications = new LinkedList<Notification>();
        notifications.add(new Notification("Water Requirement for Tomorrow", "13:00 hours, Sept 10, 2014",  new WaterRequirementNotificationDetails(activity, "100495 litres", "102700 litres","50270 litres", "50000 litres" ), false, R.drawable.water_requirement, false, "15:00 hours, Sept 10, 2014"));
        notifications.add(new Notification("Water Garden", "13:00 hours, Sept 10, 2014",  new WaterGardenNotificationDetails(activity, "5.0 ppm", "3.0 ","50 ", "Sunny", "33 C" ), false, R.drawable.tree, false, "15:00 hours, Sept 10, 2014"));

        Map<String, String> mainTankProperties = new HashMap<String, String>();
        mainTankProperties.put("storageLevel", "200000");
        mainTankProperties.put("capacity", "300000");
        mainTankProperties.put("PH", "5.2");
        mainTankProperties.put("BOD", "3.24");
        notifications.add(new Notification("Threshold Breach", "13:00 hours, Sept 10, 2014",  new ThresholdBreachNotificationDetails(activity, new AssetAggregationImpl(34, 67.78, 46.87,  mainTankProperties, 1, null), "Water Level","1000 litres", "2000 litres" ), false, R.drawable.alert,  false, "15:00 hours, Sept 10, 2014"));

        notifications.add(new Notification("Water Requirement for Tomorrow", "13:00 hours, Sept 10, 2014",  new WaterRequirementNotificationDetails(activity, "1000 litres", "1000 litres","1000 litres", "1000 litres" ), true, R.drawable.water_requirement, false, "15:00 hours, Sept 10, 2014"));
        notifications.add(new Notification("Leak Alert", "13:00 hours, Sept 10, 2014",  new LeakNotificationDetails(activity, "Pipe 123" ), true,  R.drawable.leaky_tap,  false, "15:00 hours, Sept 10, 2014"));
        notifications.add(new Notification("Water Garden", "13:00 hours, Sept 10, 2014",
                new WaterGardenNotificationDetails(activity, "5.0 ppm", "33 C","50 ", "Sunny", "3.0" ), false, R.drawable.tree
                , false, "15:00 hours, Sept 10, 2014"));
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

    public static AggregationImpl getDummyAggregationTree() {
        AggregationImpl iiitb = new AggregationImpl("IIIT-B", 100000, null, 3);
        iiitb.addChild(new AggregationImpl("MH1", 10000, iiitb, 1));
        iiitb.addChild(new AggregationImpl("MH2", 20000, iiitb, 2));
        AggregationImpl wh = new AggregationImpl("WH", 30000, iiitb,1);
        wh.addChild(new AggregationImpl("1st Floor", 7000, wh));
        wh.addChild(new AggregationImpl("2nd Floor", 11000, wh));
        wh.addChild(new AggregationImpl("3rd Floor", 4000, wh));
        wh.addChild(new AggregationImpl("4th Floor", 8000, wh, 1));
        iiitb.addChild(wh);
        iiitb.addChild(new AggregationImpl("Cafeteria", 5000, iiitb, 1));
        iiitb.addChild(new AggregationImpl("Academic Block", 5000, iiitb));

        AggregationImpl lawns = new AggregationImpl("Lawns", 30000, iiitb);

        Map<String, String> mainTankProperties = new HashMap<String, String>();
        mainTankProperties.put("storageLevel", "200000");
        mainTankProperties.put("capacity", "300000");
        mainTankProperties.put("PH", "5.2");
        mainTankProperties.put("BOD", "3.24");
        AssetAggregationImpl mainTank = new AssetAggregationImpl(1, 20, 30, mainTankProperties, 2, lawns);
        lawns.addChild(mainTank);

        Map<String, String> recyclingPlantProperties = new HashMap<String, String>();
        recyclingPlantProperties.put("storageLevel", "100000");
        recyclingPlantProperties.put("capacity", "150000");
        recyclingPlantProperties.put("flow", "5");
        recyclingPlantProperties.put("PH", "3");
        recyclingPlantProperties.put("BOD", "0.76");
        AssetAggregationImpl recyclingPlant = new AssetAggregationImpl(1, 20, 30, recyclingPlantProperties, 2, lawns);
        lawns.addChild(recyclingPlant);

        Map<String, String> pumpProperties = new HashMap<String, String>();
        pumpProperties.put("flow", "15");
        pumpProperties.put("ON", "true");
        AssetAggregationImpl pump = new AssetAggregationImpl(1, 20, 30, pumpProperties, 2, lawns);
        lawns.addChild(pump);
        iiitb.addChild(lawns);

        return iiitb;


    }

}
