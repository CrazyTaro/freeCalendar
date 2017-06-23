package com.taro.calendar.lib.callback;

/**
 * Created by taro on 2017/6/23.
 */

public interface IActionCallback {
    void onSelectedDayChanged(int oldYear, int oldMonth, int oldDay,
                              int year, int month, int day);

    void onResetToToday(int nowYear, int nowMonth, int nowDay);
}
