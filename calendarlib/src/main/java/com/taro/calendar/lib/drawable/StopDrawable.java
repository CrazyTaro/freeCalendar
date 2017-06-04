package com.taro.calendar.lib.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.taro.calendar.lib.Constant;

/**
 * Created by taro on 2017/6/2.
 */

public class StopDrawable extends Drawable {
    private Paint mPaint;
    private RectF mDrawRect;
    private Path mPath;
    private int mStorkeColor;
    private int mFillColor;
    private float mStorkeWidth;
    private float mDrawX, mDrawY;

    public StopDrawable() {
        this(Constant.DEFAULT_BACKGROUND_COLOR_BLUE, Color.TRANSPARENT, 2);
    }

    public StopDrawable(@ColorInt int storkeColor, @ColorInt int fillColor,
                        float storkeWidth) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mDrawRect = new RectF();
        mPath = new Path();

        mStorkeColor = storkeColor;
        mFillColor = fillColor;
        mStorkeWidth = storkeWidth;
    }

    public StopDrawable setStorkeWidth(float width) {
        mStorkeWidth = width;
        return this;
    }

    public StopDrawable setStorkeColor(@ColorInt int color) {
        mStorkeColor = color;
        return this;
    }

    public StopDrawable setFillColor(@ColorInt int color) {
        mFillColor = color;
        return this;
    }

    public StopDrawable setDrawXY(float x, float y) {
        mDrawX = x;
        mDrawY = y;
        return this;
    }

    @Override
    public int getIntrinsicHeight() {
        return 16;
    }

    @Override
    public int getIntrinsicWidth() {
        return 16;
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bound = getBounds();
        if (bound.width() > 0 && bound.height() > 0) {
            mDrawRect.set(bound);
            mDrawRect.offset(mDrawX, mDrawY);

            mPaint.setStrokeWidth(mStorkeWidth);
            mPaint.setColor(mStorkeColor);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(mDrawRect, 0, 360, false, mPaint);

            mPath.addArc(mDrawRect, 0, 360);
            int sc = canvas.save();
            canvas.clipPath(mPath);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawLine(mDrawRect.left, mDrawRect.top, mDrawRect.right, mDrawRect.bottom, mPaint);
            canvas.restoreToCount(sc);

//            int sc = canvas.saveLayer(mDrawRect, null, Canvas.ALL_SAVE_FLAG);
//            mPaint.setStyle(Paint.Style.FILL);
//            mPaint.setColor(mStorkeColor);
//            canvas.drawLine(mDrawRect.left, mDrawRect.top, mDrawRect.right, mDrawRect.bottom, mPaint);
//
//            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//            mPaint.setColor(Color.WHITE);
//            canvas.drawArc(mDrawRect, 0, 360, false, mPaint);
//            mPaint.setXfermode(null);
//            canvas.restoreToCount(sc);
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
