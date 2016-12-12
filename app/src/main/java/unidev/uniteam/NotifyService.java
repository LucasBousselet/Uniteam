package unidev.uniteam;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    public static final String NOTIFICATION_TEXT = "notificationText";
    private NotificationManager manager;
    private String notificationText;

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        notificationText = intent.getExtras().getString(NOTIFICATION_TEXT);

        // If this service was started by out AlarmTask intent then we want to show our notification
        if (intent.getBooleanExtra(INTENT_NOTIFY, false))
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

        builder.setAutoCancel(true);
        builder.setContentTitle(getString(R.string.notification_title));
        builder.setContentText(notificationText);
        builder.setSmallIcon(R.drawable.ic_notification_icon);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        builder.setLargeIcon(bm);
        builder.setContentIntent(contentIntent);
        builder.setOngoing(true);
        builder.build();

        manager.notify(NOTIFICATION, builder.build());

        // Stop the service when we are finished
        stopSelf();
    }
}
