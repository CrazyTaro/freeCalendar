package com.taro.calendar.lib;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.taro.calendar.lib.callback.IDrawCallback;

/**
 * Created by taro on 2017/6/23.
 */

public class CalendarDrawHelper implements IDrawCallback {

    @NonNull
    @Override
    public DayCell createDayCell() {
        return new DayCell();
    }

    @Override
    public void updateDayCellAfterNewSetting(@NonNull DayCell cell) {

    }

    @Override
    public void beforeCellDraw(@NonNull RectF drawArea, @NonNull ColorSetting color, @NonNull DayCell cell, float minSize, Canvas canvas, Paint paint) {
        //可以在这里缓存可能需要用到的数据,此部分的参数会在整个cell绘制过程不进行任何改变
        //也可以在此处进行一些提前的绘制操作
    }

    @Override
    public void afterCellDraw(Canvas canvas, Paint paint) {
        //所有绘制工作结束会回调,如果需要补充其它的绘制操作,可以在此处绘制
    }

    @Override
    public void drawWeekTitleBackground(Canvas canvas, @NonNull RectF drawArea, Paint paint) {
        canvas.drawRect(drawArea, paint);
    }

    @Override
    public void drawWeekTitle(Canvas canvas, @NonNull RectF drawArea, @AbsCalendarView.Week int weekTag, float textX, float textY, float textSize, @NonNull String week, Paint paint) {
        canvas.drawText(week, textX, textY, paint);
    }

    @Override
    public void drawDateBackground(Canvas canvas, @NonNull RectF recommendRectf, boolean isToday, boolean isSelected, Paint paint) {
        canvas.drawRect(recommendRectf, paint);
    }

    @Override
    public void drawSelectedDayBackground(Canvas canvas, @NonNull RectF recommendRectf, @Nullable Drawable drawable, boolean isToday, boolean isHasDrawable, Paint paint) {
        if (drawable != null) {
            drawable.setBounds((int) recommendRectf.left, (int) recommendRectf.top,
                    (int) recommendRectf.right, (int) recommendRectf.bottom);
            drawable.draw(canvas);
        } else {
            canvas.drawArc(recommendRectf, 0, 360, false, paint);
        }
    }

    @Override
    public void drawTodayBackground(Canvas canvas, @NonNull RectF recommendRectf, @Nullable Drawable drawable, boolean isHasDrawable, Paint paint) {
        if (drawable != null) {
            drawable.setBounds((int) recommendRectf.left, (int) recommendRectf.top,
                    (int) recommendRectf.right, (int) recommendRectf.bottom);
            drawable.draw(canvas);
        } else {
            canvas.drawArc(recommendRectf, 0, 360, false, paint);
        }
    }

    @Override
    public void drawDateText(Canvas canvas, boolean isToday, boolean isSelected, int color, float textSize, float x, float y, @NonNull String date, Paint paint) {
        canvas.drawText(date, x, y, paint);
    }

    @Override
    public boolean isNeedDrawBottomDrawable(@NonNull DayCell cell) {
        return cell.isSpecialDate(DayCell.MASK_DATE_BOTTOM_DRAWABLE);
    }

    @Override
    public void drawBottomDrawable(Canvas canvas, boolean isToday, boolean isSelected, @Nullable Bitmap bmp, @Nullable Drawable drawable, @NonNull RectF recommendRectf, Paint paint) {
        if (bmp != null && !bmp.isRecycled()) {
            canvas.drawBitmap(bmp, null, recommendRectf, paint);
        } else if (drawable != null) {
            drawable.setBounds((int) recommendRectf.left, (int) recommendRectf.top,
                    (int) recommendRectf.right, (int) recommendRectf.bottom);
            drawable.draw(canvas);
        }
    }

    @Override
    public void drawFestivalOrLunarDate(Canvas canvas, boolean isToday, boolean isSelected, boolean isLunar, int color, float textSize, float x, float y, @NonNull String festivalOrDate, Paint paint) {
        canvas.drawText(festivalOrDate, x, y, paint);
    }

    @Override
    public void drawHolidayDate(Canvas canvas, boolean isToday, boolean isSelected, @Nullable Drawable drawable, @NonNull RectF recommendRectf, Paint paint) {
        if (drawable != null) {
            drawable.setBounds((int) recommendRectf.left, (int) recommendRectf.top,
                    (int) recommendRectf.right, (int) recommendRectf.bottom);
            drawable.draw(canvas);
        }
    }

    @Override
    public void drawWorkDate(Canvas canvas, boolean isToday, boolean isSelected, @Nullable Drawable drawable, @NonNull RectF recommendRectf, Paint paint) {
        if (drawable != null) {
            drawable.setBounds((int) recommendRectf.left, (int) recommendRectf.top,
                    (int) recommendRectf.right, (int) recommendRectf.bottom);
            drawable.draw(canvas);
        }
    }
}
