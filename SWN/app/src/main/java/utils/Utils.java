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
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kumudini on 9/23/14.
 */
public class Utils {


    private static final String LOG_TAG = Utils.class.getSimpleName();

    public static final String APP_DATE_FORMAT = "MMM dd, yyyy";
    public static final String REST_API_DATE_FORMAT = "yyyy-MM-dd";

    public static String getDateString(int year, int month, int day, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(year - 1900, month, day));
    }

    public static String getDateString(String dateString, String inputFormatString, String outputFormatString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputFormatString);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputFormatString);

        try {
            return outputFormat.format(inputFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Calendar getCalender(String dateString, String format) {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(new SimpleDateFormat(format).parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c;
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
                Log.d(LOG_TAG, "Response String : " + responseString);
            } else {
                response.getEntity().getContent().close();
                throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (NoHttpResponseException e) {
            Log.d(LOG_TAG, "No Http response from backend");
            responseString = ""; // TODO : Must find a way to communicate this to the user
        } catch (HttpHostConnectException e) {
            Log.d(LOG_TAG, "Unable to connect to backend");
            responseString = ""; // TODO : Must find a way to communicate this to the user
        } catch (IOException e) {
            e.printStackTrace();
        }
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
