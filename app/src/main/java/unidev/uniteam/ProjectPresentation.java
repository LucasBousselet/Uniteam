package unidev.uniteam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ProjectPresentation extends AppCompatActivity {

    private int projectID;

    protected void onResume(){
        super.onResume();
        // TODO Uncomment when using database
        //GetProjectDetails();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_project_presentation);
        // TODO Uncomment when using DB
        //projectID = getIntent().getIntExtra("ProjectID", -1);

        ImageButton taskButton = (ImageButton) findViewById(R.id.buttonProjectTasks);
        taskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentKanban = new Intent(ProjectPresentation.this, TaskKanban.class);
                intentKanban.putExtra("ProjectID", projectID);
                startActivity(intentKanban);
            }
        });

        // Test pour acceder a l'activite de description de taches, en attendant la bdd
        Button boutonTest = (Button) findViewById(R.id.testButton);
        boutonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent taskKanban = new Intent(ProjectPresentation.this, TaskDescription.class);
                startActivity(taskKanban);
            }
        });

        ImageButton meetingsButton = (ImageButton) findViewById(R.id.buttonProjectMeetings);
        meetingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.coming_soon),
                        Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton participantsButton = (ImageButton) findViewById(R.id.buttonProjectParticipants);
        participantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.coming_soon),
                        Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton reportsButton = (ImageButton) findViewById(R.id.buttonProjectReports);
        reportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.coming_soon),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_about) {
            Intent AboutPage = new Intent(ProjectPresentation.this, AboutUs.class);
            startActivity(AboutPage);
        }
        return super.onOptionsItemSelected(item);
    }

    public void GetProjectDetails() {
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

            TextView projetDescriptionView = (TextView) findViewById(R.id.project_presentation_description);
            // Pour tous les objets on récupère les infos
            for (int i = 0; i < array.length(); i++) {
                // On récupère un objet JSON du tableau
                JSONObject obj = new JSONObject(array.getString(i));
                setTitle(obj.getString("nom"));

                projetDescriptionView.setText(obj.getString("description"));
            }

            projetDescriptionView.invalidate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
