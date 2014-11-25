package home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.android.swn.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import model.Aggregation;
import model.Asset;
import model.IAggregation;
import model.LeakNotificationDetails;
import networkHealth.AggregationFragment;
import networkHealth.asynctasks.AggregationFetchTask;
import notifications.NotificationsFragment;
import reports.tabhostFragments.Reports;
import utils.BackendURI;
import utils.JsonParser;


public class HomeActivity extends ActionBarActivity implements ActionBar.OnNavigationListener,
        AggregationFragment.OnAggregationSelectedListener, LeakNotificationDetails.OnAssetClickedListener,
        AggregationFetchTask.AggregationFetchTaskCompletionListener{

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    private static Fragment selectedFragment;
    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[] {
                                getString(R.string.title_notifications),
                                getString(R.string.title_reports),
                                getString(R.string.title_assetview),
                        }),
                this);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getSupportActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getSupportActionBar().getSelectedNavigationIndex());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {

        selectedFragment = null;
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        switch (position) {
            case 0 :
                selectedFragment = new NotificationsFragment();
                break;
            case 1:
                selectedFragment = new Reports();
                break;
            case 2:
                selectedFragment = new AggregationFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("aggregation", aggregation);
                selectedFragment.setArguments(bundle);
                break;
            default:
                Log.e(LOG_TAG, "Unknown item selected");
        }

        // When the given dropdown item is selected, show its contents in the
        // container view.

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, selectedFragment)
                .commit();
        aggregation = null;
        return true;
    }

    @Override
    public void onAggregationSelected(IAggregation IAggregation) {
        Aggregation aggregation = (Aggregation) IAggregation;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.setBreadCrumbTitle(aggregation.getName());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);


        AggregationFragment aggregationFragment = new AggregationFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("aggregation", aggregation);
        aggregationFragment.setArguments(bundle);
        transaction.replace(R.id.container, (Fragment)(aggregationFragment)).commit();
    }

    @Override
    public void onAggregationBack(IAggregation IAggregation) {
        FragmentManager fragmentManager= getSupportFragmentManager();

        // If there is nothing on back stack, create new Aggregation fragment, else pop the back stack
        if(fragmentManager.getBackStackEntryCount() == 0) {
            Aggregation aggregation = (Aggregation) IAggregation;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setBreadCrumbTitle(aggregation.getName());
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);


            AggregationFragment aggregationFragment = new AggregationFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("aggregation", aggregation);
            aggregationFragment.setArguments(bundle);
            transaction.replace(R.id.container, (Fragment)(aggregationFragment)).commit();
        }
        else
            fragmentManager.popBackStack();
    }



    int assetId;
    @Override
    public void onAssetClicked(int assetId){
        this.assetId = assetId;
        new AggregationFetchTask(this).execute(BackendURI.getAggregationURI());

    }

    Aggregation aggregation = null;

    @Override
    public void onAggregationFetchTaskCompletion(String json) {
        aggregation = null;
        try {
            aggregation = JsonParser.parseAggregation(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get parent aggregation of asset with assetId
        aggregation = parseAggregationTree(aggregation, assetId);

        getSupportActionBar().setSelectedNavigationItem(2);

    }

    private Aggregation parseAggregationTree(IAggregation iAggregation,int assetId){
        Aggregation parent = null;
        Aggregation temp = null;

        if(iAggregation instanceof Aggregation){
            if(contains(((Aggregation) iAggregation).getChildren(), assetId))
                return (Aggregation)iAggregation;
            for(IAggregation iAggregation1: ((Aggregation)iAggregation).getChildren()) {
                temp = parseAggregationTree(iAggregation1, assetId);
                if(temp != null)
                    parent = temp;
            }
        }
        return parent;
    }

    private boolean contains(List<IAggregation> aggregations, int assetId){
        boolean result = false;
        for(IAggregation iAggregation: aggregations){
            if(iAggregation instanceof Asset && ((Asset)iAggregation).getAsset_id() == assetId){
                result = true;
                break;
            }
        }
        return result;
    }
   /* @Override
    public void onBackPressed() {

        List<Fragment> fragments1 = new ArrayList<Fragment>();
        List<Fragment> fragments = selectedFragment.getChildFragmentManager().getFragments();
        fragments1.addAll(fragments);
        for(Fragment fragment:fragments){
            fragments1.addAll(fragment.getChildFragmentManager().getFragments());
        }
        boolean result = false;
        Fragment theFragment = null;

        if(fragments1.size() > 0){
            for(Fragment fragment: fragments1){
                if(fragment != null && fragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
                    theFragment = fragment;
                    result = true;
                }
            }
        }
        if(theFragment != null)
            theFragment.getChildFragmentManager().popBackStack();
        else super.onBackPressed();
    }*/
}
