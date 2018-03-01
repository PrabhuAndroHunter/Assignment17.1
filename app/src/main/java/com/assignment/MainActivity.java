package com.assignment;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.toString();
    private Button mPlayBtn, mStopBtn;
    private ImageView mTrackThumb;
    NotificationManager mNotificationManager;
    private PlayerService playerService;
    private final int NOTIFICATION_ID = 17;

    public ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            playerService = ((PlayerService.ServiceBinder) service).getService(); // get playerService instance
            if (playerService.isPlaying()) {  // check is playing
                mTrackThumb.setImageResource(R.drawable.ic_music);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            playerService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init layout file
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        // init play and stop button
        mPlayBtn = (Button) findViewById(R.id.button_play);
        mStopBtn = (Button) findViewById(R.id.button_stop);
        mTrackThumb = (ImageView) findViewById(R.id.track_thumb);
        final Intent intent = new Intent(MainActivity.this, PlayerService.class);
        startService(intent);
        // set on click listener on play button
        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: start service");
//                startService(intent); // start PlayerService
                playerService.startPlay();
                mTrackThumb.setImageResource(R.drawable.ic_music);
                showNotification();  // show user notification
            }
        });

        // set on click listener on stop button
        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: stop service");
//                stopService(intent); // Stop PlayerService
                playerService.stopPlay();
                mTrackThumb.setImageResource(R.drawable.ic_music_stop);
                cancleNotification();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, PlayerService.class);
        this.bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unbindService(connection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intentStopService = new Intent(this, PlayerService.class);
        stopService(intentStopService);
    }

    private void showNotification() {
        // create notification on start playing
        Bitmap icon1 = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_music);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this, "assignment").setAutoCancel(false)
                .setContentTitle("Music Started")
                .setSmallIcon(R.drawable.ic_music).setLargeIcon(icon1)
                .setContentText("Assignment 17.1");
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

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // NOTIFICATION_ID allows to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void cancleNotification() {
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

}
