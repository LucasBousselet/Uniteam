package unidev.uniteam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ProjectAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_add);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_project_add, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.validate:

                // TODO Uncomment when using database
                /*
                try {
                    EditText projectNameField = (EditText) findViewById(R.id.project_name);
                    EditText projectDescriptionField = (EditText) findViewById(R.id.project_description);

                    JSONObject newProject = new JSONObject();

                    newProject.put("nom", projectNameField.getText());
                    newProject.put("description", projectDescriptionField.getText());

                    // TODO put correct database URL
                    String myurl = "http://www.exemple.com/getProjet";

                    URL url = new URL(myurl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
                    wr.write(newProject.toString());
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                */

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
