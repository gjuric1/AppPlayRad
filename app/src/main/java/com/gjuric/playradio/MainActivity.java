package com.gjuric.playradio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button btPlay;
    private ImageView ivImage;

    private MediaPlayer mediaPlayer;

    private boolean prepared = false;
    private boolean started = false;

    private String narodni = "http://s7.iqstreaming.com:8034/live.ogg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();

        setListeners();


        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        new PlayerTask().execute(narodni);


    }

    //set button listener
    private void setListeners() {
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (started) {
                    started = false;
                    mediaPlayer.pause();
                    btPlay.setText("Play");
                } else {
                    started = true;
                    mediaPlayer.start();
                    btPlay.setText("Pause");
                }

            }
        });
    }

    //initialize widgets
    private void initWidgets() {
        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivImage.setImageResource(R.drawable.narodni);

        btPlay = (Button) findViewById(R.id.btPlay);
        btPlay.setEnabled(false);
        btPlay.setText("Loading");
    }

    class PlayerTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                mediaPlayer.setDataSource(strings [0]);
                mediaPlayer.prepare();
                prepared = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            btPlay.setEnabled(true);
            btPlay.setText("Play");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (started) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (started) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (prepared) {
            mediaPlayer.release();
        }
    }
}
