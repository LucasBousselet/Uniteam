package unidev.uniteam;

import android.os.AsyncTask;
import android.util.Base64;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class DatabaseGet extends AsyncTask<String, Void, String> {

    private OnJsonTransmitionCompleted mCallback;

    DatabaseGet(OnJsonTransmitionCompleted callback) {
        this.mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String uri = params[0];
        uri = "http://172.16.24.150/api/v1/" + uri;
        BufferedReader bufferedReader;

        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            String encoded = Base64.encodeToString(("unidev:unidev").getBytes("UTF-8"), Base64.NO_WRAP);
            urlConnection.setRequestProperty("Authorization", "Basic " + encoded);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            StringBuilder sb = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String get;
            while ((get = bufferedReader.readLine()) != null) {
                sb.append(get);
            }

            return sb.toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            this.mCallback.onTransmitionCompleted(new JSONArray(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    interface OnJsonTransmitionCompleted {
        void onTransmitionCompleted(JSONArray JSONArray);
    }

}
