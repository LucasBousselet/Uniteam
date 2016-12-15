package unidev.uniteam;

import android.os.AsyncTask;
import android.util.Base64;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class DatabaseGet extends AsyncTask<String, Void, String> {

    private OnJsonTransmissionCompleted mCallback;
    private String requestType;

    DatabaseGet(OnJsonTransmissionCompleted callback) {
        this.mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        requestType = params[1];
        String uri = "http://api-uniteam.spieldy.com/public/" + params[0];
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(uri);
            urlConnection = (HttpURLConnection) url.openConnection();

            String encoded = Base64.encodeToString(("unidev:unidev").getBytes("UTF-8"), Base64.NO_WRAP);
            urlConnection.setRequestProperty("Authorization", "Basic " + encoded);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String result;
            while ((result = bufferedReader.readLine()) != null) {
                stringBuilder.append(result);
            }
            return stringBuilder.toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            DatabaseGetResult result = new DatabaseGetResult(requestType, new JSONArray(s));
            this.mCallback.onTransmissionCompleted(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    interface OnJsonTransmissionCompleted {
        void onTransmissionCompleted(DatabaseGetResult getResult);
    }

}
