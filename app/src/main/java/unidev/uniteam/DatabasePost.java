package unidev.uniteam;

import android.os.AsyncTask;
import android.util.Base64;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class DatabasePost extends AsyncTask<String, Void, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String uri = params[0];
        uri = "http://172.16.24.150/api/v1/" + uri;

        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            String encoded = Base64.encodeToString(("unidev:unidev").getBytes("UTF-8"), Base64.NO_WRAP);
            urlConnection.setRequestProperty("Authorization", "Basic " + encoded);
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(params[1]);

            writer.flush();
            writer.close();
            os.close();

            int responseCode = urlConnection.getResponseCode();
            // TODO Try with
            //URLConnection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}