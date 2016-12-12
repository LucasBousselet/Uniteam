package unidev.uniteam;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

class AlarmTask implements Runnable {
    // The date selected for the alarm
    private final Calendar date;
    // The android system alarm manager
    private final AlarmManager am;
    // Your context to retrieve the alarm manager from
    private final Context context;

    private String notificationText;

    AlarmTask(Context context, Calendar date, String s) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;
        notificationText = s;
    }

    @Override
    public void run() {
        // Request to start are service when the alarm date is upon us
        // We don't start an activity as we just want to pop up a notification into the system bar not a full activity
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        intent.putExtra(NotifyService.NOTIFICATION_TEXT, notificationText);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
        am.set(AlarmManager.RTC, date.getTimeInMillis(), pendingIntent);
    }
}
