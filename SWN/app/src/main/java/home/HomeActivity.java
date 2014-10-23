package home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.android.swn.R;

import assets.AssetFragment;
import model.Aggregation;
import model.AggregationImpl;
import notifications.NotificationsFragment;
import reports.AggregationBasedReportFragment;
import reports.ReportsFragment;


public class HomeActivity extends ActionBarActivity implements ActionBar.OnNavigationListener,
        AggregationBasedReportFragment.OnAggregationPieSelectedListener,
        AssetFragment.OnAggregationSelectedListener{

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
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
                                "Notifications(2)",
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

        Fragment selectedFragment = null;
        switch (position) {
            case 0 :
                selectedFragment = new NotificationsFragment();
                break;
            case 1:
                selectedFragment = new ReportsFragment();
                break;
            case 2:
                selectedFragment = new AssetFragment();
                break;
            default:
                Log.e(LOG_TAG, "Unknown item selected");
        }

        // When the given dropdown item is selected, show its contents in the
        // container view.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, selectedFragment)
                .commit();
        return true;
    }

    /**
     * Called by the RegionReportFragment when a 'pie' in the piechart representing the water usage across aggregations is selected
     * The aggregation for which a new piechart encolsed in a RegionReportFragment must be drawn
     */
    @Override
    public void onAggregationPieSelected(Aggregation aggregation) {
        AggregationImpl aggregationImpl = (AggregationImpl)aggregation;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.setBreadCrumbTitle(aggregationImpl.getParent().getName());

        AggregationBasedReportFragment aggregationBasedReportFragment = new AggregationBasedReportFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("aggregation", aggregationImpl);
        aggregationBasedReportFragment.setArguments(bundle);
        transaction.replace(R.id.regionreport, (Fragment)(aggregationBasedReportFragment)).commit();
    }


    @Override
    public void onAggregationSelected(Aggregation aggregation) {
        AggregationImpl aggregationImpl = (AggregationImpl)aggregation;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.setBreadCrumbTitle(aggregationImpl.getName());


        AssetFragment assetFragment = new AssetFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("aggregation", aggregationImpl);
        assetFragment.setArguments(bundle);
        transaction.replace(R.id.container, (Fragment)(assetFragment)).commit();
    }
}
