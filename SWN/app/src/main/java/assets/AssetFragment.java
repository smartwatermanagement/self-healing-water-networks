package assets;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.swn.R;

import java.util.List;

import model.Aggregation;
import model.AggregationImpl;
import model.AssetAggregationImpl;
import model.DummyDataCreator;
import utils.AggregationArrayAdapter;


public class AssetFragment extends Fragment {


    private OnAggregationSelectedListener aggregationSelectedListener;
    public static final String ASSET_PATH = "file:///android_asset/";


    public AssetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AggregationImpl aggregationImpl = null;
        if(getArguments() != null) {
            aggregationImpl = (AggregationImpl) getArguments().getSerializable("aggregation");
        }
        View rootView = inflater.inflate(R.layout.fragment_asset, container, false);

        final GridView gridView = (GridView) rootView.findViewById(R.id.assetgridview);

        TextView title = (TextView) rootView.findViewById(R.id.aggregation_title);
        List<Aggregation> aggregations = null;
        if(aggregationImpl == null) {
            AggregationImpl root = new DummyDataCreator().getDummyAggregationTree();
            aggregations = root.getChildren();
            title.setText(root.getName());
        }
        else {
            aggregations = aggregationImpl.getChildren();
            title.setText(aggregationImpl.getName());
        }
        final ArrayAdapter adapter = new AggregationArrayAdapter<Aggregation>(
                getActivity(),
                R.layout.list_item_aggregations,
                aggregations);


        // Event Handler for the list item, to drill down on an aggregation
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Aggregation selectedAggregation = (Aggregation)adapter.getItem(i);
                if (selectedAggregation instanceof AggregationImpl
                        &&  ((AggregationImpl)selectedAggregation).getChildren().size() > 0)
                    aggregationSelectedListener.onAggregationSelected(selectedAggregation);
                else
                    Toast.makeText(getActivity().getBaseContext(), "No more Details available", Toast.LENGTH_SHORT).show();

            }
        });

        gridView.setAdapter(adapter);

        // Back Button Event Handler, to roll up
        ImageView imageView = (ImageView)rootView.findViewById(R.id.upimage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Aggregation aggregation = (Aggregation)adapter.getItem(0);
                if(aggregation instanceof AggregationImpl
                        && ((AggregationImpl)aggregation).getParent() != null
                        && ((AggregationImpl)aggregation).getParent().getParent() != null){
                    aggregationSelectedListener.onAggregationSelected(((AggregationImpl)aggregation).getParent().getParent());
                }
                else if(aggregation instanceof AssetAggregationImpl
                        && ((AssetAggregationImpl)aggregation).getParent() != null
                        && ((AssetAggregationImpl)aggregation).getParent().getParent() != null){
                    aggregationSelectedListener.onAggregationSelected(((AssetAggregationImpl)aggregation).getParent().getParent());
                }
                else
                    Toast.makeText(getActivity().getBaseContext(), "Further roll up not possible", Toast.LENGTH_SHORT).show();

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
