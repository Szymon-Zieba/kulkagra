package org.o7planning.kulkagra;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.*;
import android.content.Intent;

public class FinishActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    int soundToPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        soundToPlay = R.raw.parostatek;

        mediaPlayer = MediaPlayer.create(this, soundToPlay);

        // Ustaw obsługe odtwarzacza
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });

        // Pauza jeśli już działa lub start jeśli nie
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.start();
        } catch(Exception e) { e.printStackTrace(); }


    }

}
