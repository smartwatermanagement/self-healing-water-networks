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
    private int assetId;
    private String property;
    private String threshold;
    private String currentValue;
    private Context context;

    public ThresholdBreachNotificationDetails(){

    }

    public ThresholdBreachNotificationDetails(Context context, int asset, String property, String threshold, String currentValue) {
        this.assetId = asset;
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
        value1.setText("" +     assetId);
        TextView value2 = (TextView)layout.findViewById(R.id.threshold_breach_value2_textview);
        value2.setText(property.toUpperCase());
        TextView value3 = (TextView)layout.findViewById(R.id.threshold_breach_value3_textview);
        value3.setText(threshold);
        TextView value4 = (TextView)layout.findViewById(R.id.threshold_breach_value4_textview);
        value4.setText(currentValue);

        return layout;

    }

    public int getAsset() {
        return assetId;
    }

    public void setAsset(int asset) {
        this.assetId = asset;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public String getShortDescription(){
        return property.toUpperCase() + ": " + currentValue;
    }
}
