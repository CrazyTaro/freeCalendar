package com.taro.calendar.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.taro.calendar.lib.drawable.FinishBackgroundDrawable;
import com.taro.calendar.lib.drawable.StopDrawable;
import com.taro.calendar.lib.drawable.TipDrawable;
import com.taro.calendar.lib.utils.Lunar;

import java.util.Calendar;
import java.util.HashSet;

/**
 * Created by taro on 2017/6/1.
 */

public class CalendarView extends View {
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
     * 默认选中当天背景色
     */
    public static final int DEFAULT_BACKGROUND_SELECTED_DAY = Color.parseColor("#04A3E3");
    /**
     * 默认日历控件的背景色
     */
    public static final int DEFAULT_BACKGROUND_CALENDAR = Color.WHITE;

    /**
     * flag节日信息,是否显示节日
     */
    public static final int MASK_FESTIVAL_SHOW = 0b0000_0001;
    /**
     * flag节日信息,是否显示公历节日
     */
    public static final int MASK_FESTIVAL_SOLAR = 0b0000_0010;
    /**
     * flag节日信息,是否显示农历节日
     */
    public static final int MASK_FESTIVAL_LUNAR = 0b0000_0100;
    /**
     * flag节日信息,是否显示节气
     */
    public static final int MASK_FESTIVAL_SOLAR_TERM = 0b0000_1000;
    /**
     * flag节日信息,是否农历节日优先,当多个优先同时设置时,将遵循农历->公历->节气的顺序进行处理
     */
    public static final int MASK_FESTIVAL_LUNAR_FIRST = 0b0001_0000;
    /**
     * flag节日信息,是否公历节日优先,当多个优先同时设置时,将遵循农历->公历->节气的顺序进行处理
     */
    public static final int MASK_FESTIVAL_SOLAR_FIRST = 0b0010_0000;
    /**
     * flag节日信息,是否节气节日优先,当多个优先同时设置时,将遵循农历->公历->节气的顺序进行处理
     */
    public static final int MASK_FESTIVAL_SOLAR_TERM_FIRST = 0b0100_0000;

    /**
     * flag日期信息,是否显示次要月份日期
     */
    public static final int MASK_CALENDAR_MINOR_MONTH = 0b0000_0000_0001;
    /**
     * flag日期信息,是否显示上一个月份日期
     */
    public static final int MASK_CALENDAR_PREVIOUS_MONTH = 0b0000_0000_0010;
    /**
     * flag日期信息,是否显示下一个月份日期
     */
    public static final int MASK_CALENDAR_NEXT_MONTH = 0b0000_0000_0100;
    /**
     * flag日期信息,是否显示农历日期
     */
    public static final int MASK_CALENDAR_LUNAR_DATE = 0b0000_0000_1000;
    /**
     * flag日期信息,次要月份周末不使用高亮颜色
     */
    public static final int MASK_CALENDAR_MINOR_WEEKEND = 0b0000_0001_0000;
    /**
     * flag日期信息,次要月份节日不使用高亮颜色
     */
    public static final int MASK_CALENDAR_MINOR_FESTIVAL = 0b0000_0010_0000;
    /**
     * flag日期信息,是否显示顶部周末信息标题栏
     */
    public static final int MASK_CALENDAR_WEEK_TITLE = 0b0000_0100_0000;
    /**
     * flag日期信息,顶部周末信息标题栏是否使用固定高度
     */
    public static final int MASK_CALENDAR_WEEK_TITLE_FIX = 0b0000_1000_0000;
    /**
     * flag日期信息,顶部周末信息标题栏是否使用中文,否则使用英文
     */
    public static final int MASK_CALENDAR_WEEK_TITLE_CHINESE = 0b0001_0000_0000;
    /**
     * flag日期信息,是否允许滑动
     */
    public static final int MASK_CALENDAR_SCROLL = 0b0010_0000_0000;
    /**
     * flag日期信息,右上角的标志
     */
    public static final int MASK_CALENDAR_TAG = 0b0100_0000_0000;

    //星期说明标题栏的字体颜色
    private int mWeekTitleTextColor;
    //周末颜色
    private int mWeekendTextColor;
    //节日颜色
    private int mFestivalTextColor;
    //农历颜色
    private int mLunarTextColor;
    //普通文字颜色
    private int mNormalDateTextColor;
    //次要文字颜色
    private int mMinorDateTextColor;
    //选中日期的背景色
    private int mSelectDateTextColor;
    //控件背景
    private int mBackgroundColor;
    //选中日期的背景色
    private int mSelectDateBackground;

    //星期标题栏的高度
    private float mWeekTitleHeight;
    //固定高度的星期标题栏
    private float mFixWeekTitleHeight;
    //每周开始的日期
    private int mWeekStartDay;
    //第一个日期相对当前月份第一天提前的天数
    //如开始时间为周一,当月第一天为周三,则提前天数为2(即为上个月在此月份显示的天数)
    private int mAdvanceDayOfMonth;
    //当前月份最后一天到最后一个日期的天数
    //如最后一天为31号,最后一个可见日期为下月5号,则剩余天数为5(即下个月在此月份显示的天数)
    private int mRestDayOfMonth;
    //当前月份的总天数
    private int mMonthDays;
    //当前月份占用的总行数(根据第一天开始星期可能不同会有变动,行数在4-6之间变动)
    private int mMonthRowCount;
    //当前月份第一天的星期
    private int mFirstDayOfWeek;
    //节日相关信息显示
    private int mFestivalMask;
    //日期相关信息显示
    private int mCalendarMask;

