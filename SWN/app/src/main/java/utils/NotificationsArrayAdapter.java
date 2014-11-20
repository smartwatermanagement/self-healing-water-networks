package utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.swn.R;

import java.util.List;

import model.Notification;

/**
 * Created by kumudini on 9/23/14.
 */
public class NotificationsArrayAdapter<T> extends ArrayAdapter<T> {
    private final Context context;
    private final List<T> itemsArrayList;
    private final int resource;

    public NotificationsArrayAdapter(Context context, int resource,
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


        View itemView = inflater.inflate(resource, parent, false);

        TextView titleView = (TextView) itemView.findViewById(R.id.list_item_notification_title_textview);
        TextView descriptionView = (TextView) itemView.findViewById(R.id.list_item_notification_description_textview);
        TextView dateView = (TextView) itemView.findViewById(R.id.list_item_notification_date_textview);
        ImageView image = (ImageView) itemView.findViewById(R.id.icon);
        TextView status = (TextView) itemView.findViewById(R.id.status);
        Notification not = (Notification)itemsArrayList.get(position);
        titleView.setText(not.getTitle());
        descriptionView.setText(not.getDescription());
        dateView.setText(not.getDate());
        image.setImageResource(not.getImage());
        status.setText(not.getStatus().toString());
        switch(not.getStatus()) {
            case NEW : status.setTextColor(Color.RED);
                break;
            case IN_PROGRESS: status.setTextColor(Color.BLUE);
                break;
            case RESOLVED: status.setTextColor(Color.DKGRAY);
                    break;
        }
        if(!not.isRead()){
            titleView.setTypeface(null, Typeface.BOLD);
            itemView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rect_shape_colored));
        }
        else {
            titleView.setTextColor(Color.DKGRAY);
            descriptionView.setTextColor(Color.GRAY);
        }
        return itemView;
    }
}
