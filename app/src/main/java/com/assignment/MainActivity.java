package com.assignment;

import android.content.Intent;
import android.media.Image;
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

        // set on click listener on play button
        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: start service");
                startService(intent); // start PlayerService
                mTrackThumb.setImageResource(R.drawable.ic_music);
            }
        });

        // set on click listener on stop button
        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: stop service");
                stopService(intent); // Stop PlayerService
                mTrackThumb.setImageResource(R.drawable.ic_music_stop);
            }
        });
    }
}