    private int mSelectedDay;
    private int mSelectedMonth;
    private int mSelectedYear;

    private Calendar mDrawDate;
    private Calendar mTodayDate;
    private Calendar mRecycleDate;
    private Lunar mRecycleLunar;

    private PointF mViewBounds;
    private RectF mDrawRect;
    private RectF mRecycleRectf;
    private Paint mDatePaint;

    private TipDrawable mDrawHoliday;
    private TipDrawable mDrawWork;
    private Bitmap mBottomBmp;
    private Drawable mBottomDraw;
    private Drawable mBackgroundDraw;

    private SparseArrayCompat<SparseArrayCompat<DayCell>> mDateMap;
    private HashSet<Integer> mMonthSet;

    private float mDownX, mDownY;
    private int mScrollDirection = SCROLL_AXIS_NONE;
    private float mScrollDistance;
    private long mDownTime;

    private float mSelectedMonthHeight;
    private float mCacheHeight;
    private float mCellWidth;
    private int mCacheMonth;
    private boolean mIsForceClearCache;

    //上个月份
    static final int MONTH_STATUS_PRE = -1;
    //当前月份
    static final int MONTH_STATUS_CURRENT = 0;
    //下个月份
    static final int MONTH_STATUS_NEXT = 1;
    //无滑动状态
    static final int SCROLL_AXIS_NONE = -1;
    //水平滑动状态
    static final int SCROLL_AXIS_HORIZONTAL = 1;
    //垂直滑动状态
    static final int SCROLL_AXIS_VERTICAL = 2;
    //默认的中英两个周末信息
    static final String[][] WEEK_DESC_ITEM = {
            {"周日", "周一", "周二", "周三", "周四", "周五", "周六"},
            {"SUN", "MON", "TUES", "WED", "THU", "FRI", "SAT"},
    };

