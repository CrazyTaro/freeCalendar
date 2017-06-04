package com.taro.calendar.lib;

import android.graphics.Color;

/**
 * Created by taro on 2017/6/4.
 */

public class ColorSetting {
    /**
     * 默认次要字体颜色
     */
    public static final int DEFAULT_TEXT_COLOR_MINOR = Color.parseColor("#33000000");
    /**
     * 默认农历字体颜色
     */
    public static final int DEFAULT_TEXT_COLOR_LUNAR = Color.parseColor("#717171");
    /**
     * 默认周末字体颜色
     */
    public static final int DEFAULT_TEXT_COLOR_WEEKEND = Color.parseColor("#F06F28");
    /**
     * 默认节日字体颜色
     */
    public static final int DEFAULT_TEXT_COLOR_FESTIVAL = DEFAULT_TEXT_COLOR_WEEKEND;
    /**
     * 默认普通字体颜色
     */
    public static final int DEFAULT_TEXT_COLOR_NORMAL = Color.BLACK;
    /**
     * 默认选中字体颜色
     */
    public static final int DEFAULT_TEXT_COLOR_SELECTED_DAY = Color.WHITE;
    /**
     * 默认选中当天背景色
     */
    public static final int DEFAULT_BACKGROUND_SELECTED_DAY = Color.parseColor("#04A3E3");
    /**
     * 默认日历控件的背景色
     */
    public static final int DEFAULT_BACKGROUND_CALENDAR = Color.WHITE;

    /**
     * 星期说明标题栏的字体颜色
     */
    public int mWeekTitleTextColor;
    /**
     * 周末字体颜色
     */
    public int mWeekendTextColor;
    /**
     * 节日字体颜色
     */
    public int mFestivalTextColor;
    /**
     * 农历字体颜色
     */
    public int mLunarTextColor;
    /**
     * 默认普通字体颜色
     */
    public int mNormalDateTextColor;
    /**
     * 次要文字颜色(次要月份等中使用)
     */
    public int mMinorDateTextColor;
    /**
     * 选中日期的背景色
     */
    public int mSelectDateTextColor;
    /**
     * 控件背景
     */
    public int mBackgroundColor;
    /**
     * 选中日期的背景色
     */
    public int mSelectDateBackgroundColor;

    public ColorSetting() {
        reset();
    }

    public ColorSetting(int festival, int lunar, int selected, int selectedBackground) {
        this();
        mFestivalTextColor = festival;
        mLunarTextColor = lunar;
        mSelectDateTextColor = selected;
        mSelectDateBackgroundColor = selectedBackground;
    }

    public ColorSetting(int normal, int minor, int weekend, int festival, int lunar, int selected) {
        this();
        mNormalDateTextColor = normal;
        mMinorDateTextColor = minor;
        mWeekendTextColor = weekend;
        mFestivalTextColor = festival;
        mLunarTextColor = lunar;
        mSelectDateTextColor = selected;
    }

    public ColorSetting(int normal, int minor, int weekTitle, int weekend,
                        int festival, int lunar, int selected, int selectedBackground, int background) {
        init(normal, minor, weekTitle, weekend, festival, lunar, selected, selectedBackground, background);
    }

    private void init(int normal, int minor, int weekTitle, int weekend,
                      int festival, int lunar, int selected, int selectedBackground, int background) {
        mWeekTitleTextColor = weekTitle;
        mWeekendTextColor = weekend;
        mFestivalTextColor = festival;
        mLunarTextColor = lunar;
        mNormalDateTextColor = normal;
        mMinorDateTextColor = minor;
        mSelectDateTextColor = selected;
        mBackgroundColor = background;
        mSelectDateBackgroundColor = selectedBackground;
    }

    public ColorSetting reset() {
        init(DEFAULT_TEXT_COLOR_NORMAL, DEFAULT_TEXT_COLOR_MINOR, DEFAULT_TEXT_COLOR_LUNAR, DEFAULT_TEXT_COLOR_WEEKEND,
                DEFAULT_TEXT_COLOR_FESTIVAL, DEFAULT_TEXT_COLOR_LUNAR, DEFAULT_TEXT_COLOR_SELECTED_DAY,
                DEFAULT_BACKGROUND_SELECTED_DAY, DEFAULT_BACKGROUND_CALENDAR);
        return this;
    }

    public ColorSetting setNormalDateTextColor(int color) {
        mNormalDateTextColor = color;
        return this;
    }

    public ColorSetting setMinorDateTextColor(int color) {
        mMinorDateTextColor = color;
        return this;
    }

    public ColorSetting setWeekTitleTextColor(int color) {
        mWeekTitleTextColor = color;
        return this;
    }

    public ColorSetting setWeekendTextColor(int color) {
        mWeekendTextColor = color;
        return this;
    }

    public ColorSetting setFestivalTextColor(int color) {
        mFestivalTextColor = color;
        return this;
    }

    public ColorSetting setLunarTextColor(int color) {
        mLunarTextColor = color;
        return this;
    }

    public ColorSetting setSelectedDateTextColor(int color) {
        mSelectDateTextColor = color;
        return this;
    }

    public ColorSetting setSelectedDateBackgroundColor(int color) {
        mSelectDateBackgroundColor = color;
        return this;
    }

    public ColorSetting setBackgroundColor(int color) {
        mBackgroundColor = color;
        return this;
    }
}
