package assets;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.swn.R;

import java.util.ArrayList;
import java.util.List;

import model.Aggregation;
import utils.AggregationArrayAdapter;


public class AssetFragment extends Fragment {


    private OnAggregationSelectedListener aggregationSelectedListener;
    public static final String ASSET_PATH = "file:///android_asset/";
    private List aggregationList;


    public AssetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Aggregation aggregation = null;
        if(getArguments() != null)
            aggregation = (Aggregation)getArguments().getSerializable("aggregation");

        View rootView = inflater.inflate(R.layout.fragment_asset, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.assetgridview);
        aggregationList = new ArrayList();

        if(aggregation == null) {
            Aggregation iiitb = new Aggregation("IIIT-B", 100000, null, 3);
            iiitb.addChild(new Aggregation("MH1", 10000, iiitb, 1));
            iiitb.addChild(new Aggregation("MH2", 20000, iiitb, 2));
            Aggregation wh = new Aggregation("WH", 30000, iiitb,1);
            wh.addChild(new Aggregation("1st Floor", 7000, wh));
            wh.addChild(new Aggregation("2nd Floor", 11000, wh));
            wh.addChild(new Aggregation("3rd Floor", 4000, wh));
            wh.addChild(new Aggregation("4th Floor", 8000, wh, 1));
            iiitb.addChild(wh);
            iiitb.addChild(new Aggregation("Cafeteria", 5000, iiitb, 1));
            iiitb.addChild(new Aggregation("Academic Block", 5000, iiitb));
            iiitb.addChild(new Aggregation("Lawns", 30000, iiitb));


            Log.d("AssetFragment", "CreatedList");

            aggregationList.add(iiitb);
            aggregationList.add(wh);
        }
        else {
            aggregationList.addAll(aggregation.getChildren());

            Log.d("AssetFragment", "aggregation is " + aggregation.getName() + " " + aggregation.getChildren().size());

        }
        final ArrayAdapter adapter = new AggregationArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_aggregations,
                aggregationList);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Aggregation selectedAggregation = (Aggregation)adapter.getItem(i);
                if (selectedAggregation.getChildren().size() > 0)
                    aggregationSelectedListener.onAggregationSelected(selectedAggregation);
                else
                    Toast.makeText(getActivity().getBaseContext(), "No more aggregations", Toast.LENGTH_SHORT).show();

            }
        });

        gridView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            aggregationSelectedListener = (OnAggregationSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnAggregationSelectedListener");
        }
    }


    public interface OnAggregationSelectedListener {
        public void onAggregationSelected(Aggregation aggregation);
    }

}
