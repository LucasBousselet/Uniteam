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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MeetingAddNew extends AppCompatActivity {

    private EditText MeetingDateField;
    private Calendar meetingDate = Calendar.getInstance();
    private ScheduleClient scheduleClient;

    @Override
    protected void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system
        if (scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_add_new);

        MeetingDateField = (EditText) findViewById(R.id.new_meeting_date);

        MeetingDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                new DatePickerDialog(MeetingAddNew.this, date, meetingDate
                        .get(Calendar.YEAR), meetingDate.get(Calendar.MONTH),
                        meetingDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();
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

                EditText meetingSubjectField = (EditText) findViewById(R.id.new_meeting_subject);
                EditText meetingDescriptionField = (EditText) findViewById(R.id.new_meeting_description);
                EditText meetingDateField = (EditText) findViewById(R.id.new_meeting_date);
                EditText meetingPlaceField = (EditText) findViewById(R.id.new_meeting_place);
                CheckBox meetingReminder = (CheckBox) findViewById(R.id.add_to_reminder);

                if (!meetingSubjectField.getText().toString().matches("") &&
                        !meetingDescriptionField.getText().toString().matches("") &&
                        !meetingDateField.getText().toString().matches("") &&
                        !meetingPlaceField.getText().toString().matches("")) {

                    // TODO Uncomment when using database
                    try {
                        JSONObject newMeeting = new JSONObject();

                        newMeeting.put("sujet", meetingSubjectField.getText().toString());
                        newMeeting.put("description", meetingDescriptionField.getText().toString());
                        newMeeting.put("date", meetingDateField.getText().toString());

                        if (meetingReminder.isChecked()) {
                            EditText timeOffsetField = (EditText) findViewById(R.id.offset_time);
                            int timeOffset = Integer.parseInt(timeOffsetField.getText().toString());
                            meetingDate.set(Calendar.MINUTE, meetingDate.get(Calendar.MINUTE) - (timeOffset + 1));
                            scheduleClient.setAlarmForNotification(meetingDate, meetingSubjectField.getText().toString());
                        }

                        // TODO Put the correct URL complement
                        ProgressDialog loading = ProgressDialog.show(MeetingAddNew.this, "Please Wait...", null, true, true);
                        CreateNewMeeting("reunions", newMeeting);
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
            meetingDate.set(Calendar.YEAR, year);
            meetingDate.set(Calendar.MONTH, monthOfYear);
            meetingDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            new TimePickerDialog(MeetingAddNew.this, time, meetingDate
                    .get(Calendar.HOUR_OF_DAY), meetingDate.get(Calendar.MINUTE), true).show();
        }
    };

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            meetingDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            meetingDate.set(Calendar.MINUTE, minutes);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "EEE, d MMM yyyy HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        MeetingDateField.setText(sdf.format(meetingDate.getTime()));
    }

    private void CreateNewMeeting(String url, JSONObject jsonObject) {
        DatabasePost ddbPost = new DatabasePost();
        ddbPost.execute(url, jsonObject.toString());
    }
}
