package unidev.uniteam;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class ProjectAddNew extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_add_new);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_validate, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.validate:

                EditText projectNameField = (EditText) findViewById(R.id.project_name);
                EditText projectDescriptionField = (EditText) findViewById(R.id.project_description);

                if (!projectNameField.getText().toString().matches("") &&
                        !projectDescriptionField.getText().toString().matches("")) {
                    // TODO Uncomment when using database
                    /*
                    try {
                        JSONObject newProject = new JSONObject();

                        newProject.put("nom", projectNameField.getText().toString());
                        newProject.put("description", projectDescriptionField.getText().toString());

                        ProgressDialog loading = ProgressDialog.show(ProjectList.this, "Please Wait...", null, true, true);
                        CreateNewProject("projets", newProject);
                        loading.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    */
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.input_error),
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CreateNewProject(String url, JSONObject jsonObject) {
        DatabasePost ddbPost = new DatabasePost();
        ddbPost.execute(url, jsonObject.toString());
    }
}
