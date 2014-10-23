package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.swn.R;

/**
 * Created by kumudini on 10/21/14.
 */
public class ThresholdBreachNotificationDetails implements NotificationDetails {
    private AssetAggregationImpl asset;
    private String property;
    private String threshold;
    private String currentValue;
    private Context context;

    public ThresholdBreachNotificationDetails(Context context, AssetAggregationImpl asset, String property, String threshold, String currentValue) {
        this.asset = asset;
        this.property = property;
        this.threshold = threshold;
        this.currentValue = currentValue;
        this.context = context;
    }

    @Override
    public View getView(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.threshold_breach_notification, parent, false);
        TextView value1 = (TextView)layout.findViewById(R.id.threshold_breach_value1_textview);
        value1.setText("Main Tank " + asset.getAsset_id());
        TextView value2 = (TextView)layout.findViewById(R.id.threshold_breach_value2_textview);
        value2.setText(property);
        TextView value3 = (TextView)layout.findViewById(R.id.threshold_breach_value3_textview);
        value3.setText(threshold);
        TextView value4 = (TextView)layout.findViewById(R.id.threshold_breach_value4_textview);
        value4.setText(currentValue);

        return layout;

    }

    @Override
    public String getShortDescription(){
        return property + ": " + currentValue;
    }
}
