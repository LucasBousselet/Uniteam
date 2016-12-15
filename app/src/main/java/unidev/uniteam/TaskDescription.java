package unidev.uniteam;

import android.app.ProgressDialog;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TaskDescription extends AppCompatActivity implements DatabaseGet.OnJsonTransmissionCompleted {

    private String CurrentTaskID;
    private Map<String, String> hashMapString = new HashMap<>(3);
    private Map<String, Integer> hashMapInt = new HashMap<>(3);
    private Calendar deadline = Calendar.getInstance();

    protected void onResume() {
        super.onResume();
        // TODO Uncomment when using DB
        //RefreshMeetingsDescription("taches/" + CurrentTaskID);
        findViewById(R.id.parentTask).invalidate();

        TextView taskDescription = (TextView) findViewById(R.id.task_description_text);
        taskDescription.setText("Finir le projet de développement mobile");

        String myFormat = "EEE, d MMM yyyy HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        TextView taskDeadline = (TextView) findViewById(R.id.task_deadline);
        taskDeadline.setText(sdf.format(deadline.getTime()));

        TextView taskDuration = (TextView) findViewById(R.id.task_duration);
        taskDuration.setText("Des années");

        Spinner taskState = (Spinner) findViewById(R.id.spinner_task_state);
        taskState.setSelection(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_description);

        if (savedInstanceState != null) {
            // TODO uncomment when using database
            //CurrentTaskID = savedInstanceState.getString("CurrentTaskID");
        } else {
            Intent intent = getIntent();
            // TODO uncomment when using database
            //CurrentTaskID = getIntent().getStringExtra("CurrentTaskID");
        }

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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.getString("currentProjectID") != null) {
            savedInstanceState.remove("currentProjectID");
        }
        savedInstanceState.putString("currentProjectID", CurrentTaskID);
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
                /*
                try {
                    Spinner taskState = (Spinner) findViewById(R.id.spinner_task_state);
                    SeekBar taskProgress = (SeekBar) findViewById(R.id.task_progress);

                    JSONObject newTaskProgress = new JSONObject();

                    newTaskProgress.put("etat", taskState.getSelectedItem().toString());
                    newTaskProgress.put("avancement", taskProgress.getProgress());

                    // TODO Uncomment when update are implemented
                    ProgressDialog loading = ProgressDialog.show(TaskDescription.this, "Please Wait...", null, true, true);
                    //UpdateTaskProgress("utilisateurs", newTaskProgress);
                    loading.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                */
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void RefreshTaskDescription(String url) {
        DatabaseGet gj = new DatabaseGet(this);
        gj.execute(url);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // SECTION KEPT JUST IN CASE

    /*
    public void RefreshMeetingsDescription() {
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
    */

    private void UpdateTaskProgress(String url, JSONObject jsonObject) {
        /*
        DatabasePost ddbPost = new DatabasePost();
        ddbPost.execute(url, jsonObject.toString());
        */
    }

}
