package utils;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kumudini on 9/23/14.
 */
public class Utils {


    private static final String LOG_TAG = Utils.class.getSimpleName();

    public static String getFormattedDateString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return simpleDateFormat.format(date);
    }

    public static String fetchGetResponse(String uri) {
        HttpClient httpclient = new DefaultHttpClient();
        String responseString = "";

        try {
            Log.d(LOG_TAG, uri);
            HttpResponse response = httpclient.execute(new HttpGet(uri));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else {
                response.getEntity().getContent().close();
                throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (NoHttpResponseException e) {
            responseString = ""; // TODO : Must find a way to communicate this to the user
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "Response String : " + responseString);
        return responseString;
    }

    public static void sendDeleteHttpRequest(String uri){
        try {
            URL url = new URL(uri);
            Log.d("Utils", "URI is " + uri);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestProperty("Content-Type", "application/json");
            httpCon.setRequestMethod("DELETE");
            httpCon.connect();
            Log.d("Utils", "" + httpCon.getResponseCode());
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void sendUpdateHttpRequest(String uri, String data){
        try {
            URL url = new URL(uri);
            Log.d("Utils", "URI is " + uri);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            DataOutputStream dataOutputStream = null;
            httpCon.setRequestProperty("Content-Type", "application/xml");
            httpCon.setRequestMethod("PUT");
            httpCon.setDoOutput(true);
            dataOutputStream = new DataOutputStream(httpCon.getOutputStream());
            dataOutputStream.writeBytes(data);
            httpCon.connect();
            Log.d("Utils", "" + httpCon.getResponseCode());
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }


}
