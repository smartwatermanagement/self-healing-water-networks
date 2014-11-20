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
public class WaterRequirementNotificationDetails implements NotificationDetails {
    private String lastDayConsumption;
    private String predictedRequirement;
    private String currentAvailableInStorages;
    private String toBeOrdered;
    private Context context;

    public WaterRequirementNotificationDetails(Context context, String lastDayConsumption, String predictedRequirement,
                                               String currentAvailableInStorages, String toBeOrdered){
        this.context = context;
        this.lastDayConsumption = lastDayConsumption;
        this.predictedRequirement = predictedRequirement;
        this.currentAvailableInStorages = currentAvailableInStorages;
        this.toBeOrdered = toBeOrdered;

    }

    @Override
    public View getView(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.water_requirement_notification, parent, false);
        TextView value1 = (TextView)layout.findViewById(R.id.water_requirement_value1_textview);
        value1.setText(lastDayConsumption);
        TextView value2 = (TextView)layout.findViewById(R.id.water_requirement_value2_textview);
        value2.setText(predictedRequirement);
        TextView value3 = (TextView)layout.findViewById(R.id.water_requirement_value3_textview);
        value3.setText(currentAvailableInStorages);
        TextView value4 = (TextView)layout.findViewById(R.id.water_requirement_value4_textview);
        value4.setText(toBeOrdered);

        return layout;

    }

    @Override
    public String getShortDescription(){
        return predictedRequirement;
    }
}
