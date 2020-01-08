package com.example.livescoreproject.classes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class RaportView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int[] COLORS = {Color.BLUE, Color.YELLOW, Color.RED};
    private float[] degrees_value;

    public RaportView(Context context) {
        super(context);
    }

    public RaportView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float height = getHeight();
        float width = getWidth();
        float size = Math.min(width, height);

        paint.setStrokeWidth(10);

        RectF rectf = new RectF(0, 0, getWidth(), getHeight());


        final int MARGIN = 50;
        rectf.set(MARGIN, MARGIN, size - MARGIN, size - MARGIN);

        float startAngle = 0;

        for (int i = 0; degrees_value != null && i < degrees_value.length; i++) {
            if (degrees_value[i] > 0) {
                paint.setColor(COLORS[i]);

                canvas.drawArc(rectf, startAngle, degrees_value[i], true, paint);

                startAngle += degrees_value[i];
            }
        }
    }

    public void setDegrees_value(float[] degrees_value) {
        if (degrees_value.length <= 11) {
            this.degrees_value = degrees_value;
        }

        invalidate();
    }
}
