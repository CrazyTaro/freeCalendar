package com.taro.calendar.lib;

/**
 * Created by taro on 2017/6/1.
 */

public class DayCell {
    public static final int MASK_DATE_HOLIDAY = 0b0001;
    public static final int MASK_DATE_WORK = 0b0010;
    public static final int MASK_DATE_DRAWABLE = 0b0100;

    //年
    private int year;
    //月份
    private int month;
    // 天
    private int day;
    // 农历节日
    private String lunarFestival;
    //农历日期
    private String lunarDate = null;
    // 是否今天
    private boolean isToday = false;
    //公历节日
    private String solarFestival = null;
    //节气
    private String solarTermFestival = null;
    // 0没有，1假，2班
    private int jbj = 0;
    // 是否周末
    private boolean isWeekend = false;
    // 是否是节日
    private boolean isHoliday = false;

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setIsHoliday(boolean isHz) {
        this.isHoliday = isHz;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getLunarDate() {
        return lunarDate;
    }

    public void setLunarDate(String lunar) {
        lunarDate = lunar;
    }

    public String getLunarFestival() {
        return lunarFestival;
    }

    public void setLunarFestival(String lunarFestival) {
        this.lunarFestival = lunarFestival;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setIsToday(boolean boolT) {
        this.isToday = boolT;
    }

    public String getSolarFestival() {
        return solarFestival;
    }

    public void setSolarFestival(String solarFestival) {
        this.solarFestival = solarFestival;
    }

    public String getSolarTermFestival() {
        return solarTermFestival;
    }

    public void setSolarTermFestival(String solarTerm) {
        solarTermFestival = solarTerm;
    }

    public boolean isWeekend() {
        return isWeekend;
    }

    public void setIsWeekend(boolean boolW) {
        this.isWeekend = boolW;
    }

    public boolean isSpecialDate(int mask) {
        return (mask & jbj) != 0;
    }

    public void setSpecialDate(int mask, boolean isTrue) {
        if (isTrue) {
            this.jbj |= mask;
        } else {
            this.jbj &= ~mask;
        }
    }
}
