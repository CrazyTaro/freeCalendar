package com.taro.calendar.lib.utils;

/**
 * Created by taro on 2017/6/4.
 */

public class DateUtils {
    private static int[] mDayMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public static int getNumDayOfMonth(int year, int month) {
        if (month < 0 || month > 11) {
            return 0;
        }
        int i = mDayMonth[month];
        if ((isLeapYear(year)) && (month == 1)) {
            i++;
        }
        return i;
    }

    public static boolean isLeapYear(int year) {
        if (year % 400 == 0) {
            return true;
        }
        return (year % 4 == 0) && (year % 100 != 0);
    }
}
