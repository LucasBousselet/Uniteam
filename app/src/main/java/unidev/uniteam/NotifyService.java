package unidev.uniteam;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class NotifyService extends Service {

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    // Unique id to identify the notification.
    private static final int NOTIFICATION = 123;
    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "INTENT_NOTIFY";
    // The system notification manager
    private NotificationManager mNM;

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // If this service was started by out AlarmTask intent then we want to show our notification
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification();

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Creates a notification and shows it in the OS drag-down status bar
     */
    private void showNotification() {

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MeetingsList.class), 0);

        Notification.Builder builder = new Notification.Builder(NotifyService.this);

        builder.setAutoCancel(false);
        builder.setContentTitle("@string/notification_title");
        builder.setContentText("@string/notification_content");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(contentIntent);
        builder.setOngoing(true);
        builder.build();

        Notification myNotication = builder.getNotification();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(11, myNotication);

        // Clear the notification when it is pressed
        myNotication.flags |= Notification.FLAG_AUTO_CANCEL;

        // Send the notification to the system.
        mNM.notify(NOTIFICATION, myNotication);

        // Stop the service when we are finished
        stopSelf();
    }
}
