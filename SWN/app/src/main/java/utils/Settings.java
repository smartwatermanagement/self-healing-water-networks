package utils;

import com.example.android.swn.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kumudini on 11/1/14.
 */
public class Settings {
    public static Map<String, Integer> issueImageMap = new HashMap<String, Integer>();

    static{
        issueImageMap.put("WATER_REQUIREMENT_PREDICTION", R.drawable.water_requirement);
        issueImageMap.put("LEAK", R.drawable.leaky_tap);
        issueImageMap.put("WATER_GARDEN", R.drawable.tree);
        issueImageMap.put("THRESHOLD_BREACH", R.drawable.alert);
    }
}
