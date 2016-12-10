package unidev.uniteam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticipantsList extends AppCompatActivity implements DatabaseGet.OnJsonTransmissionCompleted {

    private List<Integer> participantID = new ArrayList<>();
    private List<Map<String, String>> participantsList = new ArrayList<>();
    private SimpleAdapter adapterParticipantsListView = null;

    protected void onResume() {
        super.onResume();

        // TODO comment when using database
        participantsList.clear();
        Map<String, String> am1 = new HashMap<>(2);
        am1.put("name", "Dark Vador");
        am1.put("email", "darkvador@villain.com");
        participantsList.add(am1);
        Map<String, String> am2 = new HashMap<>(2);
        am2.put("name", "Sauron");
        am2.put("email", "sauronthed4rkl0rd@villain.com");
        participantsList.add(am2);
        adapterParticipantsListView.notifyDataSetChanged();

        // TODO uncomment when using database
        ProgressDialog loading = ProgressDialog.show(ParticipantsList.this, "Please Wait...", null, true, true);
        //RefreshParticipantsList("participants");
        loading.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);

        ListView participantsListView = (ListView) findViewById(R.id.participants_list);
        adapterParticipantsListView = new SimpleAdapter(this, participantsList,
                android.R.layout.simple_list_item_2,
                new String[]{"name", "email"},
                new int[]{android.R.id.text1,
                        android.R.id.text2});

        participantsListView.setAdapter(adapterParticipantsListView);

    }

    public void onTransmissionCompleted(DatabaseGetResult result) {
        // Clear old participantsList
        participantsList.clear();
        participantID.clear();

        JSONArray jsonArray = result.getData();

        try {
            // Pour tous les objets on récupère les infos
            for (int i = 0; i < jsonArray.length(); i++) {
                // On récupère un objet JSON du tableau
                JSONObject obj = new JSONObject(jsonArray.getString(i));
                // On fait le lien Participants - Objet JSON
                Map<String, String> am1 = new HashMap<>(2);
                am1.put("name", obj.getString("nom"));
                am1.put("description", obj.getString("description"));
                participantsList.add(am1);
                participantID.add(obj.getInt("id"));
            }

            // Refresh ListView
            adapterParticipantsListView.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
