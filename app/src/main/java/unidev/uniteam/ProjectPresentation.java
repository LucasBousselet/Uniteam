package unidev.uniteam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProjectPresentation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_presentation);

        // We retrieve the intent used to create the activity (from the ProjectList page)
        Intent intent = getIntent();
        /*
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
}
