package model;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.swn.R;

/**
 * Created by kumudini on 10/21/14.
 */
public class LeakNotificationDetails implements NotificationDetails {
    private String assetId;
    private Context context;

    public LeakNotificationDetails(Context context, String assetId){
        this.context = context;
        this.assetId = assetId;

    }

    @Override
    public View getView(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.leak_notification, parent, false);
        TextView value1 = (TextView)layout.findViewById(R.id.leak_value1_textview);
        value1.setText(assetId);
        TextView link = (TextView)layout.findViewById(R.id.leak_asset_link_textview);
        link.setPaintFlags(value1.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OnAssetClickedListener)context).onAssetClicked(Integer.parseInt(assetId));
            }
        });
        return layout;

    }

    @Override
    public String getShortDescription(){
        return "Asset ID: " + assetId;
    }

    public interface OnAssetClickedListener {
        public void onAssetClicked(int assetId);
    }
}
