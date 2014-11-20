package utils;

import com.example.android.swn.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kumudini on 11/1/14.
 */
public class Settings {

    public static Map<String, Integer> issueImageMap = new HashMap<String, Integer>();
    public static Map<String, String> issueTypeTitleMap = new HashMap<String, String>();

    static{
        issueImageMap.put("WATER_REQUIREMENT_PREDICTION", R.drawable.water_requirement);
        issueImageMap.put("LEAK", R.drawable.leaky_tap);
        issueImageMap.put("WATER_GARDEN", R.drawable.tree);
        issueImageMap.put("THRESHOLD_BREACH", R.drawable.alert);
        issueTypeTitleMap.put("WATER_REQUIREMENT_PREDICTION", "Water Requirement Prediction");
        issueTypeTitleMap.put("LEAK", "Leak Alert");
        issueTypeTitleMap.put("WATER_GARDEN", "Water Garden");
        issueTypeTitleMap.put("THRESHOLD_BREACH", "Threshold Breach Alert");
    }
}
