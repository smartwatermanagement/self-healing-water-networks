package reports.asyncTask;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.Asset;
import model.AssetType;
import utils.JsonParser;

/**
 * Fetches the names of the storage assets from the backend and sets the associated adapter with the same
 * Created by kempa on 11/11/14.
 */
public class StorageFetcher extends AsyncTask<String, Void, String> {

    private final String LOG_TAG = StorageFetcher.class.getSimpleName();
    private ArrayAdapter<CharSequence> adapter;

    public StorageFetcher(ArrayAdapter<CharSequence> adapter) {
        this.adapter = adapter;
    }

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;

        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else {
                //Close the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (NoHttpResponseException e) {
            responseString = "";
            //Toast.makeText(, "No response from server", Toast.LENGTH_SHORT);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
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
                Log.d(LOG_TAG, asset.getName());
                adapter.add(asset.getName());
            }
        }
    }
}
