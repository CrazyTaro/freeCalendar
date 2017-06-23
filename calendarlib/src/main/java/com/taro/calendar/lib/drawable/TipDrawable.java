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

import com.taro.calendar.lib.ColorSetting;


/**
 * Created by taro on 2017/6/1.
 */
public class TipDrawable extends Drawable {
    private Paint mPaint;
    protected String mText;
    protected float mTextSize;
    protected int mTextColor;
    protected int mBgColor;
    private int mOldBoundWidth;
    private int mOldBoundHeight;
    private RectF mDrawRectf;

    /**
     * 创建一个TipDrawable,创建后可以不手动设置{@link #setBounds(int, int, int, int)},因为会默认调用<br>
     * 默认颜色为白色,背景色为蓝色,文本为中文文本(一般都需要重新设置文本的)
     */
    public TipDrawable() {
        this(Color.WHITE, ColorSetting.DEFAULT_COLOR_BLUE, "假");
    }

    /**
     * 创建一个TipDrawable,创建后可以不手动设置{@link #setBounds(int, int, int, int)},因为会默认调用
     *
     * @param textColor 文本颜色
     * @param bgColor   背景色
     * @param text      文本内容,不建议使用很长的文本,文本仅会显示一行
     */
    public TipDrawable(@ColorInt int textColor, @ColorInt int bgColor, @Nullable String text) {
        mText = text;
        mTextColor = textColor;
        mBgColor = bgColor;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mDrawRectf = new RectF();

        setBounds(0, 0, 16, 16);
    }

    /**
     * 设置绘制的XY
     *
     * @param x
     * @param y
     * @return
     */
    public TipDrawable updateDrawXY(float x, float y) {
        getBounds().offset((int) x, (int) y);
        return this;
    }

    /**
     * 设置文本
     *
     * @param text
     * @return
     */
    public TipDrawable setText(@Nullable String text) {
        mText = text;
        Rect rect = getBounds();
        computeTextSize(rect.width(), rect.height());
        return this;
    }

    /**
     * 设置文本的颜色
     *
     * @param color
     * @return
     */
    public TipDrawable setTextColor(@ColorInt int color) {
        mTextColor = color;
        return this;
    }

    /**
     * 是否改变文本当语言改变时
     *
     * @param isEnglish
     * @return
     */
    public void updateTextWhenLanguageChanged(boolean isEnglish, @NonNull String text) {
        setText(text);
    }

    //计算文本字体,当文本改动和图标大小改变时都应该重新计算
    private void computeTextSize(int width, int height) {
        if (mText != null && mText.length() > 0) {
            mTextSize = width * 5f / (7 * mText.length());
            if (mTextSize > height) {
                mTextSize = height * 5f / 7;
            }
        }
    }

    /**
     * 绘制图标背景
     *
     * @param canvas
     * @param drawArea 图标绘制区域
     * @param paint    已经设置好背景颜色
     */
    protected void drawBackground(Canvas canvas, @NonNull RectF drawArea, Paint paint) {
        canvas.drawArc(drawArea,
                0, 360, true, paint);
    }

    /**
     * 绘制文本,仅在文本存在和有效时会被调用
     *
     * @param canvas
     * @param drawArea   整个图标的绘制区域
     * @param text       绘制文本
     * @param recommendX 推荐绘制X坐标,此为文本的中心X坐标(centerX)
     * @param recommendY 推荐绘制Y坐标,此为文本的中心Y坐标
     * @param paint      已经设置好文本字体大小及颜色
     */
    protected void drawText(Canvas canvas, @NonNull RectF drawArea, @NonNull String text, float recommendX, float recommendY, Paint paint) {
        canvas.drawText(text, recommendX, recommendY, paint);
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
        }
        computeTextSize(width, height);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bound = getBounds();
        if (bound.width() > 0 && bound.height() > 0) {
            mPaint.setColor(mBgColor);
            mDrawRectf.set(bound);
            drawBackground(canvas, mDrawRectf, mPaint);

            if (mText != null && mText.length() > 0) {
                if (mTextSize == 0) {
                    computeTextSize(bound.width(), bound.height());
                }
                mPaint.setColor(mTextColor);
                mPaint.setTextSize(mTextSize);
                Paint.FontMetrics fm = mPaint.getFontMetrics();
                float length = mPaint.measureText(mText);
                float x = (bound.width() - length) / 2;
                float y = (bound.height() / 2) + (fm.bottom - fm.top) / 2 - fm.bottom;
                drawText(canvas, mDrawRectf, mText, x, y, mPaint);
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
