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
public class AlertNotificationDetails implements NotificationDetails {
    private String assetId;
    private String property;
    private String threshold;
    private String currentValue;
    private Context context;

    public AlertNotificationDetails(Context context, String assetId, String property, String threshold, String currentValue) {
        this.assetId = assetId;
        this.property = property;
        this.threshold = threshold;
        this.currentValue = currentValue;
        this.context = context;
    }

    @Override
    public View getView(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.alert_notification, parent, false);
        TextView value1 = (TextView)layout.findViewById(R.id.alert_value1_textview);
        value1.setText(assetId);
        TextView value2 = (TextView)layout.findViewById(R.id.alert_value2_textview);
        value2.setText(property);
        TextView value3 = (TextView)layout.findViewById(R.id.alert_value3_textview);
        value3.setText(threshold);
        TextView value4 = (TextView)layout.findViewById(R.id.alert_value4_textview);
        value4.setText(currentValue);

        return layout;

    }

    @Override
    public String getShortDescription(){
        return property + ": " + currentValue;
    }
}
