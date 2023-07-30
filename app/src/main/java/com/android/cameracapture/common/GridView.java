package com.android.cameracapture.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class GridView extends View {

    private final Paint paint;
    private boolean showGrid = true;

    public GridView(Context context) {
        this(context, null);
    }

    public GridView(Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public GridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (showGrid) {
            int width = getWidth();
            int height = getHeight();
            float lineWidth = width * 1.0f / 3;
            float lineHeight = height * 1.0f / 3;

            canvas.drawLine(lineWidth, 0, lineWidth, height, paint);
            canvas.drawLine(2 * lineWidth, 0, 2 * lineWidth, height, paint);
            canvas.drawLine(0, lineHeight, width, lineHeight, paint);
            canvas.drawLine(0, 2 * lineHeight, width, 2 * lineHeight, paint);
        }
        super.onDraw(canvas);
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        postInvalidate();
    }
}
