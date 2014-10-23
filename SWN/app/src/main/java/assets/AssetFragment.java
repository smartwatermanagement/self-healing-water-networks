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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.swn.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Aggregation;
import model.AggregationImpl;
import model.AssetAggregationImpl;
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
        AggregationImpl aggregationImpl = null;
        if(getArguments() != null)
            aggregationImpl = (AggregationImpl)getArguments().getSerializable("aggregation");

        View rootView = inflater.inflate(R.layout.fragment_asset, container, false);

        final GridView gridView = (GridView) rootView.findViewById(R.id.assetgridview);
        aggregationList = new ArrayList();

        if(aggregationImpl == null) {
            AggregationImpl iiitb = new AggregationImpl("IIIT-B", 100000, null, 3);
            iiitb.addChild(new AggregationImpl("MH1", 10000, iiitb, 1));
            iiitb.addChild(new AggregationImpl("MH2", 20000, iiitb, 2));
            AggregationImpl wh = new AggregationImpl("WH", 30000, iiitb,1);
            wh.addChild(new AggregationImpl("1st Floor", 7000, wh));
            wh.addChild(new AggregationImpl("2nd Floor", 11000, wh));
            wh.addChild(new AggregationImpl("3rd Floor", 4000, wh));
            wh.addChild(new AggregationImpl("4th Floor", 8000, wh, 1));
            iiitb.addChild(wh);
            iiitb.addChild(new AggregationImpl("Cafeteria", 5000, iiitb, 1));
            iiitb.addChild(new AggregationImpl("Academic Block", 5000, iiitb));

            AggregationImpl lawns = new AggregationImpl("Lawns", 30000, iiitb);

            Map<String, String> mainTankProperties = new HashMap<String, String>();
            mainTankProperties.put("storageLevel", "200000");
            mainTankProperties.put("capacity", "300000");
            mainTankProperties.put("PH", "5.2");
            mainTankProperties.put("BOD", "3.24");
            AssetAggregationImpl mainTank = new AssetAggregationImpl("1", "20", "30", mainTankProperties, 2, lawns);
            lawns.addChild(mainTank);

            Map<String, String> recyclingPlantProperties = new HashMap<String, String>();
            recyclingPlantProperties.put("storageLevel", "100000");
            recyclingPlantProperties.put("capacity", "150000");
            recyclingPlantProperties.put("flow", "5");
            recyclingPlantProperties.put("PH", "3");
            recyclingPlantProperties.put("BOD", "0.76");
            AssetAggregationImpl recyclingPlant = new AssetAggregationImpl("1", "20", "30", recyclingPlantProperties, 2, lawns);
            lawns.addChild(recyclingPlant);

            Map<String, String> pumpProperties = new HashMap<String, String>();
            pumpProperties.put("flow", "15");
            pumpProperties.put("ON", "true");
            AssetAggregationImpl pump = new AssetAggregationImpl("1", "20", "30", pumpProperties, 2, lawns);
            lawns.addChild(pump);
            iiitb.addChild(lawns);

            Log.d("AssetFragment", "CreatedList");

            aggregationList.add(iiitb);
            aggregationList.add(wh);
        }
        else {
            aggregationList.addAll(aggregationImpl.getChildren());

            Log.d("AssetFragment", "aggregation is " + aggregationImpl.getName() + " " + aggregationImpl.getChildren().size());

        }
        final ArrayAdapter adapter = new AggregationArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_aggregations,
                aggregationList);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Aggregation selectedAggregation = (Aggregation)adapter.getItem(i);
                if (selectedAggregation instanceof AggregationImpl
                        &&  ((AggregationImpl)selectedAggregation).getChildren().size() > 0)
                    aggregationSelectedListener.onAggregationSelected(selectedAggregation);
                else
                    Toast.makeText(getActivity().getBaseContext(), "No more aggregations", Toast.LENGTH_SHORT).show();

            }
        });

        gridView.setAdapter(adapter);

        ImageView imageView = (ImageView)rootView.findViewById(R.id.upimage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Aggregation aggregation = (Aggregation)adapter.getItem(0);
                if(aggregation instanceof AggregationImpl){
                    aggregationSelectedListener.onAggregationSelected(((AggregationImpl)aggregation).getParent().getParent());
                }
                else if(aggregation instanceof AssetAggregationImpl){
                    Log.d("AssetFragment", ((AssetAggregationImpl)aggregation).getAsset_id() );
                    aggregationSelectedListener.onAggregationSelected(((AssetAggregationImpl)aggregation).getParent().getParent());
                }
            }
        });

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
