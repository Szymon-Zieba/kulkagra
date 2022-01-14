package org.o7planning.kulkagra;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

import org.o7planning.kulkagra.Ball;
import org.o7planning.kulkagra.Bloc;



public class GraphicGameEngine extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG         = "GraphicGameEngine";
    public static final int SURFACE_RATIO   = 25;

    private final SurfaceHolder mSurfaceHolder;

    // Abstrakcyjny interfejs dla kogoś, kto trzyma powierzchnię wyświetlacza. Pozwala kontrolować rozmiar i format
    // powierzchni, edytować piksele na powierzchni i monitorować zmiany na powierzchni. Ten interfejs jest zazwyczaj dostępny w SurfaceViewklasie.
    private final DrawingThread mThread;

    // Klasa Paint zawiera informacje o stylu i kolorze dotyczące rysowania geometrii, tekstu i map bitowych.
    private final Paint mPaint;

    private Ball mBall;
    private int surfaceBgColor = Color.CYAN;
    private List<Bloc> mBlocks = null;

    /**
     * Constructor of GraphicGameEngine class.
     *
     * @param pContext Context of the activity.
     * @see Context
     */
    public GraphicGameEngine(Context pContext) {
        super(pContext);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mThread = new DrawingThread();

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);

     //   mBall = new Ball();
    }

    /**
     * Draw game design like surface & bloc color for ex.
     * Method will be called on every frame calculated.
     *
     * @param pCanvas Canvas to draw.
     * @see Context
     */
    @Override
    public void draw(Canvas pCanvas) {
        super.draw(pCanvas);

        // Rysuj tło
        pCanvas.drawColor(this.surfaceBgColor);
        if(mBlocks != null) {
            // Rysuj bloki
            for(Bloc b : mBlocks) {
                switch(b.getType()) {
                    case START:
                        mPaint.setColor(Color.WHITE);
                        break;
                    case END:
                        mPaint.setColor(Color.RED);
                        break;
                    case HOLE:
                        mPaint.setColor(Color.BLACK);
                        break;
                }
                pCanvas.drawRect(b.getRectangle(), mPaint);
            }
        }

        // Rysuj piłkę
        if(mBall != null) {
            mPaint.setColor(mBall.getBallColor());
            pCanvas.drawCircle(mBall.getX(), mBall.getY(), Ball.RADIUS, mPaint);
        }
    }

    /**
     * Event triggered when surface change.
     *
     * @param pHolder New surface holder object.
     * @param pFormat Format of the new surface.
     * @param pWidth Width of the new surface.
     * @param pHeight Height of the new surface.
     * @see SurfaceHolder
     */
    @Override
    public void surfaceChanged(SurfaceHolder pHolder, int pFormat, int pWidth, int pHeight) { }

    /**
     * Event triggered when a new surface is created.
     *
     * @param pHolder New surface holder object.
     * @see SurfaceHolder
     */
    @Override
    public void surfaceCreated(SurfaceHolder pHolder) {
        mThread.keepDrawing = true;
        mThread.start();
        // Create ball using screen coordinates
        // Stwórz piłkę według koordynatów wyświetlacza
        if(mBall != null ) {
            this.mBall.setHeight(getHeight());
            this.mBall.setWidth(getWidth());
        }
    }

    /**
     * Event triggered when surface is destroyed.
     *
     * @param pHolder Surface holder object.
     * @see SurfaceHolder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder pHolder) {
        mThread.keepDrawing = false;
        boolean retry = true;
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.d(TAG, "Error when destroying surface");
            }
        }

    }

    /**
     * Thread used to launch draw process.
     */
    private class DrawingThread extends Thread {
        boolean keepDrawing = true;

        /**
         * When the thread is started, run this method.
         *
         * @see SurfaceHolder
         */
        @Override
        public void run() {
            Canvas canvas;
            while (keepDrawing) {
                canvas = null;

                try {
                    canvas = mSurfaceHolder.lockCanvas();
                    synchronized (mSurfaceHolder) {
                        draw(canvas);
                    }
                } finally {
                    if (canvas != null)
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    /**
     * Setter of the Ball property object.
     *
     * @param pBall New ball object
     * @see Ball
     */
    public void setBall(Ball pBall) {
        this.mBall = pBall;
    }

    /**
     * Setter of all blocs (pattern of the game).
     *
     * @param pBlocks The list of bloc object (pattern of the game)
     * @see Bloc
     */
    public void setBlocks(List<Bloc> pBlocks) {
        this.mBlocks = pBlocks;
    }

    /**
     * Set surface color according to luminosity level.
     */
    public void setSurfaceBgColor(float luminosity) {

        int color;
        if(luminosity <= 100.0f) {
            color = Color.GRAY;
        } else if (luminosity > 100.0f && luminosity <= 200.0f) {
            color = Color.BLUE;
        } else if (luminosity > 200.0f && luminosity <= 290.0f) {
            color = Color.CYAN;
        } else {
            color = Color.YELLOW;
        }

        this.surfaceBgColor = color;
    }
}

