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
public class WaterGardenNotificationDetails implements NotificationDetails {
    private String currentSoilMoisture;
    private String weatherPredictionTemperature;
    private String weatherPredictionHumidity;
    private String weatherPredictionText;
    private String soilMoistureThreshold;
    private Context context;

    public WaterGardenNotificationDetails(Context context, String currentSoilMoisture, String weatherPredictionTemperature,
                                          String weatherPredictionHumidity, String weatherPredictionText,
                                          String soilMoistureThreshold) {
        this.currentSoilMoisture = currentSoilMoisture;
        this.weatherPredictionTemperature = weatherPredictionTemperature;
        this.weatherPredictionHumidity = weatherPredictionHumidity;
        this.weatherPredictionText = weatherPredictionText;
        this.soilMoistureThreshold = soilMoistureThreshold;
        this.context = context;
    }

    @Override
    public View getView(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.water_garden_notification, parent, false);
        TextView value1 = (TextView)layout.findViewById(R.id.water_garden_value1_textview);
        value1.setText(currentSoilMoisture);
        TextView value2 = (TextView)layout.findViewById(R.id.water_garden_value2_textview);
        value2.setText(soilMoistureThreshold);
        TextView value3 = (TextView)layout.findViewById(R.id.water_garden_value3_textview);
        value3.setText(weatherPredictionTemperature);
        TextView value4 = (TextView)layout.findViewById(R.id.water_garden_value4_textview);
        value4.setText(weatherPredictionHumidity);
        TextView value5 = (TextView)layout.findViewById(R.id.water_garden_value5_textview);
        value5.setText(weatherPredictionText);

        return layout;

    }

    @Override
    public String getShortDescription(){
        return weatherPredictionText;
    }
}
