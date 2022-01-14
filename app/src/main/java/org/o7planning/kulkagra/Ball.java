package org.o7planning.kulkagra;

import android.graphics.Color;
import android.graphics.RectF;


public class Ball {
    // Promień piłki
    public static float RADIUS  = 10.0f;
    private int ballColor       = Color.GREEN;

    // Maksymalna prędkość piłki
    private static final float MAX_SPEED = 4.0f;

    // Zwolnienie piłki
    private static final float COMPENSATOR = 8.0f;

    // Wyrównanie "granic"
    private static final float REBOUND = 1.75f;

    // Rect do ustawienia piłki na pozycje początkową
    private RectF mInitialRectangle = null;

    // Rect kolizja
    private RectF mRectangle = null;

    // Koordynaty X i Y
    private float mX;
    private float mY;

    // Predkość na osi
    private float mSpeedX = 0;
    private float mSpeedY = 0;

    // Rozmiar obrazu
    private int mWidth = -1;
    private int mHeight = -1;

    /**
     * Setter of initial rectangle (start point)
     *
     * @param pInitialRectangle Rectangle shape as starting point.
     * @see RectF
     */
    public void setInitialRectangle(RectF pInitialRectangle) {
        this.mInitialRectangle = pInitialRectangle;
        this.mX = pInitialRectangle.left + RADIUS;
        this.mY = pInitialRectangle.top + RADIUS;
    }

    /**
     * Get X position
     *
     * @return Nothing.
     */
    public float getX() {
        return mX;
    }

    /**
     * Set X position (start point)
     *
     * @param pPosX X axis position.
     */
    private void setPosX(float pPosX) {
        mX = pPosX;

        // Jeśli piłka wyleci poza granice, zmień kierunek piłki
        if(mX < RADIUS) {
            mX = RADIUS;
            // Zmień pozycje piłki, kiedy zmieni kierunek
            mSpeedY = -mSpeedY / REBOUND;
        } else if(mX > mWidth - RADIUS) {
            mX = mWidth - RADIUS;
            mSpeedY = -mSpeedY / REBOUND;
        }
    }

    /**
     * Get Y position
     *
     * @return Nothing.
     */
    public float getY() {
        return mY;
    }

    /**
     * Set Y position (start point)
     *
     * @param pPosY Y axis position.
     */
    private void setPosY(float pPosY) {
        mY = pPosY;
        if(mY < RADIUS) {
            mY = RADIUS;
            mSpeedX = -mSpeedX / REBOUND;
        } else if(mY > mHeight - RADIUS) {
            mY = mHeight - RADIUS;
            mSpeedX = -mSpeedX / REBOUND;
        }
    }

    /**
     * Set the height of the ball
     *
     * @param pHeight New height value of the ball
     * @see Ball
     */
    public void setHeight(int pHeight) {
        this.mHeight = pHeight;
    }

    /**
     * Set the width of the ball
     *
     * @param pWidth New width value of the ball
     * @see Ball
     */
    public void setWidth(int pWidth) {
        this.mWidth = pWidth;
    }

    /**
     * Constructor of Ball object
     *
     * @see Ball
     */
    public Ball() {
        mRectangle = new RectF();
    }

    /**
     * Setting ball coordinate (X and Y).
     *
     * @param pX X axis.
     * @param pY Y axis.
     * @return New position as RectF.
     * @see RectF
     */
    public RectF putXAndY(float pX, float pY) {
        mSpeedX += pX / COMPENSATOR;
        if(mSpeedX > MAX_SPEED)
            mSpeedX = MAX_SPEED;
        if(mSpeedX < -MAX_SPEED)
            mSpeedX = -MAX_SPEED;

        mSpeedY += pY / COMPENSATOR;
        if(mSpeedY > MAX_SPEED)
            mSpeedY = MAX_SPEED;
        if(mSpeedY < -MAX_SPEED)
            mSpeedY = -MAX_SPEED;

        setPosX(mX + mSpeedY);
        setPosY(mY + mSpeedX);

        // Ustaw koordynaty miejsca kolizji
        mRectangle.set(mX - RADIUS, mY - RADIUS, mX + RADIUS, mY + RADIUS);

        return mRectangle;
    }

    // Reset piłki do początkowej pozycji
    public void reset() {
        mSpeedX = 0;
        mSpeedY = 0;
        this.mX = mInitialRectangle.left + RADIUS;
        this.mY = mInitialRectangle.top + RADIUS;
    }

    /**
     * Getter of the ball color
     *
     * @return Color value as integer.
     */
    public int getBallColor() {
        return ballColor;
    }

    /**
     * Change ball color according to magnetic field level.
     *
     * @param magneticField Magnetic level captured by the device.
     */
    public void setBallColor(double magneticField) {

        int color;
        if(magneticField <= 100.0f) {
            color = Color.parseColor("#66ff33");
        } else if (magneticField > 100.0f && magneticField <= 200.0f) {
            color = Color.parseColor("#ff0066");
        } else if (magneticField > 200.0f && magneticField <= 300.0f) {
            color = Color.parseColor("#ff66ff");
        } else {
            color = Color.parseColor("#9900ff");
        }

        this.ballColor = color;
    }
}
