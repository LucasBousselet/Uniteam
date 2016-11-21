package unidev.uniteam;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class ProjectList extends AppCompatActivity {

    private List<Integer> projectID = new ArrayList<>();
    private List<Map<String, String>> projectList = new ArrayList<>();
    private SimpleAdapter adapterProjectListView = null;

    protected void onResume() {
        super.onResume();

        // TODO uncomment when using database
        //RefreshProjectList();

        // TODO comment when using database
        projectList.clear();
        Map<String, String> am1 = new HashMap<>(2);
        am1.put("name", "Test 1");
        am1.put("description", "Sub test 1");
        projectList.add(am1);
        Map<String, String> am2 = new HashMap<>(2);
        am2.put("name", "Test 2");
        am2.put("description", "Sub test 2");
        projectList.add(am2);
        adapterProjectListView.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView projectListView = (ListView) findViewById(R.id.project_list);

        adapterProjectListView = new SimpleAdapter(this, projectList,
                android.R.layout.simple_list_item_2,
                new String[]{"name", "description"},
                new int[]{android.R.id.text1,
                        android.R.id.text2});

        projectListView.setAdapter(adapterProjectListView);

        /*
         * Floating action button used to launch a new activity to add a new project
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProjectAdd = new Intent(ProjectList.this, ProjectAddNew.class);
                startActivity(ProjectAdd);
            }
        });

        /*
         * Defines the list of Project as an OnItemCLickListener
         * So that we can call a event when an item of the list is clicked
         */
        projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                // Intent used to start the activity ProjectPresentation
                Intent intent = new Intent(ProjectList.this, ProjectPresentation.class);
                parent.getItemAtPosition(position);

                // TODO Uncomment when using DB
                //intent.putExtra("projectID", projectID.get(position));
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
            Intent AboutPage = new Intent(ProjectList.this, AboutUs.class);
            startActivity(AboutPage);
        } else if (item.getItemId() == R.id.refresh) {
            // TODO Uncomment when using database
            //RefreshProjectList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void RefreshProjectList() {
        try {
            // TODO Put the correct URL complement
            JSONObject jsonObject = DatabaseConnect.SelectFromDB("GetProjects");
            // On récupère le tableau d'objets qui nous concerne
            JSONArray array;
            if (jsonObject != null) {
                array = new JSONArray(jsonObject.getString("projet"));

                // Clear old projectList
                projectList.clear();
                projectID.clear();

                // Pour tous les objets on récupère les infos
                for (int i = 0; i < array.length(); i++) {
                    // On récupère un objet JSON du tableau
                    JSONObject obj = new JSONObject(array.getString(i));
                    // On fait le lien Projet - Objet JSON
                    Map<String, String> am1 = new HashMap<>(2);
                    am1.put("name", obj.getString("nom"));
                    am1.put("description", obj.getString("description"));
                    projectList.add(am1);
                    projectID.add(obj.getInt("id"));
                }

                // Refresh ListView
                adapterProjectListView.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
