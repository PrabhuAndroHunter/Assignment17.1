package com.assignment;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by prabhu on 28/2/18.
 */

public class PlayerService extends Service {
    private final String TAG = PlayerService.class.toString();
    private MediaPlayer mp;
    private ServiceBinder binder = new ServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mp = MediaPlayer.create(getApplicationContext(), R.raw.beep); // create player instance
    }

    public class ServiceBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    @Override
    public void onDestroy() {
        mp.release();
        mp = null;
        super.onDestroy();
    }

    // this method will start player
    public void startPlay() {
        mp.setLooping(true);
        mp.start();
    }

    // this method will stop playing
    public void stopPlay() {
        if (mp != null) {
            if (mp.isPlaying())
                mp.pause();
        }
    }

    /*
    *
    * this method will return true if player is playing
    * or return false
    *
    * */
    public boolean isPlaying() {
        if (mp != null) {
            if (mp.isPlaying()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
