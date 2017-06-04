package com.taro.calendar.lib.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
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

public class CircleUsefulDrawable extends Drawable {
    private Paint mPaint;
    private RectF mDrawRect;
    private int mStorkeColor;
    private int mFillColor;
    private float mStorkeWidth;
    private int mStartAngle;
    private int mSweepAngle;
    private int mPercent;
    private float mDrawX, mDrawY;

    public CircleUsefulDrawable() {
        this(Constant.DEFAULT_BACKGROUND_COLOR_BLUE, Constant.DEFAULT_BACKGROUND_COLOR_BLUE,
                2, -90, 135, -1);
    }

    public CircleUsefulDrawable(@ColorInt int storkeColor, @ColorInt int fillColor,
                                float storkeWidth, int startAngle, int sweepAngle, int percent) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mDrawRect = new RectF();

        mStorkeColor = storkeColor;
        mFillColor = fillColor;
        mStorkeWidth = storkeWidth;
        mStartAngle = startAngle;
        setSweepAngle(sweepAngle);
        setPercent(percent);
    }

    public CircleUsefulDrawable setStartAngle(int angle) {
        mStartAngle = angle;
        return this;
    }

    public CircleUsefulDrawable setSweepAngle(@IntRange(from = 0) int angle) {
        if (angle >= 0) {
            if (angle > 360) {
                angle = 360;
            }
            mSweepAngle = angle;
        }
        return this;
    }

    public CircleUsefulDrawable setPercent(int percent) {
        if (percent >= 0 && mPercent <= 100) {
            mSweepAngle = 360 * mPercent / 100;
            mPercent = percent;
        }
        return this;
    }

    public CircleUsefulDrawable setStorkeWidth(float width) {
        mStorkeWidth = width;
        return this;
    }

    public CircleUsefulDrawable setStorkeColor(@ColorInt int color) {
        mStorkeColor = color;
        return this;
    }

    public CircleUsefulDrawable setFillColor(@ColorInt int color) {
        mFillColor = color;
        return this;
    }

    public CircleUsefulDrawable setDrawXY(float x, float y) {
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

            mPaint.setColor(mFillColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawArc(mDrawRect, mStartAngle, mSweepAngle, true, mPaint);

            mPaint.setColor(mStorkeColor);
            mPaint.setStrokeWidth(mStorkeWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(mDrawRect, mStartAngle, 360, true, mPaint);
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
