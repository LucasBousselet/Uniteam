package unidev.uniteam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProjectPresentation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_presentation);

        // We retrieve the intent used to create the activity (from the ProjectList page)
        Intent intent = getIntent();
        /* We fetch the name of the project that has been clicked previously, and pass through the intent
        "ProjectName" is the name we gave the data when using the "putExtra" instruction */
        String clickedProjectName = intent.getStringExtra("ProjectName");
        // Then we retrieve the correct TextView and we set the new text inside it
        TextView projectName = (TextView) findViewById(R.id.project_presentation_name);
        projectName.setText(clickedProjectName);
    }
}
