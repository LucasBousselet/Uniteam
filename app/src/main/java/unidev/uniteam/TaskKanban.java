package unidev.uniteam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskKanban extends AppCompatActivity {

    private List<Integer> TaskListID = new ArrayList<>();
    private List<String> TaskListName = new ArrayList<>();
    private int projectID;

    protected void onResume(){
        super.onResume();
        // TODO Uncomment when using DB
        //GetProjectName();
        //RefreshProjectDetails();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_kanban);

        Intent intent = getIntent();
        projectID = intent.getIntExtra("ProjectID", -1);
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
            Intent AboutPage = new Intent(TaskKanban.this, AboutUs.class);
            startActivity(AboutPage);
        } else if (item.getItemId() == R.id.refresh) {
            // TODO Uncomment when using DB
            //RefreshProjectDetails();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void GetProjectName(){
        try {
            // TODO change with the actual database URL
            String myurl = "http://www.exemple.com/getProjet";

            URL url = new URL(myurl);
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
            JSONObject jsonObject = new JSONObject(result);
            // On récupère le tableau d'objets qui nous concerne
            JSONArray array = new JSONArray(jsonObject.getString("projet"));

            // Pour tous les objets on récupère les infos
            for (int i = 0; i < array.length(); i++) {
                // On récupère un objet JSON du tableau
                JSONObject obj = new JSONObject(array.getString(i));
                setTitle(obj.getString("nom"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void RefreshProjectDetails() {
        try {
            // TODO change with the actual database URL
            String myurl = "http://www.exemple.com/getProjet";

            URL url = new URL(myurl);
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
            JSONObject jsonObject = new JSONObject(result);
            // On récupère le tableau d'objets qui nous concerne
            JSONArray array = new JSONArray(jsonObject.getString("tache"));

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

                Button b = new Button(this);
                b.setText(name);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent taskDescription = new Intent(TaskKanban.this, TaskDescription.class);
                        taskDescription.putExtra("taskID", id);
                        startActivity(taskDescription);
                    }
                });

                switch (obj.getString("etat")) {
                    case "TODO":
                        todoLayout.addView(b);
                    case "DOING":
                        doingLayout.addView(b);
                    case "DONE":
                        doneLayout.addView(b);
                }
            }

            todoLayout.invalidate();
            doingLayout.invalidate();
            doneLayout.invalidate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}