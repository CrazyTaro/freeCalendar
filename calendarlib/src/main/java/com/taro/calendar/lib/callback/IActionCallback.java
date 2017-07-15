package com.taro.calendar.lib.callback;

/**
 * 日历行为操作回调接口
 * Created by taro on 2017/6/23.
 */

public interface IActionCallback {
    /**
     * 当选中日期改变时该方法会回调(不管只有日期还是全部)
     *
     * @param oldYear  旧的年份
     * @param oldMonth 旧的月份
     * @param oldDay   旧的日期
     * @param year     当前选中的年份
     * @param month    当前选中的月份
     * @param day      当前选中的日期
     */
    void onSelectedDayChanged(int oldYear, int oldMonth, int oldDay,
                              int year, int month, int day);

    /**
     * 当日期被重置为今天时回调
     *
     * @param nowYear  今天的年份
     * @param nowMonth 今天的月份
     * @param nowDay   今天的日期
     */
    void onResetToToday(int nowYear, int nowMonth, int nowDay);
}
