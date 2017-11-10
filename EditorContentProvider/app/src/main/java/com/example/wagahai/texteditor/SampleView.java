package com.example.wagahai.texteditor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by wagahai on 2017/11/04.
 */

public class SampleView extends View {
    private Paint mPaint = new Paint();
    private static final float SIZE = 500F;

    public SampleView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = mPaint;
        canvas.drawColor(Color.WHITE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);

        float px = 0, py = 0;
        for (int i = 0; i <= 360; i++) {
            double t = i*Math.PI/180;
            float x = SIZE * (float)Math.cos(3*t) + canvas.getWidth() / 2;
            float y = SIZE * (float)Math.sin(4*t) + canvas.getHeight() / 2;
            if (0 < i) {
                canvas.drawLine(x, y , px, py, paint);
            }
            px = x; py = y;
        }
    }
}

