package com.taro.calendar.lib.callback;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.taro.calendar.lib.AbsCalendarView;
import com.taro.calendar.lib.ColorSetting;
import com.taro.calendar.lib.DayCell;

/**
 * Created by taro on 2017/6/21.
 */

public interface IDrawCallback {

    @NonNull
    DayCell createDayCell();

    void updateDayCellAfterNewSetting(@NonNull DayCell cell);

    void beforeCellDraw(@NonNull RectF drawArea, @NonNull ColorSetting color, @NonNull DayCell cell, float minSize, Canvas canvas, Paint paint);

    void afterCellDraw(Canvas canvas, Paint paint);

    void drawWeekTitleBackground(Canvas canvas, @NonNull RectF drawArea, Paint paint);

    void drawWeekTitle(Canvas canvas, @NonNull RectF drawArea, @AbsCalendarView.Week int weekTag, float textX, float textY, float textSize, @NonNull String week, Paint paint);

    void drawDateBackground(Canvas canvas, @NonNull RectF recommendRectf, boolean isToday, boolean isSelected, Paint paint);

    void drawSelectedDayBackground(Canvas canvas, @NonNull RectF recommendRectf, @Nullable Drawable drawable, boolean isToday, boolean isHasDrawable, Paint paint);

    void drawTodayBackground(Canvas canvas, @NonNull RectF recommendRectf, @Nullable Drawable drawable, boolean isHasDrawable, Paint paint);

    void drawDateText(Canvas canvas, boolean isToday, boolean isSelected, int color, float textSize, float x, float y, @NonNull String date, Paint paint);

    boolean isNeedDrawBottomDrawable(@NonNull DayCell cell);

    void drawBottomDrawable(Canvas canvas, boolean isToday, boolean isSelected, @Nullable Bitmap bmp, @Nullable Drawable drawable, @NonNull RectF recommendRectf, Paint paint);

    void drawFestivalOrLunarDate(Canvas canvas, boolean isToday, boolean isSelected, boolean isLunar, int color, float textSize, float x, float y, @NonNull String festivalOrDate, Paint paint);

    void drawHolidayDate(Canvas canvas, boolean isToday, boolean isSelected, @Nullable Drawable drawable, @NonNull RectF recommendRectf, Paint paint);

    void drawWorkDate(Canvas canvas, boolean isToday, boolean isSelected, @Nullable Drawable drawable, @NonNull RectF recommendRectf, Paint paint);

}
