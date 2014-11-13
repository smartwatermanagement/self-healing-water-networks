package reports.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.Asset;
import model.AssetType;
import utils.JsonParser;
import utils.StorageArrayAdapter;
import utils.Utils;

/**
 * Fetches the names of the storage assets from the backend and sets the associated adapter with the same
 * Created by kempa on 11/11/14.
 */
public class StorageFetcher extends AsyncTask<String, Void, String> {

    private final String LOG_TAG = StorageFetcher.class.getSimpleName();
    private StorageArrayAdapter<String> storageArrayAdapter;

    public StorageFetcher(StorageArrayAdapter<String> adapter) {
        this.storageArrayAdapter = adapter;
    }

    @Override
    protected String doInBackground(String... uri) {
        return Utils.fetchGetResponse(uri[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result.length() == 0)
            return; // no http response

        List<Asset> assets = new ArrayList<Asset>();
        Log.d(LOG_TAG, "json is " + result);
        try {
            assets = JsonParser.parseAssets(new JSONArray(result));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<String> sourceNames = new LinkedList<String>();
        for (Asset asset : assets) {
            if (asset.getType().equalsIgnoreCase(AssetType.STORAGE.toString())) {
                storageArrayAdapter.add(asset.getName(), asset.getAsset_id());
            }
        }
    }
}
