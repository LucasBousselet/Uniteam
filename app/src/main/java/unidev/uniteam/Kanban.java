package unidev.uniteam;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Kanban extends AppCompatActivity implements DatabaseGet.OnJsonTransmissionCompleted {

    private List<Integer> TaskListID = new ArrayList<>();
    private List<String> TaskListName = new ArrayList<>();
    private int projectID;

    protected void onResume() {
        super.onResume();
        // TODO Uncomment when using DB
        getTasks("projet/" + projectID + "/taches");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanban);

        Intent intent = getIntent();
        //projectID = intent.getIntExtra("ProjectID", -1);

        getProjectsName("projets/" + projectID);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent taskAddNew = new Intent(Kanban.this, TaskAddNew.class);
                startActivity(taskAddNew);
            }
        });

        // TODO Remove when using BdD
        Button BoutonTest = (Button) findViewById(R.id.button2);
        BoutonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinearLayout layout = new LinearLayout(Kanban.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutparams.setMargins(0, 10, 0, 10);
                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (10 * scale + 0.5f);
                layout.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
                layout.setLayoutParams(layoutparams);
                //use a GradientDrawable with only one color set, to make it a solid color
                GradientDrawable border = new GradientDrawable();
                border.setColor(0xFFFAFAFA); //white background
                border.setStroke(5, ContextCompat.getColor(Kanban.this, R.color.uniteamGreen));
                layout.setBackground(border);

                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent taskDescription = new Intent(Kanban.this, TaskDescription.class);
                        startActivity(taskDescription);
                    }
                });

                TextView titleView1 = new TextView(Kanban.this);
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                titleView1.setLayoutParams(lparams);
                titleView1.setText("task title");
                layout.addView(titleView1);

                TextView titleView2 = new TextView(Kanban.this);
                titleView2.setLayoutParams(lparams);
                titleView2.setText("task deadlineField");
                layout.addView(titleView2);

                LinearLayout content = (LinearLayout) findViewById(R.id.task_kanban_doing);
                content.addView(layout);
            }
        });
        /***********/
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
            Intent AboutPage = new Intent(Kanban.this, AboutUs.class);
            startActivity(AboutPage);
        } else if (item.getItemId() == R.id.refresh) {

            // TODO Uncomment when using DB
            getTasks("taches");

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getProjectsName(String url) {
        DatabaseGet gj = new DatabaseGet(this);
        gj.execute(url, "projets");
    }

    public void getTasks(String url) {
        DatabaseGet gj = new DatabaseGet(this);
        gj.execute(url, "taches");
    }

    public void onTransmissionCompleted(DatabaseGetResult result) {
        try {
            JSONArray jsonArray = result.getData();

            if(result.getReturnedType().equals("projets")){
                for (int i = 0; i < jsonArray.length(); i++) {
                    // On récupère un objet JSON du tableau
                    JSONObject obj = new JSONObject(jsonArray.getString(i));
                    setTitle(obj.getString("nom"));
                }
            }
            if(result.getReturnedType().equals("taches")){

                LinearLayout todoLayout = (LinearLayout) findViewById(R.id.task_kanban_todo);
                LinearLayout doingLayout = (LinearLayout) findViewById(R.id.task_kanban_doing);
                LinearLayout doneLayout = (LinearLayout) findViewById(R.id.task_kanban_done);

                // Clear old TaskListString
                TaskListID.clear();
                TaskListName.clear();
                // Pour tous les objets on récupère les infos
                for (int i = 0; i < jsonArray.length(); i++) {
                    // On récupère un objet JSON du tableau
                    JSONObject obj = new JSONObject(jsonArray.getString(i));
                    // On fait le lien Task - Objet JSON
                    final int id = obj.getInt("id");
                    TaskListID.add(id);
                    String name = obj.getString("nom");
                    TaskListName.add(name);

                    String dateStr = obj.getString("deadlineField");
                    Calendar deadline = Calendar.getInstance();
                    String ackwardRipOff = dateStr.replace("/Date(", "").replace(")/", "");
                    Long timeInMillis = Long.valueOf(ackwardRipOff);
                    deadline.setTimeInMillis(timeInMillis);

                    // Create a new linear layout to hold each task
                    LinearLayout taskLayout = new LinearLayout(Kanban.this);
                    taskLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams.setMargins(0, 10, 0, 10);
                    float padding = 10;
                    int dpAsPixels = (int) (padding * getResources().getDisplayMetrics().density + 0.5f);
                    taskLayout.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
                    taskLayout.setLayoutParams(layoutparams);
                    // use a GradientDrawable with only one color set, to make it a solid color
                    GradientDrawable border = new GradientDrawable();
                    border.setColor(0xFFFAFAFA);
                    border.setStroke(5, ContextCompat.getColor(Kanban.this, R.color.uniteamGreen));
                    taskLayout.setBackground(border);

                    // Set onClickListener for the layout
                    taskLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent taskDescription = new Intent(Kanban.this, TaskDescription.class);
                            taskDescription.putExtra("taskID", id);
                            startActivity(taskDescription);
                        }
                    });

                    TextView nameField = new TextView(Kanban.this);
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    nameField.setLayoutParams(lparams);
                    nameField.setText(name);
                    taskLayout.addView(nameField);

                    TextView deadlineField = new TextView(Kanban.this);
                    deadlineField.setLayoutParams(lparams);

                    String myFormat = "dd/MM/yyyy HH:mm";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                    deadlineField.setText(sdf.format(deadline.getTime()));
                    taskLayout.addView(deadlineField);

                    switch (obj.getString("etat")) {
                        case "TODO":
                            todoLayout.addView(taskLayout);
                        case "DOING":
                            doingLayout.addView(taskLayout);
                        case "DONE":
                            doneLayout.addView(taskLayout);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    public void onTransmissionCompletedForTasks(JSONArray jsonArray) {
        try {
            LinearLayout todoLayout = (LinearLayout) findViewById(R.id.task_kanban_todo);
            LinearLayout doingLayout = (LinearLayout) findViewById(R.id.task_kanban_doing);
            LinearLayout doneLayout = (LinearLayout) findViewById(R.id.task_kanban_done);

            // Clear old TaskListString
            TaskListID.clear();
            TaskListName.clear();
            // Pour tous les objets on récupère les infos
            for (int i = 0; i < jsonArray.length(); i++) {
                // On récupère un objet JSON du tableau
                JSONObject obj = new JSONObject(jsonArray.getString(i));
                // On fait le lien Task - Objet JSON
                final int id = obj.getInt("id");
                TaskListID.add(id);
                String name = obj.getString("nom");
                TaskListName.add(name);

                String dateStr = obj.getString("deadlineField");
                Calendar deadline = Calendar.getInstance();
                String ackwardRipOff = dateStr.replace("/Date(", "").replace(")/", "");
                Long timeInMillis = Long.valueOf(ackwardRipOff);
                deadline.setTimeInMillis(timeInMillis);

                // Create a new linear layout to hold each task
                LinearLayout taskLayout = new LinearLayout(Kanban.this);
                taskLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutparams.setMargins(0, 10, 0, 10);
                float padding = 10;
                int dpAsPixels = (int) (padding * getResources().getDisplayMetrics().density + 0.5f);
                taskLayout.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
                taskLayout.setLayoutParams(layoutparams);
                // use a GradientDrawable with only one color set, to make it a solid color
                GradientDrawable border = new GradientDrawable();
                border.setColor(0xFFFAFAFA);
                border.setStroke(5, ContextCompat.getColor(Kanban.this, R.color.uniteamGreen));
                taskLayout.setBackground(border);

                // Set onClickListener for the layout
                taskLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent taskDescription = new Intent(Kanban.this, TaskDescription.class);
                        taskDescription.putExtra("taskID", id);
                        startActivity(taskDescription);
                    }
                });

                TextView nameField = new TextView(Kanban.this);
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                nameField.setLayoutParams(lparams);
                nameField.setText(name);
                taskLayout.addView(nameField);

                TextView deadlineField = new TextView(Kanban.this);
                deadlineField.setLayoutParams(lparams);

                String myFormat = "dd/MM/yyyy HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                deadlineField.setText(sdf.format(deadline.getTime()));
                taskLayout.addView(deadlineField);

                switch (obj.getString("etat")) {
                    case "TODO":
                        todoLayout.addView(taskLayout);
                    case "DOING":
                        doingLayout.addView(taskLayout);
                    case "DONE":
                        doneLayout.addView(taskLayout);
                }
            }

            todoLayout.invalidate();
            doingLayout.invalidate();
            doneLayout.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    /*
    public void RefreshProjectDetails() {
        try {
            // TODO Put the correct URL complement
            JSONObject jsonObject = DatabaseConnect.SelectFromDB("getTasks");

            // On récupère le tableau d'objets qui nous concerne
            JSONArray array;
            if (jsonObject != null) {
                array = new JSONArray(jsonObject.getString("tache"));

                LinearLayout todoLayout = (LinearLayout) findViewById(R.id.task_kanban_todo);
                LinearLayout doingLayout = (LinearLayout) findViewById(R.id.task_kanban_doing);
                LinearLayout doneLayout = (LinearLayout) findViewById(R.id.task_kanban_done);

                // Clear old TaskListString
                TaskListID.clear();
                TaskListName.clear();
                // Pour tous les objets on récupère les infos
                for (int i = 0; i < array.length(); i++) {
                    // On récupère un objet JSON du tableau
                    JSONObject obj = new JSONObject(array.getString(i));
                    // On fait le lien Task - Objet JSON
                    final int id = obj.getInt("id");
                    TaskListID.add(id);
                    String name = obj.getString("nom");
                    TaskListName.add(name);

                    String dateStr = obj.getString("deadlineField");
                    Calendar deadline = Calendar.getInstance();
                    String ackwardRipOff = dateStr.replace("/Date(", "").replace(")/", "");
                    Long timeInMillis = Long.valueOf(ackwardRipOff);
                    deadline.setTimeInMillis(timeInMillis);

                    // Create a new linear layout to hold each task
                    LinearLayout taskLayout = new LinearLayout(Kanban.this);
                    taskLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutparams.setMargins(0, 10, 0, 10);
                    float padding = 10;
                    int dpAsPixels = (int) (padding * getResources().getDisplayMetrics().density + 0.5f);
                    taskLayout.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
                    taskLayout.setLayoutParams(layoutparams);
                    // use a GradientDrawable with only one color set, to make it a solid color
                    GradientDrawable border = new GradientDrawable();
                    border.setColor(0xFFFAFAFA);
                    border.setStroke(5, ContextCompat.getColor(Kanban.this, R.color.uniteamGreen));
                    taskLayout.setBackground(border);

                    // Set onClickListener for the layout
                    taskLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent taskDescription = new Intent(Kanban.this, TaskDescription.class);
                            taskDescription.putExtra("taskID", id);
                            startActivity(taskDescription);
                        }
                    });

                    TextView nameField = new TextView(Kanban.this);
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    nameField.setLayoutParams(lparams);
                    nameField.setText(name);
                    taskLayout.addView(nameField);

                    TextView deadlineField = new TextView(Kanban.this);
                    deadlineField.setLayoutParams(lparams);

                    String myFormat = "dd/MM/yyyy HH:mm";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                    deadlineField.setText(sdf.format(deadline.getTime()));
                    taskLayout.addView(deadlineField);

                    switch (obj.getString("etat")) {
                        case "TODO":
                            todoLayout.addView(taskLayout);
                        case "DOING":
                            doingLayout.addView(taskLayout);
                        case "DONE":
                            doneLayout.addView(taskLayout);
                    }
                }

                //todoLayout.invalidate();
                //doingLayout.invalidate();
                //doneLayout.invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

}