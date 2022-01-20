package org.o7planning.kulkagra;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.o7planning.kulkagra.GameActivity;
import org.o7planning.kulkagra.Ball;
import org.o7planning.kulkagra.Bloc;
import org.o7planning.kulkagra.Bloc.Type;

public class PhysicalGameEngine implements SensorEventListener {

    private Ball mBall              = null;
    private List<Bloc> mBlocks      = null;
    private GameActivity mActivity  = null;

    // Sensor init
    private SensorManager mManager  = null;
    private Sensor mAccelerometer   = null;

    // Acceleration helpers vars
    private final int ACC_RATE          = 50;
    private final double ACC_LIMIT      = 0.3;
    private int accCount                = 0;
    private double accSum               = 0.0f;
    private double accResult            = 0.0f;
    private double mAcceleration        = 0.0f;
    private double mAccelerationCurrent = SensorManager.GRAVITY_EARTH;
    private double mAccelerationLast    = SensorManager.GRAVITY_EARTH;

    /**
     * Constructor of PhysicalGameEngine class
     *
     * @param pView Main activity of the game.
     * @see GameActivity
     */
    public PhysicalGameEngine(GameActivity pView) {

        // Assign GameActivity
        mActivity = pView;
        mManager = (SensorManager) mActivity.getBaseContext().getSystemService(Service.SENSOR_SERVICE);
        mAccelerometer = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    /**
     * Sensor change event listener for game.
     * Triggered when sensor capture data.
     *
     * @param pEvent Sensor event object.
     * @see SensorEvent
     */
    @Override
    public void onSensorChanged(SensorEvent pEvent) {
        float x = pEvent.values[0];
        float y = pEvent.values[1];
        float z = pEvent.values[2];

        // Porusz piłką jeśli inne niż null
        if(mBall != null) {
            // Aktualizauj pozycje piłki
            RectF hitBox = mBall.putXAndY(x, y);

            if(hitBox == null) return;

            for(Bloc block : mBlocks) {
                // Stwórz nowy blok
                RectF inter = new RectF(block.getRectangle());
                if(inter.intersect(hitBox)) {
                    // Sprawdz jaki typ bloku
                    switch(block.getType()) {
                        case HOLE:
                            mActivity.showInfoDialog(GameActivity.DEFEAT_DIALOG);
                            break;
                        case END:
                            mActivity.showInfoDialog(GameActivity.VICTORY_DIALOG);
                            break;
                    }
                    break;
                }
            }
        }

        // Oblicz przyśpieszenie
        mAccelerationLast       = mAccelerationCurrent;
        mAccelerationCurrent    = Math.sqrt(x * x + y * y + z * z);
        mAcceleration           = (mAcceleration * 0.9f) + (mAccelerationCurrent - mAccelerationLast);

        // Jeśli liczba obliczeń przekracza limit szybkości
        if (accCount <= ACC_RATE) {
            accCount++;
            accSum += Math.abs(mAcceleration);
        } else {

            // Wynik przyśpieszenia
            accResult = accSum / ACC_RATE;

            // Jeśli limit przekroczony, wyświetl alert poruszania

            // Reset zmiennych pomocniczych
            accCount    = 0;
            accSum      = 0;
            accResult   = 0;
        }
    }

    /**
     * Sensors accuracy change event listener
     * Triggered when accuracy of sensor changed
     *
     * @param pSensor Sensor object.
     * @param pAccuracy New accuracy value.
     * @see Sensor
     */
    @Override
    public void onAccuracyChanged(Sensor pSensor, int pAccuracy) { }

    /**
     * Reset ball to original position
     */
    public void reset() {
        mBall.reset();
    }

    /**
     * Unregister event listener on accelerometer captor
     */
    public void stop() {

        mManager.unregisterListener(this, mAccelerometer);
    }

    /**
     * Attach accelerometer sensor to the event listener (to start tracking data)
     */
    public void resume() {

        mManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Set ball of the game
     *
     * @param pBall New ball object
     * @see Ball
     */
    public void setBall(Ball pBall) {
        this.mBall = pBall;
    }

    /**
     * Define list of bloc used to create pattern of the game
     * Bloc are instantiated with type, X & Y values.
     *
     * @return Nothing.
     * @see Bloc
     */
    public List<Bloc> buildLabyrinthe0() {
        mBlocks = new ArrayList<>();
        mBlocks.add(new Bloc(Type.HOLE, 0, 0));
        mBlocks.add(new Bloc(Type.HOLE, 0, 1));
        mBlocks.add(new Bloc(Type.HOLE, 0, 2));
        mBlocks.add(new Bloc(Type.HOLE, 0, 3));
        mBlocks.add(new Bloc(Type.HOLE, 0, 4));
        mBlocks.add(new Bloc(Type.HOLE, 0, 5));
        mBlocks.add(new Bloc(Type.HOLE, 0, 6));
        mBlocks.add(new Bloc(Type.HOLE, 0, 7));
        mBlocks.add(new Bloc(Type.HOLE, 0, 8));
        mBlocks.add(new Bloc(Type.HOLE, 0, 9));
        mBlocks.add(new Bloc(Type.HOLE, 0, 10));
        mBlocks.add(new Bloc(Type.HOLE, 0, 11));
        mBlocks.add(new Bloc(Type.HOLE, 0, 12));
        mBlocks.add(new Bloc(Type.HOLE, 0, 13));

        mBlocks.add(new Bloc(Type.HOLE, 1, 0));
        mBlocks.add(new Bloc(Type.HOLE, 1, 13));

        mBlocks.add(new Bloc(Type.HOLE, 2, 0));
        mBlocks.add(new Bloc(Type.HOLE, 2, 13));

        mBlocks.add(new Bloc(Type.HOLE, 3, 0));
        mBlocks.add(new Bloc(Type.HOLE, 3, 13));

        mBlocks.add(new Bloc(Type.HOLE, 4, 0));
        mBlocks.add(new Bloc(Type.HOLE, 4, 13));

        mBlocks.add(new Bloc(Type.HOLE, 5, 0));
        mBlocks.add(new Bloc(Type.HOLE, 5, 13));

        mBlocks.add(new Bloc(Type.HOLE, 6, 0));
        mBlocks.add(new Bloc(Type.HOLE, 6, 13));

        mBlocks.add(new Bloc(Type.HOLE, 7, 0));
        mBlocks.add(new Bloc(Type.HOLE, 7, 13));

        mBlocks.add(new Bloc(Type.HOLE, 8, 0));
        mBlocks.add(new Bloc(Type.HOLE, 8, 13));

        mBlocks.add(new Bloc(Type.HOLE, 9, 0));
        mBlocks.add(new Bloc(Type.HOLE, 9, 13));

        mBlocks.add(new Bloc(Type.HOLE, 10, 0));

        mBlocks.add(new Bloc(Type.HOLE, 10, 13));

        mBlocks.add(new Bloc(Type.HOLE, 11, 0));

        mBlocks.add(new Bloc(Type.HOLE, 11, 13));

        mBlocks.add(new Bloc(Type.HOLE, 12, 0));
        mBlocks.add(new Bloc(Type.HOLE, 12, 13));

        mBlocks.add(new Bloc(Type.HOLE, 13, 0));
        mBlocks.add(new Bloc(Type.HOLE, 13, 13));

        mBlocks.add(new Bloc(Type.HOLE, 14, 0));
        mBlocks.add(new Bloc(Type.HOLE, 14, 13));

        mBlocks.add(new Bloc(Type.HOLE, 15, 0));
        mBlocks.add(new Bloc(Type.HOLE, 15, 13));

        mBlocks.add(new Bloc(Type.HOLE, 16, 0));
        mBlocks.add(new Bloc(Type.HOLE, 16, 13));

        mBlocks.add(new Bloc(Type.HOLE, 17, 0));
        mBlocks.add(new Bloc(Type.HOLE, 17, 13));

        mBlocks.add(new Bloc(Type.HOLE, 18, 0));
        mBlocks.add(new Bloc(Type.HOLE, 18, 13));

        mBlocks.add(new Bloc(Type.HOLE, 19, 0));
        mBlocks.add(new Bloc(Type.HOLE, 19, 1));
        mBlocks.add(new Bloc(Type.HOLE, 19, 2));
        mBlocks.add(new Bloc(Type.HOLE, 19, 3));
        mBlocks.add(new Bloc(Type.HOLE, 19, 4));
        mBlocks.add(new Bloc(Type.HOLE, 19, 5));
        mBlocks.add(new Bloc(Type.HOLE, 19, 6));
        mBlocks.add(new Bloc(Type.HOLE, 19, 7));
        mBlocks.add(new Bloc(Type.HOLE, 19, 8));
        mBlocks.add(new Bloc(Type.HOLE, 19, 9));
        mBlocks.add(new Bloc(Type.HOLE, 19, 10));
        mBlocks.add(new Bloc(Type.HOLE, 19, 11));
        mBlocks.add(new Bloc(Type.HOLE, 19, 12));
        mBlocks.add(new Bloc(Type.HOLE, 19, 13));

        Bloc b = new Bloc(Type.START, 2, 2);
        mBall.setInitialRectangle(new RectF(b.getRectangle()));
        mBlocks.add(b);

        mBlocks.add(new Bloc(Type.END, 2, 11));

        return mBlocks;
    }

    public List<Bloc> buildLabyrinthe1() {
        mBlocks = new ArrayList<>();
        mBlocks.add(new Bloc(Type.HOLE, 0, 0));
        mBlocks.add(new Bloc(Type.HOLE, 0, 1));
        mBlocks.add(new Bloc(Type.HOLE, 0, 2));
        mBlocks.add(new Bloc(Type.HOLE, 0, 3));
        mBlocks.add(new Bloc(Type.HOLE, 0, 4));
        mBlocks.add(new Bloc(Type.HOLE, 0, 5));
        mBlocks.add(new Bloc(Type.HOLE, 0, 6));
        mBlocks.add(new Bloc(Type.HOLE, 0, 7));
        mBlocks.add(new Bloc(Type.HOLE, 0, 8));
        mBlocks.add(new Bloc(Type.HOLE, 0, 9));
        mBlocks.add(new Bloc(Type.HOLE, 0, 10));
        mBlocks.add(new Bloc(Type.HOLE, 0, 11));
        mBlocks.add(new Bloc(Type.HOLE, 0, 12));
        mBlocks.add(new Bloc(Type.HOLE, 0, 13));

        mBlocks.add(new Bloc(Type.HOLE, 1, 0));
        mBlocks.add(new Bloc(Type.HOLE, 1, 13));

        mBlocks.add(new Bloc(Type.HOLE, 2, 0));
        mBlocks.add(new Bloc(Type.HOLE, 2, 13));

        mBlocks.add(new Bloc(Type.HOLE, 3, 0));
        mBlocks.add(new Bloc(Type.HOLE, 3, 13));

        mBlocks.add(new Bloc(Type.HOLE, 4, 0));
        mBlocks.add(new Bloc(Type.HOLE, 4, 1));
        mBlocks.add(new Bloc(Type.HOLE, 4, 2));
        mBlocks.add(new Bloc(Type.HOLE, 4, 3));
        mBlocks.add(new Bloc(Type.HOLE, 4, 4));
        mBlocks.add(new Bloc(Type.HOLE, 4, 5));
        mBlocks.add(new Bloc(Type.HOLE, 4, 6));
        mBlocks.add(new Bloc(Type.HOLE, 4, 7));
        mBlocks.add(new Bloc(Type.HOLE, 4, 8));
        mBlocks.add(new Bloc(Type.HOLE, 4, 12));
        mBlocks.add(new Bloc(Type.HOLE, 4, 13));

        mBlocks.add(new Bloc(Type.HOLE, 5, 0));
        mBlocks.add(new Bloc(Type.HOLE, 5, 13));

        mBlocks.add(new Bloc(Type.HOLE, 6, 0));
        mBlocks.add(new Bloc(Type.HOLE, 6, 13));

        mBlocks.add(new Bloc(Type.HOLE, 7, 0));
        mBlocks.add(new Bloc(Type.HOLE, 7, 13));

        mBlocks.add(new Bloc(Type.HOLE, 8, 0));
        mBlocks.add(new Bloc(Type.HOLE, 8, 1));
        mBlocks.add(new Bloc(Type.HOLE, 8, 2));
        mBlocks.add(new Bloc(Type.HOLE, 8, 6));
        mBlocks.add(new Bloc(Type.HOLE, 8, 7));
        mBlocks.add(new Bloc(Type.HOLE, 8, 8));
        mBlocks.add(new Bloc(Type.HOLE, 8, 9));
        mBlocks.add(new Bloc(Type.HOLE, 8, 10));
        mBlocks.add(new Bloc(Type.HOLE, 8, 11));
        mBlocks.add(new Bloc(Type.HOLE, 8, 12));
        mBlocks.add(new Bloc(Type.HOLE, 8, 13));

        mBlocks.add(new Bloc(Type.HOLE, 9, 0));
        mBlocks.add(new Bloc(Type.HOLE, 9, 13));

        mBlocks.add(new Bloc(Type.HOLE, 10, 0));

        mBlocks.add(new Bloc(Type.HOLE, 10, 13));

        mBlocks.add(new Bloc(Type.HOLE, 11, 0));

        mBlocks.add(new Bloc(Type.HOLE, 11, 13));

        mBlocks.add(new Bloc(Type.HOLE, 12, 0));
        mBlocks.add(new Bloc(Type.HOLE, 12, 1));
        mBlocks.add(new Bloc(Type.HOLE, 12, 2));
        mBlocks.add(new Bloc(Type.HOLE, 12, 3));
        mBlocks.add(new Bloc(Type.HOLE, 12, 4));
        mBlocks.add(new Bloc(Type.HOLE, 12, 8));
        mBlocks.add(new Bloc(Type.HOLE, 12, 9));
        mBlocks.add(new Bloc(Type.HOLE, 12, 10));
        mBlocks.add(new Bloc(Type.HOLE, 12, 11));
        mBlocks.add(new Bloc(Type.HOLE, 12, 12));
        mBlocks.add(new Bloc(Type.HOLE, 12, 13));

        mBlocks.add(new Bloc(Type.HOLE, 13, 0));
        mBlocks.add(new Bloc(Type.HOLE, 13, 13));

        mBlocks.add(new Bloc(Type.HOLE, 14, 0));
        mBlocks.add(new Bloc(Type.HOLE, 14, 13));

        mBlocks.add(new Bloc(Type.HOLE, 15, 0));
        mBlocks.add(new Bloc(Type.HOLE, 15, 13));

        mBlocks.add(new Bloc(Type.HOLE, 16, 0));
        mBlocks.add(new Bloc(Type.HOLE, 16, 1));
        mBlocks.add(new Bloc(Type.HOLE, 16, 2));
        mBlocks.add(new Bloc(Type.HOLE, 16, 3));
        mBlocks.add(new Bloc(Type.HOLE, 16, 4));
        mBlocks.add(new Bloc(Type.HOLE, 16, 5));
        mBlocks.add(new Bloc(Type.HOLE, 16, 6));
        mBlocks.add(new Bloc(Type.HOLE, 16, 7));
        mBlocks.add(new Bloc(Type.HOLE, 16, 11));
        mBlocks.add(new Bloc(Type.HOLE, 16, 12));
        mBlocks.add(new Bloc(Type.HOLE, 16, 13));

        mBlocks.add(new Bloc(Type.HOLE, 17, 0));
        mBlocks.add(new Bloc(Type.HOLE, 17, 13));

        mBlocks.add(new Bloc(Type.HOLE, 18, 0));
        mBlocks.add(new Bloc(Type.HOLE, 18, 13));

        mBlocks.add(new Bloc(Type.HOLE, 19, 0));
        mBlocks.add(new Bloc(Type.HOLE, 19, 13));

        mBlocks.add(new Bloc(Type.HOLE, 20, 0));
        mBlocks.add(new Bloc(Type.HOLE, 20, 1));
        mBlocks.add(new Bloc(Type.HOLE, 20, 2));
        mBlocks.add(new Bloc(Type.HOLE, 20, 3));
        mBlocks.add(new Bloc(Type.HOLE, 20, 7));
        mBlocks.add(new Bloc(Type.HOLE, 20, 8));
        mBlocks.add(new Bloc(Type.HOLE, 20, 9));
        mBlocks.add(new Bloc(Type.HOLE, 20, 10));
        mBlocks.add(new Bloc(Type.HOLE, 20, 11));
        mBlocks.add(new Bloc(Type.HOLE, 20, 12));
        mBlocks.add(new Bloc(Type.HOLE, 20, 13));

        mBlocks.add(new Bloc(Type.HOLE, 21, 0));
        mBlocks.add(new Bloc(Type.HOLE, 21, 13));

        mBlocks.add(new Bloc(Type.HOLE, 22, 0));
        mBlocks.add(new Bloc(Type.HOLE, 22, 13));

        mBlocks.add(new Bloc(Type.HOLE, 23, 0));
        mBlocks.add(new Bloc(Type.HOLE, 23, 13));

        mBlocks.add(new Bloc(Type.HOLE, 24, 0));
        mBlocks.add(new Bloc(Type.HOLE, 24, 13));

        mBlocks.add(new Bloc(Type.HOLE, 25, 0));
        mBlocks.add(new Bloc(Type.HOLE, 25, 13));

        mBlocks.add(new Bloc(Type.HOLE, 26, 0));
        mBlocks.add(new Bloc(Type.HOLE, 26, 1));
        mBlocks.add(new Bloc(Type.HOLE, 26, 2));
        mBlocks.add(new Bloc(Type.HOLE, 26, 3));
        mBlocks.add(new Bloc(Type.HOLE, 26, 4));
        mBlocks.add(new Bloc(Type.HOLE, 26, 5));
        mBlocks.add(new Bloc(Type.HOLE, 26, 6));
        mBlocks.add(new Bloc(Type.HOLE, 26, 7));
        mBlocks.add(new Bloc(Type.HOLE, 26, 8));
        mBlocks.add(new Bloc(Type.HOLE, 26, 9));
        mBlocks.add(new Bloc(Type.HOLE, 26, 10));
        mBlocks.add(new Bloc(Type.HOLE, 26, 11));
        mBlocks.add(new Bloc(Type.HOLE, 26, 12));
        mBlocks.add(new Bloc(Type.HOLE, 26, 13));

        Bloc b = new Bloc(Type.START, 2, 2);
        mBall.setInitialRectangle(new RectF(b.getRectangle()));
        mBlocks.add(b);

        mBlocks.add(new Bloc(Type.END, 23, 3));

        return mBlocks;
    }

    public List<Bloc> buildLabyrinthe2() {
        mBlocks = new ArrayList<>();
        mBlocks.add(new Bloc(Type.HOLE, 0, 0));
        mBlocks.add(new Bloc(Type.HOLE, 0, 1));
        mBlocks.add(new Bloc(Type.HOLE, 0, 2));
        mBlocks.add(new Bloc(Type.HOLE, 0, 3));
        mBlocks.add(new Bloc(Type.HOLE, 0, 4));
        mBlocks.add(new Bloc(Type.HOLE, 0, 5));
        mBlocks.add(new Bloc(Type.HOLE, 0, 6));
        mBlocks.add(new Bloc(Type.HOLE, 0, 7));
        mBlocks.add(new Bloc(Type.HOLE, 0, 8));
        mBlocks.add(new Bloc(Type.HOLE, 0, 9));
        mBlocks.add(new Bloc(Type.HOLE, 0, 10));
        mBlocks.add(new Bloc(Type.HOLE, 0, 11));
        mBlocks.add(new Bloc(Type.HOLE, 0, 12));
        mBlocks.add(new Bloc(Type.HOLE, 0, 13));

        mBlocks.add(new Bloc(Type.HOLE, 1, 4));
        mBlocks.add(new Bloc(Type.HOLE, 2, 4));
        mBlocks.add(new Bloc(Type.HOLE, 3, 4));
        mBlocks.add(new Bloc(Type.HOLE, 4, 4));
        mBlocks.add(new Bloc(Type.HOLE, 5, 4));
        mBlocks.add(new Bloc(Type.HOLE, 6, 4));
        mBlocks.add(new Bloc(Type.HOLE, 7, 4));
        mBlocks.add(new Bloc(Type.HOLE, 8, 4));
        mBlocks.add(new Bloc(Type.HOLE, 9, 4));
        mBlocks.add(new Bloc(Type.HOLE, 10, 4));
        mBlocks.add(new Bloc(Type.HOLE, 11, 4));
        mBlocks.add(new Bloc(Type.HOLE, 12, 4));
        mBlocks.add(new Bloc(Type.HOLE, 13, 4));
        mBlocks.add(new Bloc(Type.HOLE, 14, 4));
        mBlocks.add(new Bloc(Type.HOLE, 15, 4));
        mBlocks.add(new Bloc(Type.HOLE, 16, 4));
        mBlocks.add(new Bloc(Type.HOLE, 17, 4));
        mBlocks.add(new Bloc(Type.HOLE, 18, 4));
        mBlocks.add(new Bloc(Type.HOLE, 19, 4));
        mBlocks.add(new Bloc(Type.HOLE, 20, 4));
        mBlocks.add(new Bloc(Type.HOLE, 21, 4));

        mBlocks.add(new Bloc(Type.HOLE, 4, 8));
        mBlocks.add(new Bloc(Type.HOLE, 5, 8));
        mBlocks.add(new Bloc(Type.HOLE, 6, 8));
        mBlocks.add(new Bloc(Type.HOLE, 7, 8));
        mBlocks.add(new Bloc(Type.HOLE, 8, 8));
        mBlocks.add(new Bloc(Type.HOLE, 9, 8));
        mBlocks.add(new Bloc(Type.HOLE, 10, 8));
        mBlocks.add(new Bloc(Type.HOLE, 11, 8));
        mBlocks.add(new Bloc(Type.HOLE, 12, 8));
        mBlocks.add(new Bloc(Type.HOLE, 13, 8));
        mBlocks.add(new Bloc(Type.HOLE, 14, 8));
        mBlocks.add(new Bloc(Type.HOLE, 15, 8));
        mBlocks.add(new Bloc(Type.HOLE, 16, 8));
        mBlocks.add(new Bloc(Type.HOLE, 17, 8));
        mBlocks.add(new Bloc(Type.HOLE, 18, 8));
        mBlocks.add(new Bloc(Type.HOLE, 19, 8));
        mBlocks.add(new Bloc(Type.HOLE, 20, 8));
        mBlocks.add(new Bloc(Type.HOLE, 21, 8));
        mBlocks.add(new Bloc(Type.HOLE, 22, 8));
        mBlocks.add(new Bloc(Type.HOLE, 23, 8));
        mBlocks.add(new Bloc(Type.HOLE, 24, 8));
        mBlocks.add(new Bloc(Type.HOLE, 25, 8));

        mBlocks.add(new Bloc(Type.HOLE, 1, 0));
        mBlocks.add(new Bloc(Type.HOLE, 1, 13));

        mBlocks.add(new Bloc(Type.HOLE, 2, 0));
        mBlocks.add(new Bloc(Type.HOLE, 2, 13));

        mBlocks.add(new Bloc(Type.HOLE, 3, 0));
        mBlocks.add(new Bloc(Type.HOLE, 3, 13));

        mBlocks.add(new Bloc(Type.HOLE, 4, 0));
        mBlocks.add(new Bloc(Type.HOLE, 4, 13));

        mBlocks.add(new Bloc(Type.HOLE, 5, 0));
        mBlocks.add(new Bloc(Type.HOLE, 5, 9));
        mBlocks.add(new Bloc(Type.HOLE, 5, 13));

        mBlocks.add(new Bloc(Type.HOLE, 6, 0));
        mBlocks.add(new Bloc(Type.HOLE, 6, 13));

        mBlocks.add(new Bloc(Type.HOLE, 7, 0));
        mBlocks.add(new Bloc(Type.HOLE, 7, 13));

        mBlocks.add(new Bloc(Type.HOLE, 8, 0));
        mBlocks.add(new Bloc(Type.HOLE, 8, 13));

        mBlocks.add(new Bloc(Type.HOLE, 9, 0));
        mBlocks.add(new Bloc(Type.HOLE, 9, 12));
        mBlocks.add(new Bloc(Type.HOLE, 9, 13));

        mBlocks.add(new Bloc(Type.HOLE, 10, 0));
        mBlocks.add(new Bloc(Type.HOLE, 10, 13));

        mBlocks.add(new Bloc(Type.HOLE, 11, 0));
        mBlocks.add(new Bloc(Type.HOLE, 11, 13));

        mBlocks.add(new Bloc(Type.HOLE, 12, 0));
        mBlocks.add(new Bloc(Type.HOLE, 12, 13));

        mBlocks.add(new Bloc(Type.HOLE, 13, 0));
        mBlocks.add(new Bloc(Type.HOLE, 13, 9));
        mBlocks.add(new Bloc(Type.HOLE, 13, 13));

        mBlocks.add(new Bloc(Type.HOLE, 14, 0));
        mBlocks.add(new Bloc(Type.HOLE, 14, 13));

        mBlocks.add(new Bloc(Type.HOLE, 15, 0));
        mBlocks.add(new Bloc(Type.HOLE, 15, 13));

        mBlocks.add(new Bloc(Type.HOLE, 16, 0));
        mBlocks.add(new Bloc(Type.HOLE, 16, 13));

        mBlocks.add(new Bloc(Type.HOLE, 17, 0));
        mBlocks.add(new Bloc(Type.HOLE, 17, 12));
        mBlocks.add(new Bloc(Type.HOLE, 17, 13));

        mBlocks.add(new Bloc(Type.HOLE, 18, 0));
        mBlocks.add(new Bloc(Type.HOLE, 18, 13));

        mBlocks.add(new Bloc(Type.HOLE, 19, 0));
        mBlocks.add(new Bloc(Type.HOLE, 19, 13));

        mBlocks.add(new Bloc(Type.HOLE, 20, 0));
        mBlocks.add(new Bloc(Type.HOLE, 20, 13));

        mBlocks.add(new Bloc(Type.HOLE, 21, 0));
        mBlocks.add(new Bloc(Type.HOLE, 21, 9));
        mBlocks.add(new Bloc(Type.HOLE, 21, 13));

        mBlocks.add(new Bloc(Type.HOLE, 22, 0));
        mBlocks.add(new Bloc(Type.HOLE, 22, 13));

        mBlocks.add(new Bloc(Type.HOLE, 23, 0));
        mBlocks.add(new Bloc(Type.HOLE, 23, 13));

        mBlocks.add(new Bloc(Type.HOLE, 24, 0));
        mBlocks.add(new Bloc(Type.HOLE, 24, 13));

        mBlocks.add(new Bloc(Type.HOLE, 25, 0));
        mBlocks.add(new Bloc(Type.HOLE, 25, 13));

        mBlocks.add(new Bloc(Type.HOLE, 26, 0));
        mBlocks.add(new Bloc(Type.HOLE, 26, 1));
        mBlocks.add(new Bloc(Type.HOLE, 26, 2));
        mBlocks.add(new Bloc(Type.HOLE, 26, 3));
        mBlocks.add(new Bloc(Type.HOLE, 26, 4));
        mBlocks.add(new Bloc(Type.HOLE, 26, 5));
        mBlocks.add(new Bloc(Type.HOLE, 26, 6));
        mBlocks.add(new Bloc(Type.HOLE, 26, 7));
        mBlocks.add(new Bloc(Type.HOLE, 26, 8));
        mBlocks.add(new Bloc(Type.HOLE, 26, 9));
        mBlocks.add(new Bloc(Type.HOLE, 26, 10));
        mBlocks.add(new Bloc(Type.HOLE, 26, 11));
        mBlocks.add(new Bloc(Type.HOLE, 26, 12));
        mBlocks.add(new Bloc(Type.HOLE, 26, 13));

        Bloc b = new Bloc(Type.START, 2, 2);
        mBall.setInitialRectangle(new RectF(b.getRectangle()));
        mBlocks.add(b);

        mBlocks.add(new Bloc(Type.END, 25, 12));

        return mBlocks;
    }

    public List<Bloc> buildLabyrinthe3() {
        mBlocks = new ArrayList<>();
        mBlocks.add(new Bloc(Type.HOLE, 0, 0));
        mBlocks.add(new Bloc(Type.HOLE, 0, 1));
        mBlocks.add(new Bloc(Type.HOLE, 0, 2));
        mBlocks.add(new Bloc(Type.HOLE, 0, 3));
        mBlocks.add(new Bloc(Type.HOLE, 0, 4));
        mBlocks.add(new Bloc(Type.HOLE, 0, 5));
        mBlocks.add(new Bloc(Type.HOLE, 0, 6));
        mBlocks.add(new Bloc(Type.HOLE, 0, 7));
        mBlocks.add(new Bloc(Type.HOLE, 0, 8));
        mBlocks.add(new Bloc(Type.HOLE, 0, 9));
        mBlocks.add(new Bloc(Type.HOLE, 0, 10));
        mBlocks.add(new Bloc(Type.HOLE, 0, 11));
        mBlocks.add(new Bloc(Type.HOLE, 0, 12));
        mBlocks.add(new Bloc(Type.HOLE, 0, 13));

        mBlocks.add(new Bloc(Type.HOLE, 1, 0));
        mBlocks.add(new Bloc(Type.HOLE, 1, 13));

        mBlocks.add(new Bloc(Type.HOLE, 2, 0));
        mBlocks.add(new Bloc(Type.HOLE, 2, 13));

        mBlocks.add(new Bloc(Type.HOLE, 3, 0));
        mBlocks.add(new Bloc(Type.HOLE, 3, 13));

        mBlocks.add(new Bloc(Type.HOLE, 4, 0));
        mBlocks.add(new Bloc(Type.HOLE, 4, 1));
        mBlocks.add(new Bloc(Type.HOLE, 4, 2));
        mBlocks.add(new Bloc(Type.HOLE, 4, 3));
        mBlocks.add(new Bloc(Type.HOLE, 4, 4));
        mBlocks.add(new Bloc(Type.HOLE, 4, 8));
        mBlocks.add(new Bloc(Type.HOLE, 4, 9));
        mBlocks.add(new Bloc(Type.HOLE, 4, 10));
        mBlocks.add(new Bloc(Type.HOLE, 4, 11));
        mBlocks.add(new Bloc(Type.HOLE, 4, 12));
        mBlocks.add(new Bloc(Type.HOLE, 4, 13));

        mBlocks.add(new Bloc(Type.HOLE, 5, 0));
        mBlocks.add(new Bloc(Type.HOLE, 5, 13));

        mBlocks.add(new Bloc(Type.HOLE, 6, 0));
        mBlocks.add(new Bloc(Type.HOLE, 6, 13));

        mBlocks.add(new Bloc(Type.HOLE, 7, 0));
        mBlocks.add(new Bloc(Type.HOLE, 7, 3));
        mBlocks.add(new Bloc(Type.HOLE, 7, 4));
        mBlocks.add(new Bloc(Type.HOLE, 7, 5));
        mBlocks.add(new Bloc(Type.HOLE, 7, 6));
        mBlocks.add(new Bloc(Type.HOLE, 7, 7));
        mBlocks.add(new Bloc(Type.HOLE, 7, 8));
        mBlocks.add(new Bloc(Type.HOLE, 7, 9));
        mBlocks.add(new Bloc(Type.HOLE, 7, 10));
        mBlocks.add(new Bloc(Type.HOLE, 7, 13));

        mBlocks.add(new Bloc(Type.HOLE, 8, 0));
        mBlocks.add(new Bloc(Type.HOLE, 8, 3));
        mBlocks.add(new Bloc(Type.HOLE, 8, 10));
        mBlocks.add(new Bloc(Type.HOLE, 8, 13));

        mBlocks.add(new Bloc(Type.HOLE, 9, 0));
        mBlocks.add(new Bloc(Type.HOLE, 9, 3));
        mBlocks.add(new Bloc(Type.HOLE, 9, 10));
        mBlocks.add(new Bloc(Type.HOLE, 9, 13));

        mBlocks.add(new Bloc(Type.HOLE, 10, 0));
        mBlocks.add(new Bloc(Type.HOLE, 10, 13));

        mBlocks.add(new Bloc(Type.HOLE, 11, 0));
        mBlocks.add(new Bloc(Type.HOLE, 11, 13));

        mBlocks.add(new Bloc(Type.HOLE, 12, 0));
        mBlocks.add(new Bloc(Type.HOLE, 12, 13));

        mBlocks.add(new Bloc(Type.HOLE, 13, 0));
        mBlocks.add(new Bloc(Type.HOLE, 13, 13));

        mBlocks.add(new Bloc(Type.HOLE, 14, 0));
        mBlocks.add(new Bloc(Type.HOLE, 14, 3));
        mBlocks.add(new Bloc(Type.HOLE, 14, 10));
        mBlocks.add(new Bloc(Type.HOLE, 14, 13));

        mBlocks.add(new Bloc(Type.HOLE, 15, 0));
        mBlocks.add(new Bloc(Type.HOLE, 15, 3));
        mBlocks.add(new Bloc(Type.HOLE, 15, 10));
        mBlocks.add(new Bloc(Type.HOLE, 15, 13));

        mBlocks.add(new Bloc(Type.HOLE, 16, 0));
        mBlocks.add(new Bloc(Type.HOLE, 16, 3));;
        mBlocks.add(new Bloc(Type.HOLE, 16, 4));
        mBlocks.add(new Bloc(Type.HOLE, 16, 5));
        mBlocks.add(new Bloc(Type.HOLE, 16, 6));
        mBlocks.add(new Bloc(Type.HOLE, 16, 7));
        mBlocks.add(new Bloc(Type.HOLE, 16, 8));
        mBlocks.add(new Bloc(Type.HOLE, 16, 9));
        mBlocks.add(new Bloc(Type.HOLE, 16, 10));
        mBlocks.add(new Bloc(Type.HOLE, 16, 13));

        mBlocks.add(new Bloc(Type.HOLE, 17, 0));
        mBlocks.add(new Bloc(Type.HOLE, 17, 13));

        mBlocks.add(new Bloc(Type.HOLE, 18, 0));
        mBlocks.add(new Bloc(Type.HOLE, 18, 13));

        mBlocks.add(new Bloc(Type.HOLE, 19, 0));
        mBlocks.add(new Bloc(Type.HOLE, 19, 1));
        mBlocks.add(new Bloc(Type.HOLE, 19, 2));
        mBlocks.add(new Bloc(Type.HOLE, 19, 3));
        mBlocks.add(new Bloc(Type.HOLE, 19, 4));
        mBlocks.add(new Bloc(Type.HOLE, 19, 8));
        mBlocks.add(new Bloc(Type.HOLE, 19, 9));
        mBlocks.add(new Bloc(Type.HOLE, 19, 10));
        mBlocks.add(new Bloc(Type.HOLE, 19, 11));
        mBlocks.add(new Bloc(Type.HOLE, 19, 12));
        mBlocks.add(new Bloc(Type.HOLE, 19, 13));

        mBlocks.add(new Bloc(Type.HOLE, 20, 0));
        mBlocks.add(new Bloc(Type.HOLE, 20, 13));

        mBlocks.add(new Bloc(Type.HOLE, 21, 0));
        mBlocks.add(new Bloc(Type.HOLE, 21, 13));

        mBlocks.add(new Bloc(Type.HOLE, 22, 0));
        mBlocks.add(new Bloc(Type.HOLE, 22, 13));

        mBlocks.add(new Bloc(Type.HOLE, 23, 0));
        mBlocks.add(new Bloc(Type.HOLE, 23, 13));

        mBlocks.add(new Bloc(Type.HOLE, 23, 0));
        mBlocks.add(new Bloc(Type.HOLE, 23, 1));
        mBlocks.add(new Bloc(Type.HOLE, 23, 2));
        mBlocks.add(new Bloc(Type.HOLE, 23, 3));
        mBlocks.add(new Bloc(Type.HOLE, 23, 4));
        mBlocks.add(new Bloc(Type.HOLE, 23, 5));
        mBlocks.add(new Bloc(Type.HOLE, 23, 6));
        mBlocks.add(new Bloc(Type.HOLE, 23, 7));
        mBlocks.add(new Bloc(Type.HOLE, 23, 8));
        mBlocks.add(new Bloc(Type.HOLE, 23, 9));
        mBlocks.add(new Bloc(Type.HOLE, 23, 10));
        mBlocks.add(new Bloc(Type.HOLE, 23, 11));
        mBlocks.add(new Bloc(Type.HOLE, 23, 12));
        mBlocks.add(new Bloc(Type.HOLE, 23, 13));

        Bloc b = new Bloc(Type.START, 2, 2);
        mBall.setInitialRectangle(new RectF(b.getRectangle()));
        mBlocks.add(b);

        mBlocks.add(new Bloc(Type.END, 12, 6));

        return mBlocks;
    }
}