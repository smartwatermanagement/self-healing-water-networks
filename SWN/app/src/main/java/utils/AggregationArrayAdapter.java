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

        ViewGroup itemView = (ViewGroup)inflater.inflate(resource, parent, false);
        Aggregation aggregation = (Aggregation)itemsArrayList.get(position);
        TextView nameView = (TextView) itemView.findViewById(R.id.list_item_aggregation_name_textview);
        TextView issueCountView = (TextView)itemView.findViewById(R.id.list_item_aggregation_issuecount_textview);

        if(aggregation instanceof AggregationImpl) {
            nameView.setText(((AggregationImpl) aggregation).getName());
            issueCountView.setText(((AggregationImpl) aggregation).getIssueCount());
            if (((AggregationImpl)aggregation).getIssueCount() > 0)
                itemView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.round_rect_shape_colored));
        }
        else if (aggregation instanceof AssetAggregationImpl) {
                AssetAggregationImpl asset = (AssetAggregationImpl)aggregation;
                issueCountView.setText(asset.getIssueCount());
                nameView.setText(asset.getAsset_id());
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(10, 10, 10, 10);
                for(String property: asset.getPropertyValue().keySet()){
                    TextView textView = new TextView(context);
                    textView.setText(property + ": " + asset.getPropertyValue().get(property));

                    layout.addView(textView);
                }
            layout.setGravity(Gravity.CENTER);
            itemView.addView(layout);



        }
        return itemView;
    }
}
