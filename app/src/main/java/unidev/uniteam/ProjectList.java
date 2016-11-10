package unidev.uniteam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ProjectList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView projectListView = (ListView) findViewById(R.id.project_list);
        final ArrayList<String> projectList = new ArrayList<String>();
        projectList.add("Test1");
        projectList.add("Test2");

        final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, projectList);
        projectListView.setAdapter(listAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProjectAdd = new Intent(ProjectList.this, ProjectAdd.class);
                startActivity(ProjectAdd);

                //projectList.add("test");
                //listAdapter.notifyDataSetChanged();
            }
        });

        /* Defines the list of project as an OnItemCLickListener
        So that we can call a event when an item of the list is clicked
         */
        projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position, long id){
                //ItemClicked item = adapter.getItemAtPosition(position);

                /* Intent used to start the activity ProjectPresentation */
                Intent intent = new Intent(ProjectList.this,ProjectPresentation.class);
                adapter.getItemAtPosition(position);
                String nom = listAdapter.getItem(position);
                intent.putExtra("ProjectName", nom);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
