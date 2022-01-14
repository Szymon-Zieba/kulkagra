package org.o7planning.kulkagra;

import android.graphics.RectF;

public class Bloc {
    public enum  Type { HOLE, START, END }

    private Type mType = null;
    private RectF mRectangle = null;

    /**
     * Getter type of bloc
     *
     * @return The type of bloc.
     * @see Type
     */
    public Type getType() {
        return mType;
    }

    /**
     * Get shape of the bloc
     *
     * @return Rectangle shape object of the bloc.
     * @see RectF
     */
    public RectF getRectangle() {
        return mRectangle;
    }

    /**
     * Constructor of Bloc class
     *
     * @param pType Type of the bloc.
     * @param pX Position of the bloc in the X axis.
     * @param pY Position of the bloc in the Y axis.
     * @see Type
     */
    public Bloc(Type pType, int pX, int pY) {
        this.mType = pType;
        float blocSize = Ball.RADIUS * 2;
        this.mRectangle = new RectF(pX * blocSize, pY * blocSize, (pX + 1) * blocSize, (pY + 1) * blocSize);
    }
}
