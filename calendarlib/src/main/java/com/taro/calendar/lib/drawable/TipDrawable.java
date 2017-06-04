package com.taro.calendar.lib.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
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
 * Created by taro on 2017/6/1.
 */
public class TipDrawable extends Drawable {
    private Paint mPaint;
    private String mText;
    private float mTextSize;
    private int mTextColor;
    private int mBgColor;
    private RectF mDrawRect;
    private int mOldBoundWidth;
    private int mOldBoundHeight;

    private float mDrawX;
    private float mDrawY;

    public TipDrawable() {
        this(Color.WHITE, Constant.DEFAULT_BACKGROUND_COLOR_BLUE, "假");
    }

    public TipDrawable(@ColorInt int textColor, @ColorInt int bgColor, @Nullable String text) {
        mText = text;
        mTextColor = textColor;
        mBgColor = bgColor;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mDrawRect = new RectF();

        setBounds(0, 0, 16, 16);
    }

    public TipDrawable setDrawXY(float x, float y) {
        mDrawX = x;
        mDrawY = y;
        return this;
    }

    public TipDrawable setText(@Nullable String text) {
        mText = text;
        onBoundsChange(getBounds());
        return this;
    }

    public TipDrawable setTextColor(@ColorInt int color) {
        mTextColor = color;
        return this;
    }

    @Override
    public int getIntrinsicWidth() {
        return 16;
    }

    @Override
    public int getIntrinsicHeight() {
        return 16;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        mOldBoundWidth = getBounds().width();
        mOldBoundHeight = getBounds().height();
        super.setBounds(left, top, right, bottom);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        int width = bounds.width();
        int height = bounds.height();
        if (width == 0 || height == 0
                || (mOldBoundWidth == width && mOldBoundHeight == height)) {
            return;
        } else if (mText != null && mText.length() > 0) {
            mTextSize = width * 5f / (7 * mText.length());
            if (mTextSize > height) {
                mTextSize = height * 5f / 7;
            }
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bound = getBounds();
        if (bound.width() > 0 && bound.height() > 0) {
            return;
        } else {
            mDrawRect.set(bound);
            mDrawRect.offset(mDrawX, mDrawY);
            mPaint.setColor(mBgColor);
            canvas.drawArc(mDrawRect, 0, 360, true, mPaint);

            if (mText != null && mText.length() > 0) {
                if (mTextSize == 0) {
                    onBoundsChange(getBounds());
                }
                mPaint.setColor(mTextColor);
                mPaint.setTextSize(mTextSize);
                Paint.FontMetrics fm = mPaint.getFontMetrics();
                float length = mPaint.measureText(mText);
                float x = (bound.width() - length) / 2;
                float y = (bound.height() / 2) + (fm.bottom - fm.top) / 2 - fm.bottom;
                canvas.drawText(mText, mDrawX + x, mDrawY + y, mPaint);
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
