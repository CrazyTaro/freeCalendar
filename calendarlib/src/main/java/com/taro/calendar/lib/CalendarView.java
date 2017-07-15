package com.taro.calendar.lib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.taro.calendar.lib.callback.IDrawCallback;


/**
 * Created by taro on 2017/6/1.
 */

public class CalendarView extends AbsCalendarView {

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @NonNull
    @Override
    protected IDrawCallback createCallback() {
        return new BaseCalendarDrawHelper();
    }

    @Override
    public CalendarView setDrawCallback(@NonNull IDrawCallback callback) {
        return (CalendarView) super.setDrawCallback(callback);
    }

    @Override
    public IDrawCallback getDrawCallback() {
        return super.getDrawCallback();
    }
}
