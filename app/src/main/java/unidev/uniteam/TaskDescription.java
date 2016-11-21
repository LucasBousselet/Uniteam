package unidev.uniteam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TaskDescription extends AppCompatActivity {

    private int taskID;
    private Map<String, String> hashMapString = new HashMap<>(3);
    private Map<String, Integer> hashMapInt = new HashMap<>(3);
    private Calendar deadline;

    protected void onResume() {
        super.onResume();
        // TODO Uncomment when using DB
        //RefreshTaskDescription();
        findViewById(R.id.parentTask).invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_description);

        Intent intent = getIntent();
        taskID = intent.getIntExtra("taskID", -1);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_task_state);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.task_state, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_validate, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.validate:

                // TODO Uncomment when using database

                try {
                    Spinner taskState = (Spinner) findViewById(R.id.spinner_task_state);
                    SeekBar taskProgress = (SeekBar) findViewById(R.id.task_progress);

                    JSONObject newTaskProgress = new JSONObject();

                    newTaskProgress.put("etat", taskState.getSelectedItem().toString());
                    newTaskProgress.put("avancement", taskProgress.getProgress());

                    // TODO Put the correct URL complement
                    DatabaseConnect.InsertIntoDB(newTaskProgress, "SetTask");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void RefreshTaskDescription() {
        try {
            // On récupère le JSON complet
            // TODO Put the correct URL complement
            JSONObject jsonObject = DatabaseConnect.SelectFromDB("GetProject");

            // On récupère le tableau d'objets qui nous concerne
            JSONArray array;
            if (jsonObject != null) {
                array = new JSONArray(jsonObject.getString("projet"));

                String myFormat = "EEE, d MMM yyyy HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

                // Pour tous les objets on récupère les infos
                for (int i = 0; i < array.length(); i++) {
                    // On récupère un objet JSON du tableau
                    JSONObject obj = new JSONObject(array.getString(i));
                    // On fait le lien Projet - Objet JSON
                    hashMapString.put("name", obj.getString("nom"));
                    hashMapString.put("description", obj.getString("description"));
                    hashMapString.put("etat", obj.getString("etat"));

                    hashMapInt.put("id", obj.getInt("id"));
                    hashMapInt.put("progress", obj.getInt("avancement"));
                    hashMapInt.put("duration", obj.getInt("duree"));

                    String dateStr = obj.getString("deadlineField");
                    deadline = Calendar.getInstance();
                    String ackwardRipOff = dateStr.replace("/Date(", "").replace(")/", "");
                    Long timeInMillis = Long.valueOf(ackwardRipOff);
                    deadline.setTimeInMillis(timeInMillis);
                }
                setTitle(hashMapString.get("nom"));

                TextView taskDescription = (TextView) findViewById(R.id.task_description_text);
                taskDescription.setText(hashMapString.get("description"));

                TextView taskDeadline = (TextView) findViewById(R.id.task_deadline);
                taskDeadline.setText(sdf.format(deadline.getTime()));

                TextView taskDuration = (TextView) findViewById(R.id.task_duration);
                taskDuration.setText(hashMapInt.get("duration"));

                Spinner taskState = (Spinner) findViewById(R.id.spinner_task_state);

                String compareValue = hashMapString.get("etat");
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.task_state, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                taskState.setAdapter(adapter);
                if (compareValue != null) {
                    int spinnerPosition = adapter.getPosition(compareValue);
                    taskState.setSelection(spinnerPosition);
                }

                SeekBar taskProgress = (SeekBar) findViewById(R.id.task_progress);
                taskProgress.setProgress(hashMapInt.get("progress"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
