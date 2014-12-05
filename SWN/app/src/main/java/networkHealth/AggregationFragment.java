package networkHealth;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import model.Aggregation;
import model.Asset;
import model.IAggregation;
import networkHealth.asynctasks.AggregationFetchTask;
import utils.AggregationArrayAdapter;
import utils.BackendURI;
import utils.JsonParser;


public class AggregationFragment extends Fragment implements AggregationFetchTask.AggregationFetchTaskCompletionListener {

    public Aggregation aggregation;
    public List<IAggregation> IAggregations;
    private OnAggregationSelectedListener aggregationSelectedListener;
    public static final String ASSET_PATH = "file:///android_asset/";
    private ArrayAdapter adapter;
    private GridView gridView;
    private TextView title;
    private static final String LOG_TAG = AggregationFragment.class.getSimpleName();
    private AggregationFetchTask aggregationFetchTask;

    public AggregationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getArguments() != null) {
            aggregation = (Aggregation) getArguments().getSerializable("aggregation");
        }
        View rootView = inflater.inflate(R.layout.fragment_asset, container, false);

        gridView = (GridView) rootView.findViewById(R.id.assetgridview);

        title = (TextView) rootView.findViewById(R.id.aggregation_title);

        Aggregation root;
        if(aggregation == null) {
            if (aggregationFetchTask != null)
                aggregationFetchTask.cancel(true);
            aggregationFetchTask = (new AggregationFetchTask(this));
            aggregationFetchTask.execute(BackendURI.getAggregationURI());

        }
        else {
            IAggregations = aggregation.getChildren();
            title.setText(aggregation.getName());
            adapter = new AggregationArrayAdapter<IAggregation>(
                    getActivity(),
                    R.layout.list_item_aggregations,
                    IAggregations);

            gridView.setAdapter(adapter);
        }


        // Event Handler for the list item, to drill down on an aggregation
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                IAggregation selectedIAggregation = (IAggregation)adapter.getItem(i);
                if (selectedIAggregation instanceof Aggregation
                        &&  ((Aggregation) selectedIAggregation).getChildren().size() > 0)
                    aggregationSelectedListener.onAggregationSelected(selectedIAggregation);
                else
                    Toast.makeText(getActivity().getBaseContext(), "No more Details available", Toast.LENGTH_SHORT).show();

            }
        });


        // Back Button Event Handler, to roll up
        ImageView imageView = (ImageView)rootView.findViewById(R.id.upimage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IAggregation IAggregation = adapter != null ? (IAggregation)adapter.getItem(0): null;
                if(IAggregation instanceof Aggregation
                        && ((Aggregation) IAggregation).getParent() != null
                        && ((Aggregation) IAggregation).getParent().getParent() != null){
                    aggregationSelectedListener.onAggregationBack(((Aggregation) IAggregation).getParent().getParent());
                }
                else if(IAggregation instanceof Asset
                        && ((Asset) IAggregation).getParent() != null
                        && ((Asset) IAggregation).getParent().getParent() != null){
                    aggregationSelectedListener.onAggregationBack(((Asset) IAggregation).getParent().getParent());
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

    @Override
    public void onAggregationFetchTaskCompletion(String json) {
        try {
            aggregation = JsonParser.parseAggregation(new JSONObject(json));
            title.setText(aggregation.getName());
            IAggregations = aggregation.getChildren();
            adapter = new AggregationArrayAdapter<IAggregation>(
                    getActivity(),
                    R.layout.list_item_aggregations,
                    IAggregations);

            gridView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelAsyncTask();
    }

    @Override
    public void onStop() {
        super.onStop();
        cancelAsyncTask();
    }

    private void cancelAsyncTask() {
        if (aggregationFetchTask != null)
            aggregationFetchTask.cancel(true);
    }

    public interface OnAggregationSelectedListener {
        public void onAggregationSelected(IAggregation IAggregation);
        public void onAggregationBack(IAggregation IAggregation);
    }

}
