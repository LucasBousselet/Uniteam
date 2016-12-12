package unidev.uniteam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeetingsList extends AppCompatActivity implements DatabaseGet.OnJsonTransmissionCompleted {

    private List<Map<String, String>> meetingsList = new ArrayList<>();
    private List<Integer> meetingID = new ArrayList<>();
    private SimpleAdapter adapterMeetingsListView = null;
    private String currentProjectID;

    protected void onResume() {
        super.onResume();

        // TODO comment when using database
        meetingsList.clear();
        Map<String, String> am1 = new HashMap<>(2);
        am1.put("subject", "Test 1");
        am1.put("date", "Sub test 1");
        meetingsList.add(am1);
        Map<String, String> am2 = new HashMap<>(2);
        am2.put("subject", "Test 2");
        am2.put("date", "Sub test 2");
        meetingsList.add(am2);
        adapterMeetingsListView.notifyDataSetChanged();

        // TODO uncomment when using database
        ProgressDialog loading = ProgressDialog.show(MeetingsList.this, "Please Wait...", null, true, true);
        //RefreshProjectList("reunion");
        loading.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings_list);

        if (savedInstanceState != null) {
            // TODO uncomment when using database
            //currentProjectID = savedInstanceState.getString("currentProjectID");
        } else {
            Intent intent = getIntent();
            // TODO uncomment when using database
            //currentProjectID = getIntent().getStringExtra("currentProjectID");
        }

        ListView meetingListView = (ListView) findViewById(R.id.meetings_list);

        adapterMeetingsListView = new SimpleAdapter(this, meetingsList,
                android.R.layout.simple_list_item_2,
                new String[]{"subject", "date"},
                new int[]{android.R.id.text1,
                        android.R.id.text2});

        meetingListView.setAdapter(adapterMeetingsListView);

        // Floating action button used to launch a new activity to add a new meeting
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabMeetings);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent meetingAddNew = new Intent(MeetingsList.this, MeetingAddNew.class);
                startActivity(meetingAddNew);
            }
        });

        meetingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                // Intent used to start the activity ProjectPresentation
                Intent intent = new Intent(MeetingsList.this, MeetingDetails.class);
                parent.getItemAtPosition(position);

                // TODO Uncomment when using DB
                //intent.putExtra("meetingID", meetingID.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_project_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_about) {
            Intent aboutPage = new Intent(MeetingsList.this, AboutUs.class);
            startActivity(aboutPage);
        } else if (item.getItemId() == R.id.refresh) {
            // TODO Uncomment when using DB
            //RefreshMeetingsDescription("reunions");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void RefreshMeetingsDescription(String url) {
        DatabaseGet gj = new DatabaseGet(this);
        gj.execute(url);
    }

    public void onTransmissionCompleted(DatabaseGetResult result) {
        // Clear old meetingsList
        meetingsList.clear();

        JSONArray jsonArray = result.getData();

        try {
            // Pour tous les objets on récupère les infos
            for (int i = 0; i < jsonArray.length(); i++) {
                // On récupère un objet JSON du tableau
                JSONObject obj = new JSONObject(jsonArray.getString(i));
                // On fait le lien Projet - Objet JSON
                Map<String, String> am1 = new HashMap<>(3);
                am1.put("subject", obj.getString("sujet"));
                am1.put("description", obj.getString("description"));
                am1.put("date", obj.getString("date"));
                meetingsList.add(am1);
                meetingID.add(obj.getInt("id"));
            }

            // Refresh ListView
            adapterMeetingsListView.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
