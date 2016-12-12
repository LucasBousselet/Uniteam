package unidev.uniteam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MeetingDetails extends AppCompatActivity implements DatabaseGet.OnJsonTransmissionCompleted {

    private String currentMeetingID;
    private Map<String, String> hashMapMeeting = new HashMap<>(3);

    protected void onResume() {
        super.onResume();
        // TODO Uncomment when using DB
        //RefreshMeetingDescription("taches/" + CurrentTaskID);
        //findViewById(R.id.parentTask).invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);

        if (savedInstanceState != null) {
            // TODO uncomment when using database
            //currentMeetingID = savedInstanceState.getString("currentMeetingID");
        } else {
            Intent intent = getIntent();
            // TODO uncomment when using database
            //currentMeetingID = getIntent().getStringExtra("currentMeetingID");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.getString("currentProjectID") != null) {
            savedInstanceState.remove("currentProjectID");
        }
        savedInstanceState.putString("currentProjectID", currentMeetingID);
    }

    public void RefreshMeetingDescription(String url) {
        DatabaseGet gj = new DatabaseGet(this);
        gj.execute(url);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_validate, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.validate:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onTransmissionCompleted(DatabaseGetResult result) {

        JSONArray jsonArray = result.getData();

        try {
            String myFormat = "EEE, d MMM yyyy HH:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

            // Pour tous les objets on récupère les infos
            for (int i = 0; i < jsonArray.length(); i++) {
                // On récupère un objet JSON du tableau
                JSONObject obj = new JSONObject(jsonArray.getString(i));
                // On fait le lien Projet - Objet JSON
                hashMapMeeting.put("subject", obj.getString("nom"));
                hashMapMeeting.put("description", obj.getString("description"));
                hashMapMeeting.put("date", obj.getString("date"));

                /*
                String dateStr = obj.getString("deadlineField");
                String ackwardRipOff = dateStr.replace("/Date(", "").replace(")/", "");
                Long timeInMillis = Long.valueOf(ackwardRipOff);
                */
            }

            TextView meetingSubject = (TextView) findViewById(R.id.meeting_subject);
            meetingSubject.setText(hashMapMeeting.get("subject"));

            TextView meetingDescription = (TextView) findViewById(R.id.meeting_description);
            meetingDescription.setText(hashMapMeeting.get("description"));

            TextView meetingDate = (TextView) findViewById(R.id.meeting_date);
            meetingDate.setText(hashMapMeeting.get("date"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
