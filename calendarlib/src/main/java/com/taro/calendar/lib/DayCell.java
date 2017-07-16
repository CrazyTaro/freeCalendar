package com.taro.calendar.lib;

/**
 * Created by taro on 2017/6/1.
 */

public class DayCell {
    /**
     * 假期标识
     */
    public static final int MASK_DATE_HOLIDAY = 0b0001;
    /**
     * 加班标识
     */
    public static final int MASK_DATE_WORK = 0b0010;
    /**
     * 显示底层图标标识
     */
    public static final int MASK_DATE_BOTTOM_DRAWABLE = 0b0100;

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
    //公历节日
    private String solarFestival = null;
    //节气
    private String solarTermFestival = null;
    // 0没有，1假，2班
    private int specialDateStatus = 0;
    // 是否周末
    private boolean isWeekend = false;
    // 是否是节日
    private boolean isHoliday = false;

    /**
     * 重置所有数据
     */
    public void reset() {
        this.year = 0;
        this.month = 0;
        this.day = 0;
        this.lunarFestival = null;
        this.solarFestival = null;
        this.solarTermFestival = null;
        this.lunarDate = null;
        this.specialDateStatus = 0;
        this.isHoliday = false;
        this.isWeekend = false;
    }

    /**
     * 是否今日
     *
     * @param todayYear  当天日期年份
     * @param todayMonth 当天日期月份,月份需要注意为实际月份-1
     * @param todayDate  当天日期天数
     * @return
     */
    public boolean isToday(int todayYear, int todayMonth, int todayDate) {
        return year == todayYear && month == todayMonth && day == todayDate;
    }

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
        return (mask & specialDateStatus) != 0;
    }

    /**
     * 设置当前是否为特殊的日期
     *
     * @param mask   日期flag字段
     * @param isTrue true为设置该特殊日期,false为取消该特殊日期
     */
    public void setSpecialDate(int mask, boolean isTrue) {
        if (isTrue) {
            this.specialDateStatus |= mask;
        } else {
            this.specialDateStatus &= ~mask;
        }
    }

    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d %s %s %s %s ", year, month, day, lunarDate, lunarFestival, solarFestival, solarTermFestival);
    }
}