    public CalendarView(Context context) {
        super(context);
        init();
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //宽度使用默认系统计算方式
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCellWidth = getMeasuredWidth() / 7;

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        //获取高度计算的模式
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        //获取默认计算得到的高度
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //计算得到期望高度
        float exceptHeight = mCellWidth * 6;
        switch (mode) {
            //若是高度不固定由控件决定,则使用期望高度
            case MeasureSpec.UNSPECIFIED:
                mWeekTitleHeight = computeWeekTitleHeight(measuredWidth, exceptHeight);
                setMeasuredDimension(measuredWidth, (int) (exceptHeight + mWeekTitleHeight));
                break;
            //有限制最大值的可调整高度,若能使用期望高度则使用期望高度,否则使用要求高度
            case MeasureSpec.AT_MOST:
                if (heightSize < exceptHeight) {
                    exceptHeight = heightSize;
                }
                mWeekTitleHeight = computeWeekTitleHeight(measuredWidth, exceptHeight);
                setMeasuredDimension(measuredWidth, (int) (exceptHeight + mWeekTitleHeight));
                break;
            case MeasureSpec.EXACTLY:
                mWeekTitleHeight = computeWeekTitleHeight(measuredWidth, measuredHeight);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                mDownTime = System.currentTimeMillis();
                mScrollDirection = SCROLL_AXIS_NONE;
                return true;
            case MotionEvent.ACTION_MOVE:
                if (getCalendarStatus(MASK_CALENDAR_SCROLL)) {
                    float x = event.getX();
                    float y = event.getY();
                    float distanceX = mDownX - x;
                    float distanceY = mDownY - y;
                    if (mScrollDirection == SCROLL_AXIS_NONE) {
                        float absX = Math.abs(distanceX);
                        float absY = Math.abs(distanceY);
                        if (absX > 10 && absX > absY) {
                            mScrollDirection = SCROLL_AXIS_HORIZONTAL;
                        } else if (absY > 10 && absY > absX) {
                            mScrollDirection = SCROLL_AXIS_VERTICAL;
                        }
                    }

                    if (mScrollDirection == SCROLL_AXIS_HORIZONTAL) {
                        mScrollDistance = distanceX;
                        invalidate();
                    } else if (mScrollDirection == SCROLL_AXIS_VERTICAL) {
                        mScrollDistance = distanceY;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                long upTime = System.currentTimeMillis();
                if (upTime - mDownTime < 350) {
                    //单击,计算按下位置的日期,更新当前选中日期
                    int day = computeCellDay(mDownX, mDownY);
                    if (day != -1) {
                        mSelectedDay = day;
                        invalidate();
                    }
                } else {
                    //长按
                }

                if (getCalendarStatus(MASK_CALENDAR_SCROLL)) {
                    if (mScrollDirection == SCROLL_AXIS_HORIZONTAL) {
                        mScrollDistance = mDownX - event.getX();
                        if (mScrollDistance <= -mViewBounds.x / 3) {
                            switch2NewMonth(false);
                        } else if (mScrollDistance >= mViewBounds.x / 3) {
                            switch2NewMonth(true);
                        }
                    } else if (mScrollDirection == SCROLL_AXIS_VERTICAL) {
                        mScrollDistance = mDownY - event.getY();
                        if (mScrollDistance <= -mViewBounds.y / 3) {
                            switch2NewMonth(false);
                        } else if (mScrollDistance >= mViewBounds.y / 3) {
                            switch2NewMonth(true);
                        }
                    }
                    mScrollDirection = SCROLL_AXIS_NONE;
                    mScrollDistance = 0;
                }
                break;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int widthSpec = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
        measure(widthSpec, heightSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initBeforeDraw();
        canvas.drawColor(mBackgroundColor);

        float startX, startY;
        int year, month;

        mRecycleDate.set(Calendar.YEAR, mSelectedYear);
        mRecycleDate.set(Calendar.MONTH, mSelectedMonth);

        boolean isDraw = false;
        if (mScrollDistance != 0) {
            //水平方向的滑动
            if (mScrollDirection == SCROLL_AXIS_HORIZONTAL) {
                //上个月界面
                startX = -mViewBounds.x - mScrollDistance;
                mRecycleDate.add(Calendar.MONTH, -1);
                if (startX + mViewBounds.x > 0 || startX < mViewBounds.x) {
                    year = mRecycleDate.get(Calendar.YEAR);
                    month = mRecycleDate.get(Calendar.MONTH);
                    drawMonth(canvas, startX, 0, year, month);
                    Log.e("draw", "draw pre ***********");
                }

                //本月界面
                startX += mViewBounds.x;
                mRecycleDate.set(Calendar.YEAR, mSelectedYear);
                mRecycleDate.set(Calendar.MONTH, mSelectedMonth);
                if (startX + mViewBounds.x > 0 || startX < mViewBounds.x) {
                    year = mRecycleDate.get(Calendar.YEAR);
                    month = mRecycleDate.get(Calendar.MONTH);
                    drawMonth(canvas, startX, 0, year, month);
                }

                //下个月界面
                startX += mViewBounds.x;
                mRecycleDate.set(Calendar.YEAR, mSelectedYear);
                mRecycleDate.set(Calendar.MONTH, mSelectedMonth);
                mRecycleDate.add(Calendar.MONTH, 1);
                if (startX + mViewBounds.x > 0 || startX < mViewBounds.x) {
                    year = mRecycleDate.get(Calendar.YEAR);
                    month = mRecycleDate.get(Calendar.MONTH);
                    drawMonth(canvas, startX, 0, year, month);
                    Log.e("draw", "draw next $$$$$$$$$$$$");
                }

                isDraw = true;
            } else if (mScrollDirection == SCROLL_AXIS_VERTICAL) {
                //垂直方向的滑动
                //上个月
                startY = -mViewBounds.y - mScrollDistance;
                mRecycleDate.add(Calendar.MONTH, -1);
                if (startY + mViewBounds.y > 0 || startY < mViewBounds.y) {
                    year = mRecycleDate.get(Calendar.YEAR);
                    month = mRecycleDate.get(Calendar.MONTH);
                    drawMonth(canvas, 0, startY, year, month);
                }

                //本月
                startY += mViewBounds.y;
                mRecycleDate.set(Calendar.YEAR, mSelectedYear);
                mRecycleDate.set(Calendar.MONTH, mSelectedMonth);
                if (startY + mViewBounds.y > 0 || startY < mViewBounds.y) {
                    year = mRecycleDate.get(Calendar.YEAR);
                    month = mRecycleDate.get(Calendar.MONTH);
                    drawMonth(canvas, 0, startY, year, month);
                }

                //下个月
                startY += mViewBounds.y;
                mRecycleDate.set(Calendar.YEAR, mSelectedYear);
                mRecycleDate.set(Calendar.MONTH, mSelectedMonth);
                mRecycleDate.add(Calendar.MONTH, 1);
                if (startY + mViewBounds.y > 0 || startY < mViewBounds.y) {
                    year = mRecycleDate.get(Calendar.YEAR);
                    month = mRecycleDate.get(Calendar.MONTH);
                    drawMonth(canvas, 0, startY, year, month);
                }

                isDraw = true;
            }
        }

        if (!isDraw) {
            year = mRecycleDate.get(Calendar.YEAR);
            month = mRecycleDate.get(Calendar.MONTH);
            drawMonth(canvas, 0, 0, year, month);
        }

        handleAfterDraw();
    }

    private void init() {
        mWeekTitleTextColor = DEFAULT_TEXT_COLOR_LUNAR;
        mWeekendTextColor = DEFAULT_TEXT_COLOR_WEEKEND;
        mFestivalTextColor = DEFAULT_TEXT_COLOR_FESTIVAL;
        mLunarTextColor = DEFAULT_TEXT_COLOR_LUNAR;
        mNormalDateTextColor = Color.BLACK;
        mMinorDateTextColor = DEFAULT_TEXT_COLOR_MINOR;
        mSelectDateTextColor = Color.WHITE;
        mBackgroundColor = DEFAULT_BACKGROUND_CALENDAR;
        mSelectDateBackground = DEFAULT_BACKGROUND_SELECTED_DAY;

        //每周开始星期
        mWeekStartDay = Calendar.SUNDAY;
        //默认显示节日,农历优先并显示所有农历/公历/节气
        // 0b0001_1111
        resetFestivalStatus();
        //日期信息,默认显示次要月份,并且次要月份全部不高亮,显示农历日期及周末标题栏
        resetCalendarStatus();

        mViewBounds = new PointF();
        mDrawDate = Calendar.getInstance();
        mTodayDate = Calendar.getInstance();
        mRecycleDate = Calendar.getInstance();
        mRecycleLunar = new Lunar(mTodayDate.getTimeInMillis());

        mSelectedDay = mTodayDate.get(Calendar.DAY_OF_MONTH);
        mSelectedMonth = mTodayDate.get(Calendar.MONTH);
        mSelectedYear = mTodayDate.get(Calendar.YEAR);

        mDatePaint = new Paint();
        mDatePaint.setAntiAlias(true);
        mDrawRect = new RectF();
        mRecycleRectf = new RectF();

        mDrawHoliday = new TipDrawable(Color.WHITE, Constant.DEFAULT_BACKGROUND_COLOR_GREEN, "假");
        mDrawWork = new TipDrawable(Color.WHITE, Constant.DEFAULT_BACKGROUND_COLOR_RED, "班");
        mBottomDraw = new StopDrawable();
        mBackgroundDraw = new FinishBackgroundDrawable();

        mMonthSet = new HashSet<>(mCacheMonth);

        mDateMap = new SparseArrayCompat<>();
        mCacheMonth = 3;
        for (int i = 0; i < mCacheMonth; i++) {
            SparseArrayCompat<DayCell> month = new SparseArrayCompat<>(31);
            for (int d = 1; d <= 31; d++) {
                month.put(d, new DayCell());
            }
            mDateMap.put(i, month);
        }
    }

    private void initBeforeDraw() {
        if (mViewBounds.equals(0, 0)) {
            mViewBounds.set(getWidth(), getHeight());
            mCellWidth = mViewBounds.x / 7;
        }
    }

    private void handleAfterDraw() {
        mIsForceClearCache = false;
        mMonthSet.clear();
        mMonthSet.add(mSelectedMonth);
        mMonthSet.add(mSelectedMonth - 1);
        mMonthSet.add(mSelectedMonth + 1);
    }

    private void computeCalendarBaseParams(int year, int month, int weekStartDay) {
        //设置本月1号
        mRecycleDate.set(year, month, 1);
        //获取当月天数
        mMonthDays = mRecycleDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        //获取第一天的星期
        mFirstDayOfWeek = mRecycleDate.get(Calendar.DAY_OF_WEEK);
        //获取上个月显示的天数
        mAdvanceDayOfMonth = (mFirstDayOfWeek - weekStartDay + 7) % 7;
        //计算出本月占用的行数
        //否则动态计算其行数
        mMonthRowCount = (mMonthDays + mAdvanceDayOfMonth) / 7;
        if (mMonthRowCount * 7 < (mMonthDays + mAdvanceDayOfMonth)) {
            //调整
            mMonthRowCount++;
        }
        //计算出下月显示的天数
        int totalPass = mAdvanceDayOfMonth + mMonthDays;
        mRestDayOfMonth = 7 * mMonthRowCount - totalPass;

        //计算标题外的高度
        float actualDateHeight = mViewBounds.y;
        if (getCalendarStatus(MASK_CALENDAR_WEEK_TITLE)) {
            actualDateHeight = mViewBounds.y - mWeekTitleHeight;
        }
        //计算实际绘制时的cell可用高度
        mCacheHeight = actualDateHeight / mMonthRowCount;
    }

    protected int computeCellDay(float downX, float downY) {
        float actualCellPointY = getCalendarStatus(MASK_CALENDAR_WEEK_TITLE) ? downY - mWeekTitleHeight : downY;

        int column = (int) (downX / mCellWidth);
        int row = (int) (actualCellPointY / mCacheHeight);
        int position = row * 7 + column + 1;

        int day = position - mAdvanceDayOfMonth;
        if (day > 0 && day <= mMonthDays) {
            return day;
        } else {
            return -1;
        }
    }

    protected float computeWeekTitleHeight(float viewWidth, float viewHeight) {
        if (!getCalendarStatus(MASK_CALENDAR_WEEK_TITLE) || viewWidth <= 0 || viewHeight <= 0) {
            return 0;
        } else {
            if (getCalendarStatus(MASK_CALENDAR_WEEK_TITLE_FIX)) {
                return mFixWeekTitleHeight;
            } else {
                float cellHeightExcept = viewHeight / 6;
                float minSize = Math.min(viewWidth / 7, cellHeightExcept);
                float textSize = minSize * 2f / 5;
                return textSize * 3f / 2;
            }
        }
    }

    protected boolean computeIfCellCanSeen(float startX, float startY) {
        float endStartX = startX + mCellWidth;
        float endStartY = startY + mCacheHeight;
        return endStartX > 0 && startX < mViewBounds.x && endStartY > 0 && startY < mViewBounds.y;
    }

    protected boolean computeIfIsSelectedDay(int day) {
        return day == mSelectedDay;
    }

    protected void switch2NewMonth(boolean isIncreased) {
        mRecycleDate.set(mSelectedYear, mSelectedMonth, 1);
        mRecycleDate.add(Calendar.MONTH, isIncreased ? 1 : -1);
        mSelectedYear = mRecycleDate.get(Calendar.YEAR);
        mSelectedMonth = mRecycleDate.get(Calendar.MONTH);
        invalidate();
    }

    private void drawDate(Canvas canvas, String prefix, float startX, float startY) {
        mDatePaint.setStyle(Paint.Style.FILL);
        mDatePaint.setStrokeWidth(20);
        mDatePaint.setTextSize(100);
        mDatePaint.setColor(Color.BLACK);
        mDatePaint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText(String.format("%s: %d-%02d", prefix, mRecycleDate.get(Calendar.YEAR), mRecycleDate.get(Calendar.MONTH) + 1),
                startX, startY, mDatePaint);
    }

    protected void drawMonth(Canvas canvas, float initX, float initY, int mainYear, int mainMonth) {
        computeCalendarBaseParams(mainYear, mainMonth, mWeekStartDay);
        int year, month, day, monthDay;
        float startX = initX, startY = initY;

        if (getCalendarStatus(MASK_CALENDAR_WEEK_TITLE)) {
            String[] item = getCalendarStatus(MASK_CALENDAR_WEEK_TITLE_CHINESE) ? WEEK_DESC_ITEM[0] : WEEK_DESC_ITEM[1];
            drawWeekTitle(canvas, startX, startY, item);
            startY += mWeekTitleHeight;
        }

        year = mainYear;
        month = mainMonth;

        if (getCalendarStatus(MASK_CALENDAR_PREVIOUS_MONTH) && mAdvanceDayOfMonth > 0) {
            mRecycleDate.set(mainYear, mainMonth, 1);
            mRecycleDate.add(Calendar.DAY_OF_MONTH, -mAdvanceDayOfMonth);
            //获取上个月份日期的年月日
            day = mRecycleDate.get(Calendar.DAY_OF_MONTH);
            year = mRecycleDate.get(Calendar.YEAR);
            month = mRecycleDate.get(Calendar.MONTH);
            monthDay = mRecycleDate.getActualMaximum(Calendar.DAY_OF_MONTH);
            //推前推算出上个月的日期
            updateMonthDate(year, month, 1, monthDay, true, true);
            //绘制上个月的日期
            //肯定不需要换行的!!!!
            for (int i = day; i <= monthDay; i++) {
                DayCell cell = mDateMap.get(month % mCacheMonth).get(i);
                if (computeIfCellCanSeen(startX, startY)) {
                    drawCell(canvas, startX, startY, cell, MONTH_STATUS_PRE);
                }
                startX += mCellWidth;
            }
        } else {
            startX += mCellWidth * mAdvanceDayOfMonth;
        }


        //获取当前日期的时间
        mRecycleDate.set(mainYear, mainMonth, 1);
        monthDay = mRecycleDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        day = mRecycleDate.get(Calendar.DAY_OF_MONTH);
        month = mRecycleDate.get(Calendar.MONTH);
        //推算本月的日期
        updateMonthDate(mainYear, mainMonth, 1, monthDay, true, true);
        //开始绘制前进行换行判断
        for (int i = day; i <= monthDay; i++) {
            //第一个日期肯定会在第一行,但是之后6个日期内随时都有可能会换行
            if ((i - 1 + mAdvanceDayOfMonth) % 7 == 0
                    //换行必须保存第一行第一项不是本月1号,否则会空出一行
                    && (mAdvanceDayOfMonth + i != 1)) {
                startX = initX;
                startY += mCacheHeight;
            }
            DayCell cell = mDateMap.get(month % mCacheMonth).get(i);
            if (computeIfCellCanSeen(startX, startY)) {
                drawCell(canvas, startX, startY, cell, MONTH_STATUS_CURRENT);
            }
            startX += mCellWidth;
        }

        if (getCalendarStatus(MASK_CALENDAR_NEXT_MONTH) && mRestDayOfMonth > 0) {
            mRecycleDate.set(mainYear, mainMonth, monthDay);
            mRecycleDate.add(Calendar.DAY_OF_MONTH, mRestDayOfMonth);
            //获取下个月份的时间
            day = mRecycleDate.get(Calendar.DAY_OF_MONTH);
            month = mRecycleDate.get(Calendar.MONTH);
            year = mRecycleDate.get(Calendar.YEAR);
            //推算下月的日期
            monthDay = mRecycleDate.getActualMaximum(Calendar.DAY_OF_MONTH);
            updateMonthDate(year, month, 1, monthDay, true, true);
            //肯定不需要换行的!!!!
            for (int i = 1; i <= day; i++) {
                DayCell cell = mDateMap.get(month % mCacheMonth).get(i);
                if (computeIfCellCanSeen(startX, startY)) {
                    drawCell(canvas, startX, startY, cell, MONTH_STATUS_NEXT);
                }
                startX += mCellWidth;
            }
        }
    }

    protected void updateMonthDate(int year, int month, int fromDay, int dayDistance, boolean isContainFirstDay, boolean direction) {
        if (!mIsForceClearCache && mMonthSet.contains(month)) {
            return;
        }
        mRecycleDate.set(year, month, fromDay);

        int nowYear = mTodayDate.get(Calendar.YEAR);
        int nowMonth = mTodayDate.get(Calendar.MONTH);
        int nowDate = mTodayDate.get(Calendar.DAY_OF_MONTH);

        int begin = 0;
        if (direction) {
            //若不包括第一天,则开始前先增加1天,排除掉第一天
            if (!isContainFirstDay) {
                begin = 1;
            }
            mRecycleDate.add(Calendar.DAY_OF_MONTH, begin);
            month = mRecycleDate.get(Calendar.MONTH);
            //向后加载日期信息
            for (int i = 0; i < dayDistance; i++) {
                int day = mRecycleDate.get(Calendar.DAY_OF_MONTH);
                SparseArrayCompat<DayCell> monthMap = mDateMap.get(month % mCacheMonth);
                DayCell outCell = monthMap.get(day);
//                DayCell outCell = mDateMap.get(month % mCacheMonth).get(day);
                setDayCell(outCell, nowYear, nowMonth, nowDate, mRecycleDate);
                mRecycleDate.add(Calendar.DAY_OF_MONTH, 1);
            }
        } else {
            //若不包括第一天,开始前减1天,排除掉第一天
            if (!isContainFirstDay) {
                begin = -1;
            }
            mRecycleDate.add(Calendar.DAY_OF_MONTH, begin);
            month = mRecycleDate.get(Calendar.MONTH);
            //向前加载日期信息
            for (int i = 0; i < dayDistance; i++) {
                int day = mRecycleDate.get(Calendar.DAY_OF_MONTH);

                DayCell outCell = mDateMap.get(month % mCacheMonth).get(day);
                setDayCell(outCell, nowYear, nowMonth, nowDate, mRecycleDate);
                mRecycleDate.add(Calendar.DAY_OF_MONTH, -1);
            }
        }
    }

    protected void setDayCell(DayCell cell, int nowYear, int nowMonth, int nowDay, Calendar changedDate) {
        if (cell == null) {
            return;
        }
        int changedDay, changedMonth, changedYear, weekDay;
        changedDay = changedDate.get(Calendar.DAY_OF_MONTH);
        changedMonth = changedDate.get(Calendar.MONTH);
        changedYear = changedDate.get(Calendar.YEAR);
        weekDay = changedDate.get(Calendar.DAY_OF_WEEK);

        //公历日期
        cell.setDay(changedDay);
        cell.setMonth(changedMonth);
        //是否今天
        cell.setIsToday(changedYear == nowYear && changedMonth == nowMonth && changedDay == nowDay);
        //是否周末
        cell.setIsWeekend(weekDay == Calendar.SUNDAY || weekDay == Calendar.SATURDAY);

        //当需要日期或者节日时才进行农历的创建,否则不进行处理
        if (getCalendarStatus(MASK_CALENDAR_LUNAR_DATE) || getFestivalStatus(MASK_FESTIVAL_SHOW)) {
            mRecycleLunar.updateDate(changedDate.getTimeInMillis());
        }
        //农历日期
        cell.setLunarDate(mRecycleLunar.getLunarDayString());
        if (getFestivalStatus(MASK_FESTIVAL_SHOW)) {
            //是否节假日
            cell.setIsHoliday(mRecycleLunar.isFestival());
            //农历节日
            cell.setLunarFestival(mRecycleLunar.getLFestivalName());
            //公历节日
            cell.setSolarFestival(mRecycleLunar.getSFestivalName());
            //节气
            cell.setSolarTermFestival(mRecycleLunar.getTermString());
        }

        //是否选中了今天
        if (cell.isHoliday()) {
            cell.setSpecialDate(DayCell.MASK_DATE_HOLIDAY, true);
            cell.setSpecialDate(DayCell.MASK_DATE_DRAWABLE, true);
        } else {
            cell.setSpecialDate(DayCell.MASK_DATE_HOLIDAY, false);
            cell.setSpecialDate(DayCell.MASK_DATE_DRAWABLE, false);
        }
    }

    protected void drawCell(Canvas canvas, float startX, float startY, DayCell cell, int monthStatus) {
        if (cell == null) {
            return;
        } else {
            float tempX, tempY;
            float minSize = Math.min(mCellWidth, mCacheHeight);
            int suggestColor = mNormalDateTextColor;
            int fesTextColor = mFestivalTextColor;
            int weekendColor = mWeekendTextColor;
            int lunarColor = mLunarTextColor;
            int dateTextColor = suggestColor;

            //若是绘制上个月或者下个月的数据
            if (monthStatus == MONTH_STATUS_PRE
                    || monthStatus == MONTH_STATUS_NEXT) {
                dateTextColor = mMinorDateTextColor;
                lunarColor = mMinorDateTextColor;
                //根据配置要求设置相应的字体颜色
                if (getCalendarStatus(MASK_CALENDAR_MINOR_WEEKEND)) {
                    weekendColor = mMinorDateTextColor;
                }
                if (getCalendarStatus(MASK_CALENDAR_MINOR_FESTIVAL)) {
                    fesTextColor = mMinorDateTextColor;
                }
            }
            mDrawRect.set(startX, startY, startX + mCellWidth, startY + mCacheHeight);

            //若周末文本则使用周末文本颜色
            //此参数在这里设置是因为后面有可能日期是被选中日期,则文本颜色为白色
            dateTextColor = cell.isWeekend() ? weekendColor : dateTextColor;
            //选中当天背景色
            if (monthStatus == MONTH_STATUS_CURRENT && computeIfIsSelectedDay(cell.getDay())) {
                dateTextColor = mSelectDateTextColor;
                mDatePaint.setColor(mSelectDateBackground);
                mDatePaint.setStyle(Paint.Style.FILL);

                if (mBackgroundDraw != null) {
                    float centerX = startX + mCellWidth / 2;
                    float centerY = startY + mCacheHeight / 2;
                    mBackgroundDraw.setBounds((int) (centerX - minSize / 2), (int) (centerY - minSize / 2),
                            (int) (centerX + minSize / 2), (int) (centerY + minSize / 2));
                    mBackgroundDraw.draw(canvas);
                } else {
                    canvas.drawCircle(mDrawRect.centerX(), mDrawRect.centerY(), minSize / 2, mDatePaint);
                }
            }
            //今天日期轮廓
            if (cell.isToday()) {
                mDatePaint.setColor(mSelectDateBackground);
                mDatePaint.setStyle(Paint.Style.STROKE);
                mDatePaint.setStrokeWidth(1.5f);
                canvas.drawCircle(mDrawRect.centerX(), mDrawRect.centerY(), minSize / 2, mDatePaint);
            }

            //日期文本
            String day = String.format("%1$02d", cell.getDay());
            float dateTextSize = minSize * 2f / 5;
            mDatePaint.setStyle(Paint.Style.FILL);
            mDatePaint.setColor(dateTextColor);
            mDatePaint.setTextAlign(Paint.Align.CENTER);
            mDatePaint.setTextSize(dateTextSize);

            tempX = startX + mCellWidth / 2;
            tempY = startY + mCacheHeight / 2;
            canvas.drawText(day, tempX, tempY, mDatePaint);

            //计算drawable图标的大小
            int drawSize = (int) (minSize / 2 - dateTextSize / 2);
            //底部的小图标与农历日期只能存在一个,没有足够的空间可以同时存在
            //绘制底部的图片
            if (cell.isSpecialDate(DayCell.MASK_DATE_DRAWABLE)) {
                drawBottomDrawable(canvas, startX, startY, minSize, drawSize);
            } else {
                //节日或者农历日期的绘制
                drawFestivalOrLunarDate(canvas, cell, startX, startY, fesTextColor, lunarColor, minSize);
            }

            tempX = startX + mCellWidth / 2 - minSize / 2;
            tempY = startY + mCacheHeight / 2 - minSize / 2;
            //是否为假期
            if (cell.isSpecialDate(DayCell.MASK_DATE_HOLIDAY) && mDrawHoliday != null) {
                mDrawHoliday.setBounds(0, 0, drawSize, drawSize);
                mDrawHoliday.setDrawXY(tempX, tempY);
                mDrawHoliday.draw(canvas);
            }
            //是否为加班
            if (cell.isSpecialDate(DayCell.MASK_DATE_WORK) && mDrawWork != null) {
                mDrawWork.setBounds(0, 0, drawSize, drawSize);
                mDrawWork.setDrawXY(tempX, tempY);
                mDrawWork.draw(canvas);
            }
        }
    }

    protected void drawBottomDrawable(Canvas canvas, float startX, float startY, float cellMinSize, float suggestSize) {
        if (suggestSize >= cellMinSize / 2) {
            suggestSize = cellMinSize / 2 * 4f / 5;
        }

        float tempX, tempY;
        tempX = startX + mCellWidth / 2 - suggestSize / 2;
        tempY = startY + mCacheHeight / 2 + ((mCacheHeight / 2 - suggestSize)) / 2;
        mRecycleRectf.set(tempX, tempY, tempX + suggestSize, tempY + suggestSize);

        if (mBottomBmp != null && !mBottomBmp.isRecycled()) {
            canvas.drawBitmap(mBottomBmp, null, mRecycleRectf, mDatePaint);
        } else if (mBottomDraw != null) {
            mBottomDraw.setBounds((int) mRecycleRectf.left, (int) mRecycleRectf.top,
                    (int) mRecycleRectf.right, (int) mRecycleRectf.bottom);
            mBottomDraw.draw(canvas);
        }
    }

    protected void drawFestivalOrLunarDate(Canvas canvas, DayCell cell, float startX, float startY, int fesTextColor, int lunarColor, float cellMinSize) {
        String festivalOrDate = null;
        float tempX, tempY;
        //若显示节日时,才进行绘制
        if (getFestivalStatus(MASK_FESTIVAL_SHOW)) {
            //农历节日
            String solarFestival = cell.getSolarFestival();
            //公历节日
            String lunarFestival = cell.getLunarFestival();
            //节气
            String solarTerm = cell.getSolarTermFestival();

            if (!getFestivalStatus(MASK_FESTIVAL_LUNAR)) {
                lunarFestival = null;
            }
            if (!getFestivalStatus(MASK_FESTIVAL_SOLAR)) {
                solarFestival = null;
            }
            if (!getFestivalStatus(MASK_FESTIVAL_SOLAR_TERM)) {
                solarFestival = null;
            }

            //优先显示农历节日
            if (getFestivalStatus(MASK_FESTIVAL_LUNAR_FIRST)) {
                //农历优先
                if (lunarFestival != null && lunarFestival.length() > 0) {
                    festivalOrDate = lunarFestival;
                } else if (solarTerm != null && solarTerm.length() > 0) {
                    //节气次之
                    festivalOrDate = solarTerm;
                } else if (solarFestival != null && solarFestival.length() > 0) {
                    //普通公历节日
                    festivalOrDate = solarFestival;
                }
            } else if (getFestivalStatus(MASK_FESTIVAL_SOLAR_FIRST)) {
                //公历优先
                if (solarFestival != null && solarFestival.length() > 0) {
                    festivalOrDate = solarFestival;
                } else if (lunarFestival != null && lunarFestival.length() > 0) {
                    //农历
                    festivalOrDate = lunarFestival;
                } else if (solarTerm != null && solarTerm.length() > 0) {
                    //节气
                    festivalOrDate = solarTerm;
                }
            } else if (getFestivalStatus(MASK_FESTIVAL_SOLAR_TERM_FIRST)) {
                //节气优先
                if (solarTerm != null && solarTerm.length() > 0) {
                    festivalOrDate = solarTerm;
                } else if (lunarFestival != null && lunarFestival.length() > 0) {
                    //农历
                    festivalOrDate = lunarFestival;
                } else if (solarFestival != null && solarFestival.length() > 0) {
                    //普通公历节日
                    festivalOrDate = solarFestival;
                }
            }
        }

        //若显示农历日期则进行处理
        if (getCalendarStatus(MASK_CALENDAR_LUNAR_DATE)) {
            //农历日期
            String lunarDate = cell.getLunarDate();
            //若不存在任何节日,显示农历日期
            if (festivalOrDate == null || festivalOrDate.length() <= 0) {
                fesTextColor = lunarColor;
                festivalOrDate = lunarDate;
            }
        }

        if (festivalOrDate != null && festivalOrDate.length() > 0) {
            //文本大小为以一行可放置文本长度为准
            float fesTextSize = cellMinSize * 2f / (3 * festivalOrDate.length());
            //字体太大稍微再缩小一些
            fesTextSize = fesTextSize * 2f / 3;
            mDatePaint.setStyle(Paint.Style.FILL);
            mDatePaint.setTextSize(fesTextSize);
            mDatePaint.setColor(computeIfIsSelectedDay(cell.getDay()) ? mSelectDateTextColor : fesTextColor);
            mDatePaint.setTextAlign(Paint.Align.CENTER);

            //绘制在下半圆中
            tempX = startX + mCellWidth / 2;
            tempY = startY + mCacheHeight / 2 + cellMinSize * 1f / 3;
            canvas.drawText(festivalOrDate, tempX, tempY, mDatePaint);
        }
    }


    protected void drawWeekTitle(Canvas canvas, float startX, float startY, String[] weekItems) {
        int begin = mWeekStartDay;
        mDatePaint.setColor(mWeekTitleTextColor);
        for (int i = 0; i < 7; i++) {
            int index = (begin - 1) % 7;
            if (index >= 0 && index < 7) {
                String item = weekItems[index];
                drawCommonText(item, startX, startY, mCellWidth, mWeekTitleHeight, canvas, mDatePaint);
                startX += mCellWidth;
            }
            begin++;
        }
    }

    protected void drawCommonText(String text, float startX, float startY, float width, float height, Canvas canvas, Paint paint) {
        if (text == null || text.length() <= 0 || width <= 0 || height <= 0) {
            return;
        } else {
            float widthSize = width * 3f / (5 * text.length());
            float heightSize = height * 1f / 2;
            float textSize = Math.min(widthSize, heightSize);

            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetrics fm = paint.getFontMetrics();

            float tempX = startX + width / 2;
            float tempY = startY + (height / 2 + (fm.bottom - fm.top) / 2 - fm.bottom);
            canvas.drawText(text, tempX, tempY, paint);
        }
    }

    public CalendarView resetCalendarStatus() {
        //日期信息,默认显示次要月份,并且次要月份全部不高亮,显示农历日期及周末标题栏
        mCalendarMask = MASK_CALENDAR_MINOR_MONTH
                | MASK_CALENDAR_PREVIOUS_MONTH | MASK_CALENDAR_NEXT_MONTH
                | MASK_CALENDAR_MINOR_WEEKEND | MASK_CALENDAR_MINOR_FESTIVAL
                | MASK_CALENDAR_WEEK_TITLE | MASK_CALENDAR_WEEK_TITLE_CHINESE
                | MASK_CALENDAR_LUNAR_DATE
                | MASK_CALENDAR_SCROLL;
        return this;
    }

    public CalendarView resetFestivalStatus() {
        //默认显示节日,农历优先并显示所有农历/公历/节气
        // 0b0001_1111
        mFestivalMask = MASK_FESTIVAL_SHOW
                | MASK_FESTIVAL_LUNAR | MASK_FESTIVAL_SOLAR | MASK_FESTIVAL_SOLAR_TERM
                | MASK_FESTIVAL_LUNAR_FIRST;
        return this;
    }

    public boolean getCalendarStatus(int mask) {
        return (mCalendarMask & mask) != 0;
    }

    public CalendarView addCalendarFlag(int mask) {
        mCalendarMask |= mask;
        return this;
    }

    public CalendarView removeCalendarFlag(int mask) {
        mCalendarMask &= ~mask;
        return this;
    }

    public CalendarView setCalendarFlag(int flag) {
        mCalendarMask = flag;
        return this;
    }


    public boolean getFestivalStatus(int mask) {
        return (mFestivalMask & mask) != 0;
    }

    public CalendarView addFestivalFlag(int mask) {
        mFestivalMask |= mask;
        return this;
    }

    public CalendarView removeFestivalFlag(int mask) {
        mFestivalMask &= ~mask;
        return this;
    }

    public CalendarView setFestvalFlag(int flag) {
        mFestivalMask = flag;
        return this;
    }

}
