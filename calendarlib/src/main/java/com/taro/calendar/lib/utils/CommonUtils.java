package com.taro.calendar.lib.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;

/**
 * Created by taro on 2017/6/1.
 */

public class CommonUtils {
    public static float convertDp2Px(@NonNull Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


    /**
     * 判断是否为null或空值
     *
     * @param str
     *            String
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

}
