package com.example.wagahai.texteditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wagahai on 2017/11/04.
 */

public class CanvasView extends View {
    private Bitmap mBitmap = null;
    private Canvas mBmpCanvas = null;
    private Paint mPaint = new Paint();
    private Rect mCanvasSize;

    private float px = -1.0F;
    private float py = -1.0F;
    private final float SCALE = 1.0F;

    public CanvasView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLUE);
        //Log.d("DrawEvent", "drawed");
        //Paint paint = mPaint;
        if (mBmpCanvas == null) {

            Log.d("ViewSize", "W:" + this.getWidth() + ", H:" + this.getHeight());
            mCanvasSize = new Rect(0, 0, this.getWidth(), this.getHeight());
            Bitmap loadedBmp = mBitmap;
            mBitmap = Bitmap.createBitmap((int) (this.getWidth() / SCALE),
                                          (int) (this.getHeight() / SCALE),
                                          Bitmap.Config.ARGB_8888);
            mBmpCanvas = new Canvas(mBitmap);
            setmBitmap(loadedBmp);

            mPaint.setColor(Color.BLACK);
            mPaint.setStrokeWidth(5);

            Log.d("Bitmap", "initialized");


        }
        canvas.drawBitmap(mBitmap, null, mCanvasSize, null);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN ) {

        } else if (ev.getAction() == MotionEvent.ACTION_UP ) {
            px = -1.0F; py = -1.0F;
        }
        final int historySize = ev.getHistorySize();
        final int pointerCount = ev.getPointerCount();
        for (int h = 0; h < historySize; h++) {
            for (int p = 0; p < pointerCount; p++) {
                float x = ev.getHistoricalX(p, h)/SCALE;
                if (x < 0) { x = 0; }
                else if (mBitmap.getWidth() < x ) { x = mBitmap.getWidth(); }
                float y = ev.getHistoricalY(p, h)/SCALE;
                if (y < 0) { y = 0; }
                else if (mBitmap.getHeight() < y) { mBitmap.getHeight(); }
                if(0 <= px && 0 <= py) {
                    mBmpCanvas.drawLine(px, py, x, y, mPaint);
                }
                px = x; py = y;

            }
        }
        invalidate();
        return true;
    }
    public void clearBmp() {
        if (mBmpCanvas == null) { return; }
        mBmpCanvas.drawColor(Color.WHITE);
        px = -1.0F; py = -1.0F;
        invalidate();
    }
    public Bitmap getmBitmap() {
        return mBitmap;
    }
    public void setmBitmap(Bitmap bmp) {
        if (bmp == null) {
            clearBmp();
        } else {
            if (mBmpCanvas == null) {
                mBitmap = bmp;
            } else {
                mBmpCanvas.drawBitmap(bmp, 0, 0, null);
                invalidate();
            }
        }
    }
    public int getmBitmapSize() {
        return mBitmap.getByteCount();
    }
}

