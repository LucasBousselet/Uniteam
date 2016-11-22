package unidev.uniteam;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TaskAddNew extends AppCompatActivity {

    private EditText deadlineField;
    private Calendar deadline = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add_new);

        deadlineField = (EditText) findViewById(R.id.new_task_deadline);

        deadlineField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                new DatePickerDialog(TaskAddNew.this, date, deadline
                        .get(Calendar.YEAR), deadline.get(Calendar.MONTH),
                        deadline.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.new_spinner_task_state);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.task_state, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
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

                EditText taskName = (EditText) findViewById(R.id.new_task_name);
                EditText taskDescription = (EditText) findViewById(R.id.new_task_description_text);
                EditText taskDuration = (EditText) findViewById(R.id.new_task_duration);
                EditText taskDeadline = (EditText) findViewById(R.id.new_task_deadline);
                Spinner taskState = (Spinner) findViewById(R.id.new_spinner_task_state);
                SeekBar taskProgress = (SeekBar) findViewById(R.id.new_task_progress);


                if (!taskName.getText().toString().matches("") &&
                        !taskDescription.getText().toString().matches("") &&
                        !taskDuration.getText().toString().matches("") &&
                        !taskDeadline.getText().toString().matches("")) {

                    // TODO Uncomment when using database

                    try {
                        JSONObject newTaskProgress = new JSONObject();

                        newTaskProgress.put("nom", taskName.getText().toString());
                        newTaskProgress.put("description", taskDescription.getText().toString());
                        newTaskProgress.put("etat", taskState.getSelectedItem().toString());
                        newTaskProgress.put("avancement", taskProgress.getProgress());

                        // TODO uncomment when implemented in DB
                        newTaskProgress.put("deadline", deadline.getTimeInMillis());
                        //newTaskProgress.put("duree", taskDuration.getText().toString());

                        // TODO Put the correct URL complement
                        ProgressDialog loading = ProgressDialog.show(TaskAddNew.this, "Please Wait...", null, true, true);
                        CreateNewTask("utilisateurs", newTaskProgress);
                        loading.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            deadline.set(Calendar.YEAR, year);
            deadline.set(Calendar.MONTH, monthOfYear);
            deadline.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            new TimePickerDialog(TaskAddNew.this, time, deadline
                    .get(Calendar.HOUR_OF_DAY), deadline.get(Calendar.MINUTE), true).show();
        }
    };

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            deadline.set(Calendar.HOUR_OF_DAY, hourOfDay);
            deadline.set(Calendar.MINUTE, minutes);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "EEE, d MMM yyyy HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        deadlineField.setText(sdf.format(deadline.getTime()));
    }

    private void CreateNewTask(String url, JSONObject jsonObject) {
        DatabasePost ddbPost = new DatabasePost();
        ddbPost.execute(url, jsonObject.toString());
    }

}
