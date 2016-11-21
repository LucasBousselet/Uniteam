package unidev.uniteam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

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
                Intent intentKanban = new Intent(ProjectPresentation.this, Kanban.class);
                intentKanban.putExtra("ProjectID", projectID);
                startActivity(intentKanban);
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
            // TODO Put the correct URL complement
            JSONObject jsonObject = DatabaseConnect.SelectFromDB("GetProjectDetails");

            // On récupère le tableau d'objets qui nous concerne
            JSONArray array;
            if (jsonObject != null) {
                array = new JSONArray(jsonObject.getString("projet"));

                TextView projetDescriptionView = (TextView) findViewById(R.id.project_presentation_description);
                // Pour tous les objets on récupère les infos
                for (int i = 0; i < array.length(); i++) {
                    // On récupère un objet JSON du tableau
                    JSONObject obj = new JSONObject(array.getString(i));
                    setTitle(obj.getString("nom"));

                    projetDescriptionView.setText(obj.getString("description"));
                }

                projetDescriptionView.invalidate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
