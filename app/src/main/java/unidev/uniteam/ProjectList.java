package unidev.uniteam;

import android.app.ProgressDialog;
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

public class ProjectList extends AppCompatActivity implements DatabaseGet.OnJsonTransmissionCompleted {

    private List<Integer> projectID = new ArrayList<>();
    private List<Map<String, String>> projectList = new ArrayList<>();
    private SimpleAdapter adapterProjectListView = null;
    private String facebookUserID;

    protected void onResume() {
        super.onResume();

        // TODO comment when using database
        projectList.clear();
        Map<String, String> am1 = new HashMap<>(2);
        am1.put("name", "Un projet");
        am1.put("description", "Ceci est un test");
        projectList.add(am1);
        Map<String, String> am2 = new HashMap<>(2);
        am2.put("name", "Un autre projet");
        am2.put("description", "Ah ben non en fait");
        projectList.add(am2);
        adapterProjectListView.notifyDataSetChanged();

        // TODO uncomment when using database
        ProgressDialog loading = ProgressDialog.show(ProjectList.this, "Please Wait...", null, true, true);
        RefreshProjectList("projets");
        loading.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        if (savedInstanceState != null) {
            // TODO uncomment when using database
            //facebookUserID = savedInstanceState.getString("CurrentUser");
        }
        else {
            Intent intent = getIntent();
            // TODO uncomment when using database
            //facebookUserID = intent.getStringExtra("facebookUserID");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView projectListView = (ListView) findViewById(R.id.project_list);

        adapterProjectListView = new SimpleAdapter(this, projectList,
                android.R.layout.simple_list_item_2,
                new String[]{"name", "description"},
                new int[]{android.R.id.text1,
                        android.R.id.text2});

        projectListView.setAdapter(adapterProjectListView);

        // Floating action button used to launch a new activity to add a new project
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabProject);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProjectAdd = new Intent(ProjectList.this, ProjectAddNew.class);
                startActivity(ProjectAdd);
            }
        });

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
            //RefreshProjectList("projets");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.getString("CurrentUser") != null) {
            savedInstanceState.remove("CurrentUser");
        }
        savedInstanceState.putString("CurrentUser", facebookUserID);
    }

    private void RefreshProjectList(String url) {
        /* In case not working tomorrow
        class GetJSON extends AsyncTask<String, Void, String> {
            private ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProjectList.this, "Please Wait...", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                uri = "http://172.16.24.150/api/v1/" + uri;
                BufferedReader bufferedReader = null;

                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    String encoded = Base64.encodeToString(("unidev:unidev").getBytes("UTF-8"), Base64.NO_WRAP);
                    con.setRequestProperty("Authorization", "Basic " + encoded);

                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json);
                    }

                    return sb.toString().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONArray array = new JSONArray(s);

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

                    loading.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        */

        DatabaseGet gj = new DatabaseGet(this);
        gj.execute(url, "projets");
    }

    public void onTransmissionCompleted(DatabaseGetResult result) {
        // Clear old projectList
        projectList.clear();
        projectID.clear();

        JSONArray jsonArray = result.getData();

        try {
            // Pour tous les objets on récupère les infos
            for (int i = 0; i < jsonArray.length(); i++) {
                // On récupère un objet JSON du tableau
                JSONObject obj = new JSONObject(jsonArray.getString(i));
                // On fait le lien Projet - Objet JSON
                Map<String, String> am1 = new HashMap<>(2);
                am1.put("name", obj.getString("nom"));
                am1.put("description", obj.getString("description"));
                projectList.add(am1);
                projectID.add(obj.getInt("id"));
            }

            // Refresh ListView
            adapterProjectListView.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}