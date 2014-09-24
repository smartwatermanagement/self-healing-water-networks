package utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.swn.R;

import java.util.List;
import java.util.Map;

/**
 * Created by kumudini on 9/23/14.
 */
public class NotificationsArrayAdapter<T> extends ArrayAdapter<T> {
    private Map<String, Integer> imageTitleMap;
    private final Context context;
    private final List<T> itemsArrayList;
    private final int resource;

    public NotificationsArrayAdapter(Context context, int resource,
                                     List<T> objects, Map<String, Integer> imageTitleMap) {
        super(context, resource, objects);
        this.context = context;
        this.itemsArrayList = objects;
        this.imageTitleMap = imageTitleMap;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View itemView = inflater.inflate(resource, parent, false);

        TextView titleView = (TextView) itemView.findViewById(R.id.list_item_notification_title_textview);
        TextView descriptionView = (TextView) itemView.findViewById(R.id.list_item_notification_description_textview);

        String[] strings = itemsArrayList.get(position).toString().split("-");
        titleView.setText(strings[0]);
        if(strings.length > 1)
            descriptionView.setText(strings[1]);
        titleView.setCompoundDrawablesWithIntrinsicBounds(
                imageTitleMap.get(titleView.getText().toString()),
                0,
                0,
                0);

        return itemView;
    }
}
