package unidev.uniteam;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportsList extends AppCompatActivity implements DatabaseGet.OnJsonTransmissionCompleted {

    private List<Integer> reportsID = new ArrayList<>();
    private List<Map<String, String>> reportsList = new ArrayList<>();
    private SimpleAdapter adapterReportsListView = null;

    protected void onResume() {
        super.onResume();

        // TODO comment when using database
        reportsList.clear();
        Map<String, String> am1 = new HashMap<>(2);
        am1.put("title", "Lancement du projet");
        am1.put("date", "10/12/2016");
        reportsList.add(am1);
        Map<String, String> am2 = new HashMap<>(2);
        am2.put("title", "Compte-rendu de Noël");
        am2.put("date", "25/12/2016");
        reportsList.add(am2);
        adapterReportsListView .notifyDataSetChanged();

        // TODO uncomment when using database
        ProgressDialog loading = ProgressDialog.show(ReportsList.this, "Please Wait...", null, true, true);
        //RefreshParticipantsList("participants");
        loading.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_list);

        ListView reportsListView = (ListView) findViewById(R.id.reports_list);
        adapterReportsListView  = new SimpleAdapter(this, reportsList,
                android.R.layout.simple_list_item_2,
                new String[]{"title", "date"},
                new int[]{android.R.id.text1,
                        android.R.id.text2});

        reportsListView.setAdapter(adapterReportsListView);
    }

    public void onTransmissionCompleted(DatabaseGetResult result) {
        // Clear old participantsList
        reportsList.clear();
        reportsID.clear();

        JSONArray jsonArray = result.getData();

        try {
            // Pour tous les objets on récupère les infos
            for (int i = 0; i < jsonArray.length(); i++) {
                // On récupère un objet JSON du tableau
                JSONObject obj = new JSONObject(jsonArray.getString(i));
                // On fait le lien Participants - Objet JSON
                Map<String, String> am1 = new HashMap<>(2);
                am1.put("title", obj.getString("titre"));
                am1.put("date", obj.getString("date"));
                reportsList.add(am1);
                reportsID.add(obj.getInt("id"));
            }

            // Refresh ListView
            adapterReportsListView.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
