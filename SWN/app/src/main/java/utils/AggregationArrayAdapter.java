package utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.swn.R;

import java.util.List;

import model.Aggregation;
import model.AggregationImpl;
import model.AssetAggregationImpl;

/**
 * Created by kumudini on 10/22/14.
 */
public class AggregationArrayAdapter<T> extends ArrayAdapter<T> {

    private Context context;
    private List<T> itemsArrayList;
    int resource;

    public AggregationArrayAdapter(Context context, int resource,
                                   List<T> objects) {
        super(context, resource, objects);
        this.context = context;
        this.itemsArrayList = objects;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewGroup itemView = (ViewGroup) inflater.inflate(resource, parent, false);

        Aggregation aggregation = (Aggregation) itemsArrayList.get(position);
        TextView nameView = (TextView) itemView.findViewById(R.id.list_item_aggregation_name_textview);
        TextView issueCountView = (TextView) itemView.findViewById(R.id.list_item_aggregation_issuecount_textview);

        if (aggregation instanceof AggregationImpl) {
            nameView.setText(((AggregationImpl) aggregation).getName());

            int issueCount = (((AggregationImpl) aggregation).getIssueCount());
            if (issueCount > 0) {
                issueCountView.setText("(Pending issues : " + String.valueOf(issueCount) + ")");
                itemView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.round_rect_shape_colored));
            }
            else {
                itemView.removeView(itemView);
            }

        } else if (aggregation instanceof AssetAggregationImpl) {
            AssetAggregationImpl asset = (AssetAggregationImpl) aggregation;
            int issueCount = asset.getIssueCount();
            if (issueCount > 0) {
                issueCountView.setText("(Pending issues : " + String.valueOf(issueCount) + ")");
                itemView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.round_rect_shape_colored));
            }
            else
                itemView.removeView(itemView);

            nameView.setText(asset.getType() + " " + String.valueOf(asset.getAsset_id()));
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(10, 10, 10, 10);

            TextView locationView = new TextView(context);
            locationView.setText("lat : " + asset.getLatitude() + ", long : " + asset.getLongitude());
            locationView.setGravity(Gravity.CENTER);
            layout.addView(locationView);
            for (String property : asset.getPropertyValue().keySet()) {
                TextView textView = new TextView(context);
                textView.setText(String.valueOf(property + " : " + asset.getPropertyValue().get(property)));
                textView.setGravity(Gravity.CENTER);

                layout.addView(textView);
            }
            layout.setGravity(Gravity.CENTER);
            itemView.addView(layout);
        }
        return itemView;
    }
}
