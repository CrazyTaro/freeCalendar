package com.taro.calendar.lib.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.taro.calendar.lib.ColorSetting;

/**
 * Created by taro on 2017/6/2.
 */

public class FinishBackgroundDrawable extends Drawable {
    private Paint mPaint;
    private RectF mDrawRect;
    private int mFillColor;
    private float mStorkeWidth;
    private float mDrawX, mDrawY;
    private Path mPath;

    public FinishBackgroundDrawable() {
        this(ColorSetting.DEFAULT_COLOR_BLUE, 5);
    }

    public FinishBackgroundDrawable setFillColor(@ColorInt int color) {
        mFillColor = color;
        return this;
    }

    public FinishBackgroundDrawable setDrawXY(float x, float y) {
        mDrawX = x;
        mDrawY = y;
        return this;
    }

    public FinishBackgroundDrawable(@ColorInt int fillColor, float storkeWidth) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mDrawRect = new RectF();
        mPath = new Path();

        mFillColor = fillColor;
        mStorkeWidth = storkeWidth;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bound = getBounds();
        if (bound.width() > 0 && bound.height() > 0) {
            mDrawRect.set(bound);
            mDrawRect.offset(mDrawX, mDrawY);

            mPaint.setColor(mFillColor);
            mPaint.setStyle(Paint.Style.FILL);
            int sc = canvas.saveLayer(mDrawRect, null, Canvas.ALL_SAVE_FLAG);
            canvas.drawArc(mDrawRect, 0, 360, false, mPaint);

            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            float width = mDrawRect.width();
            float height = mDrawRect.height();
            float left = mDrawRect.right - width / 3;
            float top = mDrawRect.bottom - height / 3;
            mDrawRect.set(left, top, mDrawRect.right, mDrawRect.bottom);

            canvas.drawArc(mDrawRect, 0, 360, false, mPaint);
            mPaint.setXfermode(null);
            canvas.restoreToCount(sc);

            width = mDrawRect.width();
            height = mDrawRect.height();
            left = mDrawRect.left + (width * 1 / 7);
            top = mDrawRect.top + (height * 1 / 7);
            mDrawRect.set(left, top, left + width * 4 / 5, top + height * 4 / 5);
            canvas.drawArc(mDrawRect, 0, 360, false, mPaint);

            mPaint.setColor(Color.WHITE);
            width = mDrawRect.width();
            height = mDrawRect.height();
            float centerX = mDrawRect.centerX();
            float centerY = mDrawRect.centerY();

            mPath.reset();
            left = mDrawRect.left + width / 4;
            mPath.moveTo(left, centerY);
            left = mDrawRect.left + width * 2 / 5;
            top = mDrawRect.top + height * 3 / 4;
            mPath.lineTo(left, top);
            left = mDrawRect.left + width * 3 / 4;
            top = mDrawRect.top + height / 4;
            mPath.lineTo(left, top);

            float textSize = width / 10;
            if (textSize < 1) {
                textSize = 1;
            }

            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(textSize);
            canvas.drawPath(mPath, mPaint);
        }
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }
}
