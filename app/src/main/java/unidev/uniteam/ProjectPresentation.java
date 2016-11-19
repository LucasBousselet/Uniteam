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

public class ProjectPresentation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_presentation);

        // We retrieve the intent used to create the activity (from the ProjectList page)
        Intent intent = getIntent();
        /*
        We fetch the name of the project that has been clicked previously, and pass through the intent
        "ProjectName" is the name we gave the data when using the "putExtra" instruction
         * We fetch the name of the Project that has been clicked previously, and pass through the intent
         * *"ProjectName" is the name we gave the projectList when using the "putExtra" instruction
        */
        String clickedProjectName = intent.getStringExtra("ProjectName");
        // Then we retrieve the correct TextView and we set the new text inside it
        TextView projectName = (TextView) findViewById(R.id.project_presentation_name);
        projectName.setText(clickedProjectName);

        String clickedProjectDescription = intent.getStringExtra("ProjectDescription");
        TextView projectDescription = (TextView) findViewById(R.id.project_presentation_description);
        projectDescription.setText(clickedProjectDescription);

        ImageButton taskButton = (ImageButton) findViewById(R.id.buttonProjectTasks);
        taskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProjectKanban = new Intent(ProjectPresentation.this, TaskKanban.class);
                startActivity(ProjectKanban);
            }
        });

<<<<<<< HEAD
        //Test pour accéder a l'activite de description de taches, en attendant la bdd
        Button boutonTest = (Button) findViewById(R.id.testButton);
        boutonTest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent toto = new Intent(ProjectPresentation.this, TaskDescription.class);
                startActivity(toto);
            }
        });
=======
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
>>>>>>> e5df37a676f58306e7f236303650e325979897bc
    }
}
