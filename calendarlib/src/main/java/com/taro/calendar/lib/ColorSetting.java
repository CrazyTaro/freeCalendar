package com.taro.calendar.lib;

import android.graphics.Color;
import android.support.annotation.ColorInt;

/**
 * Created by taro on 2017/6/4.
 */

public class ColorSetting {
    /**
     * 默认的黄色
     */
    public static final int DEFAULT_COLOR_YELLOW = Color.parseColor("#fdc183");
    /**
     * 默认的绿色
     */
    public static final int DEFAULT_COLOR_GREEN = Color.parseColor("#90c41f");
    /**
     * 默认的蓝色
     */
    public static final int DEFAULT_COLOR_BLUE = Color.parseColor("#04A3E3");
    /**
     * 默认的红色
     */
    public static final int DEFAULT_COLOR_RED = Color.parseColor("#F06F28");

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
    public static final int DEFAULT_BACKGROUND_SELECTED_DAY = DEFAULT_COLOR_BLUE;
    /**
     * 默认日历控件的背景色
     */
    public static final int DEFAULT_BACKGROUND_CALENDAR = Color.WHITE;

    /**
     * 星期说明标题栏的字体颜色
     */
    public int mWeekTitleTextColor;
    /**
     * 星期说明标题栏的背景色
     */
    public int mWeekTitleBackgroundColor;
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
     * 选中日期的字体色
     */
    public int mSelectDateTextColor;
    /**
     * 今天字体颜色
     */
    public int mTodayDateTextColor;
    /**
     * 控件背景
     */
    public int mBackgroundColor;
    /**
     * 每一个日期的背景色
     */
    public int mDateBackgroundColor;
    /**
     * 选中日期的背景色
     */
    public int mSelectDateBackgroundColor;
    /**
     * 今天日期的背景色
     */
    public int mTodayBackgroundColor;

    public ColorSetting() {
        reset();
    }

    public ColorSetting(@ColorInt int festival, @ColorInt int lunar, @ColorInt int selected, @ColorInt int selectedBackground) {
        this();
        mFestivalTextColor = festival;
        mLunarTextColor = lunar;
        mSelectDateTextColor = selected;
        mSelectDateBackgroundColor = selectedBackground;
    }

    public ColorSetting(@ColorInt int normal, @ColorInt int minor, @ColorInt int weekend, @ColorInt int festival, @ColorInt int lunar, @ColorInt int selected) {
        this();
        mNormalDateTextColor = normal;
        mMinorDateTextColor = minor;
        mWeekendTextColor = weekend;
        mFestivalTextColor = festival;
        mLunarTextColor = lunar;
        mSelectDateTextColor = selected;
    }

    private void init(int normal, int minor, int weekTitle,
                      int weekTitleBackground, int weekend,
                      int festival, int lunar, int selected, int today,
                      int todayBackground, int selectedBackground,
                      int background, int dateBackground) {
        mWeekTitleTextColor = weekTitle;
        mWeekTitleBackgroundColor = weekTitleBackground;
        mWeekendTextColor = weekend;
        mFestivalTextColor = festival;
        mLunarTextColor = lunar;
        mNormalDateTextColor = normal;
        mMinorDateTextColor = minor;
        mSelectDateTextColor = selected;
        mTodayDateTextColor = today;
        mBackgroundColor = background;
        mDateBackgroundColor = dateBackground;
        mSelectDateBackgroundColor = selectedBackground;
        mTodayBackgroundColor = todayBackground;
    }

    public ColorSetting reset() {
        //周末标题/周末背景色/周末文本颜色
        init(DEFAULT_TEXT_COLOR_NORMAL, DEFAULT_TEXT_COLOR_MINOR, DEFAULT_TEXT_COLOR_LUNAR,
                //周末背景色/周末文本颜色
                Color.TRANSPARENT, DEFAULT_TEXT_COLOR_WEEKEND,
                //节日文本颜色/农历日历文本颜色
                DEFAULT_TEXT_COLOR_FESTIVAL, DEFAULT_TEXT_COLOR_LUNAR,
                //选中文本颜色/今日文本颜色
                DEFAULT_TEXT_COLOR_SELECTED_DAY, DEFAULT_TEXT_COLOR_NORMAL,
                //今日背景颜色/选中背景颜色
                DEFAULT_BACKGROUND_SELECTED_DAY, DEFAULT_BACKGROUND_SELECTED_DAY,
                //控件背景色/日期背景色
                DEFAULT_BACKGROUND_CALENDAR, Color.TRANSPARENT);
        return this;
    }

    /**
     * 设置默认的文本颜色
     *
     * @param color
     * @return
     */
    public ColorSetting setNormalDateTextColor(@ColorInt int color) {
        mNormalDateTextColor = color;
        return this;
    }

    /**
     * 设置次要的文本颜色
     *
     * @param color
     * @return
     */
    public ColorSetting setMinorDateTextColor(@ColorInt int color) {
        mMinorDateTextColor = color;
        return this;
    }

    /**
     * 设置周末标题文本颜色
     *
     * @param color
     * @return
     */
    public ColorSetting setWeekTitleTextColor(@ColorInt int color) {
        mWeekTitleTextColor = color;
        return this;
    }

    /**
     * 设置周末文本颜色
     *
     * @param color
     * @return
     */
    public ColorSetting setWeekendTextColor(@ColorInt int color) {
        mWeekendTextColor = color;
        return this;
    }

    /**
     * 设置节日文本颜色
     *
     * @param color
     * @return
     */
    public ColorSetting setFestivalTextColor(@ColorInt int color) {
        mFestivalTextColor = color;
        return this;
    }

    /**
     * 设置农历文本颜色
     *
     * @param color
     * @return
     */
    public ColorSetting setLunarTextColor(@ColorInt int color) {
        mLunarTextColor = color;
        return this;
    }

    /**
     * 设置选中日期文本颜色
     *
     * @param color
     * @return
     */
    public ColorSetting setSelectedDateTextColor(@ColorInt int color) {
        mSelectDateTextColor = color;
        return this;
    }

    /**
     * 设置今日文本颜色
     *
     * @param color
     * @return
     */
    public ColorSetting setTodayDateTextColor(@ColorInt int color) {
        mTodayDateTextColor = color;
        return this;
    }

    /**
     * 设置选中日期背景色
     *
     * @param color
     * @return
     */
    public ColorSetting setSelectedDateBackgroundColor(@ColorInt int color) {
        mSelectDateBackgroundColor = color;
        return this;
    }

    /**
     * 设置今日背景色
     *
     * @param color
     * @return
     */
    public ColorSetting setTodayBackgroundColor(@ColorInt int color) {
        mTodayBackgroundColor = color;
        return this;
    }

    /**
     * 设置控件背景色
     *
     * @param color
     * @return
     */
    public ColorSetting setBackgroundColor(@ColorInt int color) {
        mBackgroundColor = color;
        return this;
    }

    /**
     * 设置日期背景色
     *
     * @param color
     * @return
     */
    public ColorSetting setDateBackgroundColor(@ColorInt int color) {
        mDateBackgroundColor = color;
        return this;
    }
}
