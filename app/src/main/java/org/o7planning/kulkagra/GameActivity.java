package org.o7planning.kulkagra;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import org.o7planning.kulkagra.GraphicGameEngine;
import org.o7planning.kulkagra.PhysicalGameEngine;
import org.o7planning.kulkagra.Ball;
import org.o7planning.kulkagra.Bloc;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    // ID dialogów
    public static final int VICTORY_DIALOG  = 0;
    public static final int DEFEAT_DIALOG   = 1;
    public static int LEVEL = 2;

    // Definicja wysokości obrazu
    private static final int SCREEN_HEIGHT_RATION = 143;

    // Definicja obiektów gry
    private PhysicalGameEngine mEngine  = null;
    private GraphicGameEngine mView     = null;
    private Ball mBall                  = null;


    // Sensory
    private SensorManager mSensorManager    = null;
    private Sensor mLuminositySensor        = null;
    private Sensor mMagneticSensor          = null;

    // Dzwięki
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Inizjalizacja sensor Managera
        mSensorManager      = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLuminositySensor   = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mMagneticSensor     = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Inizjalizacja graphic game engine
        mView   = new GraphicGameEngine(this);
        mEngine = new PhysicalGameEngine(this);
        setContentView(mView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Zmiana promienia w zależności od wysokości obrazu
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Ekran początkowy

        Ball.RADIUS = (metrics.heightPixels - SCREEN_HEIGHT_RATION) / GraphicGameEngine.SURFACE_RATIO;

        // Inicjalizacja ball
        mBall = new Ball();
        mView.setBall(mBall);
        mEngine.setBall(mBall);

        // Tworzenie labiryntu
        List<Bloc> mList1 = mEngine.buildLabyrinthe1();
        mView.setBlocks(mList1);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setMessage("Podołasz wyzwaniu?")
                .setTitle("Rozpocznij grę")
                .setNeutralButton("START", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEngine.reset();
                        mEngine.resume();
                    }
                });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Wznowienie gry
       // mEngine.resume();

        // Rejerstracja Listener'a
        mSensorManager.registerListener(this, mLuminositySensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    public void showInfoDialog(int id) {


        // Pokaż powiadomienie kiedy event zostanie uruchomiony
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        int soundToPlay = R.raw.walking;
        mEngine.stop();


        switch(id) {
            case VICTORY_DIALOG:
                if(LEVEL == 4) {
                    builder.setCancelable(false)
                            .setMessage("Jesteś zwycięzcą!")
                            .setTitle("O to nagroda ;)");
                    builder.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run(){
                            Intent i = new Intent(GameActivity.this, FinishActivity.class);
                            startActivity(i);
                            GameActivity.this.onDestroy();
                        }
                    }, 6000);
                    return;
                }

                else{
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }
                        mediaPlayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    soundToPlay = R.raw.win;
                    builder.setCancelable(false)
                            .setMessage(R.string.victory_msg)
                            .setMessage(R.string.victory_title)
                            .setNeutralButton(R.string.next_round, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mEngine.reset();
                                    if (LEVEL == 2) {
                                        List<Bloc> mList2 = mEngine.buildLabyrinthe2();
                                        mView.setBlocks(mList2);
                                    } else if (LEVEL == 3) {
                                        List<Bloc> mList3 = mEngine.buildLabyrinthe3();
                                        mView.setBlocks(mList3);
                                    }

                                    LEVEL++;
                                    mEngine.resume();
                                    try {
                                        if (mediaPlayer.isPlaying()) {
                                            mediaPlayer.stop();
                                        }
                                        mediaPlayer.start();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
                break;
            case DEFEAT_DIALOG:
                try {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    mediaPlayer.start();
                } catch(Exception e) { e.printStackTrace(); }
                soundToPlay = R.raw.loose;
                builder.setCancelable(false)
                        .setMessage(R.string.defeat_msg)
                        .setTitle(R.string.defeat_title)
                        .setNeutralButton(R.string.restart_game, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mEngine.reset();
                                mEngine.resume();
                                try {
                                    if (mediaPlayer.isPlaying()) {
                                        mediaPlayer.stop();
                                    }
                                    mediaPlayer.start();
                                } catch(Exception e) { e.printStackTrace(); }
                            }
                        });
                break;
            default:
                soundToPlay = R.raw.loose;
                break;
        }
        builder.show();
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                float mLuminosity = sensorEvent.values[0];
                mView.setSurfaceBgColor(mLuminosity);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                float xMagnetic = sensorEvent.values[0];
                float yMagnetic = sensorEvent.values[1];
                float zMagnetic = sensorEvent.values[2];
                double mMagnetic = Math.sqrt((double) (xMagnetic * xMagnetic + yMagnetic * yMagnetic + zMagnetic * zMagnetic));
                mBall.setBallColor(mMagnetic);
                break;
            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private double getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }
}


