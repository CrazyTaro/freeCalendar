package com.taro.calendar.lib.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.taro.calendar.lib.ColorSetting;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by taro on 2017/6/2.
 */

public class CircleUsefulDrawable extends Drawable {
    public static final int DIRECTION_NONE = 0;
    public static final int DIRECTION_CENTER_X = 1;
    public static final int DIRECTION_CENTER_Y = 2;
    public static final int DIRECTION_DIAGONAL_LEFT_TOP_RIGHT_BOTTOM = 3;
    public static final int DIRECTION_DIAGONAL_RIGHT_TOP_LEFT_BOTTOM = 4;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {DIRECTION_NONE, DIRECTION_CENTER_X, DIRECTION_CENTER_Y,
            DIRECTION_DIAGONAL_LEFT_TOP_RIGHT_BOTTOM, DIRECTION_DIAGONAL_RIGHT_TOP_LEFT_BOTTOM})
    public @interface DividerDirection {
    }

    private Paint mPaint;
    private Path mPath;
    private RectF mDrawRect;

    private int mStrokeColor;
    private int mFillColor;
    private float mStrokeWidth;
    private int mFillStartAngle;
    private int mFillSweepAngle;
    private int mStrokeStartAngle;
    private int mStrokeSweepAngle;

    private boolean mIsDrawDivider = false;
    private int mDividerDirection;
    private int mDividerStartAngle;

    public CircleUsefulDrawable() {
        this(ColorSetting.DEFAULT_COLOR_BLUE, ColorSetting.DEFAULT_COLOR_BLUE,
                2, 0, 360, -90, 135, -1, -1);
    }

    public CircleUsefulDrawable(@ColorInt int storkeColor, @ColorInt int fillColor, float strokeWidth,
                                int filStartAngle, @IntRange(from = 0, to = 360) int fillSweepAngle,
                                int strokeStartAngle, @IntRange(from = 0, to = 360) int strokeSweepAngle,
                                int strokeAnglePercent, int fillAnglePercent) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mDrawRect = new RectF();

        mStrokeColor = storkeColor;
        mFillColor = fillColor;
        mStrokeWidth = strokeWidth;
        mStrokeStartAngle = strokeStartAngle;
        mFillStartAngle = filStartAngle;

        //备用,旋转角度只能是0-360之间
        mStrokeSweepAngle = strokeSweepAngle;
        mFillSweepAngle = fillSweepAngle;

        mIsDrawDivider = false;
        mDividerDirection = DIRECTION_NONE;
        mDividerStartAngle = Integer.MIN_VALUE;

        setStrokeAnglePercent(strokeAnglePercent);
        setFillAnglePercent(fillAnglePercent);

        setBounds(0, 0, getIntrinsicWidth(), getIntrinsicHeight());
    }

    public CircleUsefulDrawable setDividerDirection(@DividerDirection int direction) {
        mDividerDirection = direction;
        return this;
    }

    public CircleUsefulDrawable setDividerStartAngle(int angle) {
        mDividerStartAngle = angle;
        return this;
    }

    public CircleUsefulDrawable setFillStartAngle(int angle) {
        mFillStartAngle = angle;
        return this;
    }

    public CircleUsefulDrawable setFillSweepAngle(@IntRange(from = 0, to = 360) int angle) {
        mFillStartAngle = angle;
        return this;
    }

    public CircleUsefulDrawable setStrokeStartAngle(int angle) {
        mStrokeStartAngle = angle;
        return this;
    }

    public CircleUsefulDrawable setStrokeSweepAngle(@IntRange(from = 0, to = 360) int angle) {
        mStrokeSweepAngle = angle;
        return this;
    }

    public CircleUsefulDrawable setFillAnglePercent(int percent) {
        if (percent >= 0 && percent <= 100) {
            mFillSweepAngle = 360 * percent / 100;
        }
        return this;
    }

    public CircleUsefulDrawable setStrokeAnglePercent(int percent) {
        if (percent >= 0 && percent <= 100) {
            mStrokeSweepAngle = 360 * percent / 100;
        }
        return this;
    }

    public CircleUsefulDrawable setIsDrawDivider(boolean isDraw) {
        mIsDrawDivider = isDraw;
        return this;
    }

    public CircleUsefulDrawable setStorkeWidth(float width) {
        mStrokeWidth = width;
        return this;
    }

    public CircleUsefulDrawable setStorkeColor(@ColorInt int color) {
        mStrokeColor = color;
        return this;
    }

    public CircleUsefulDrawable setFillColor(@ColorInt int color) {
        mFillColor = color;
        return this;
    }

    public CircleUsefulDrawable setOffsetXY(int x, int y) {
        getBounds().offset(x, y);
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

            mPaint.setColor(mFillColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawArc(mDrawRect, mStrokeStartAngle, mStrokeSweepAngle, true, mPaint);

            mPaint.setColor(mStrokeColor);
            mPaint.setStrokeWidth(mStrokeWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(mDrawRect, mFillStartAngle, mFillSweepAngle, false, mPaint);

            if (mDividerDirection != DIRECTION_NONE) {
                if (mPath == null) {
                    mPath = new Path();
                }
                mPath.reset();
                mPath.addArc(mDrawRect, 0, 360);
                int sc = canvas.save();
                canvas.clipPath(mPath);
                mPaint.setStyle(Paint.Style.FILL);
                switch (mDividerDirection) {
                    case DIRECTION_CENTER_X:
                        canvas.drawLine(mDrawRect.centerX(), 0, mDrawRect.centerX(), mDrawRect.height(), mPaint);
                        break;
                    case DIRECTION_CENTER_Y:
                        canvas.drawLine(0, mDrawRect.centerY(), mDrawRect.width(), mDrawRect.centerY(), mPaint);
                        break;
                    case DIRECTION_DIAGONAL_LEFT_TOP_RIGHT_BOTTOM:
                        canvas.drawLine(mDrawRect.left, mDrawRect.top, mDrawRect.right, mDrawRect.bottom, mPaint);
                        break;
                    case DIRECTION_DIAGONAL_RIGHT_TOP_LEFT_BOTTOM:
                        canvas.drawLine(mDrawRect.right, mDrawRect.top, mDrawRect.left, mDrawRect.bottom, mPaint);
                        break;
                }
                canvas.restoreToCount(sc);
            }
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
