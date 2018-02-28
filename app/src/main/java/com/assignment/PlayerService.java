package com.assignment;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * Created by prabhu on 28/2/18.
 */

public class PlayerService extends Service {
    private final String TAG = PlayerService.class.toString();
    NotificationManager mNotificationManager1;
    private MediaPlayer mp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mp = MediaPlayer.create(getApplicationContext(), R.raw.beep); // create player instance
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mp.start();
        mp.setLooping(true);
        Bitmap icon1 = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_music);
        // create notification on start playing
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this, "assignment").setAutoCancel(false)
                .setContentTitle("Music Started")
                .setSmallIcon(R.drawable.ic_music).setLargeIcon(icon1)
                .setContentText("Assignment 17.1");

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.setSummaryText("beep sound");
        mBuilder.setStyle(bigText);

        // Creates an explicit intent for an Activity
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager1 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager1.notify(17, mBuilder.build());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mp.release();
        mNotificationManager1.cancel(17); // cancel notification
        super.onDestroy();
    }
}
