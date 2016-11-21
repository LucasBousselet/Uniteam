package unidev.uniteam;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class DatabaseConnect {

    // TODO Change with the correct URL
    private static String DBUrl = "http://www.exemple.com/";

    public static void InsertIntoDB(JSONObject jsonObject, String DBUrlComplement) {

        try {
            String urlString = DBUrl + DBUrlComplement;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(jsonObject.toString());
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static JSONObject SelectFromDB(String DBUrlComplement) {
        try {
            String urlString = DBUrl + DBUrlComplement;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            /*
             * InputStreamOperations est une classe complémentaire:
             * Elle contient une méthode InputStreamToString.
             */
            String result = InputStreamOperations.InputStreamToString(inputStream);

            connection.disconnect();

            // On récupère le JSON complet
            return new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
