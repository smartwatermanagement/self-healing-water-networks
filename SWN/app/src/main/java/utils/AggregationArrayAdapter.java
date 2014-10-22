package utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.swn.R;

import java.util.List;

import model.Aggregation;

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

        Log.d("AggregationArrayAdapter", "Creating view item " + ((Aggregation)itemsArrayList.get(position)).getName());

        View itemView = inflater.inflate(resource, parent, false);
        Aggregation aggregation = (Aggregation)itemsArrayList.get(position);

        TextView nameView = (TextView) itemView.findViewById(R.id.list_item_aggregation_name_textview);
        nameView.setText(aggregation.getName());
        if(aggregation.getIssueCount() > 0)
            itemView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.round_rect_shape_colored));

        return itemView;
    }
}
