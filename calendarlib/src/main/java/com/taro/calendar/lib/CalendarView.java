package com.taro.calendar.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.taro.calendar.lib.drawable.TipDrawable;
import com.taro.calendar.lib.utils.Lunar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.Stack;

import static android.R.attr.y;


/**
 * Created by taro on 2017/6/1.
 */

public class CalendarView extends View {

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
     * flag日期信息,是否允许滑动
     */
    public static final int MASK_CALENDAR_SCROLL_HORIZONTAL = 0b0100_0000_0000;
    /**
     * flag日期信息,是否允许滑动
     */
    public static final int MASK_CALENDAR_SCROLL_VERTICAL = 0b1000_0000_0000;
    /**
     * flag日期信息,右上角的标志
     */
    public static final int MASK_CALENDAR_TAG = 0b0001_0000_0000_0000;

    //所有颜色设置存储类
    private ColorSetting mColor;

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

    //当前选中日期
    private int mSelectedDay;
    //当前选中月份,即当前显示的月份
    private int mSelectedMonth;
    //当前选中的年份
    private int mSelectedYear;

    private Calendar mDrawDate;
    //今日日期,永远都是今日日期,不会使用其它日期
    private Calendar mTodayDate;
    //复用的日期对象
    private Calendar mRecycleDate;
    //复用的农历日期对象
    private Lunar mRecycleLunar;

    private PointF mViewBounds;
    private RectF mDrawRect;
    private RectF mRecycleRectf;
    private Paint mDatePaint;

    //假期图标
    private TipDrawable mDrawHoliday;
    //加班图标
    private TipDrawable mDrawWork;
    //底部的小图标,或者图片
    private Bitmap mBottomBmp;
    private Drawable mBottomDraw;
    //背景图片
    private Drawable mBackgroundDraw;

    //缓存计算日期的map对象
    private SparseArrayCompat<SparseArrayCompat<DayCell>> mCacheDateMap;
    //记录当前已缓存的月份对象
    private Stack<SparseArrayCompat<DayCell>> mRecycleMonthStack;

    private float mDownX, mDownY;
    //界面滑动方向
    private int mScrollDirection = SCROLL_AXIS_NONE;
    //界面滑动距离,决定了最终是否切换月份
    private float mScrollDistance;
    private long mDownTime;

    //当前显示界面的日期单元高度
    private float mCacheHeight;
    //日期单元宽度
    private float mCellWidth;
    //当前缓存的月份数据数量
    private int mCacheMonthCount;
    //是否强制重新清除缓存,重新加载数据
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
                //若允许滑动,则尝试进行滑动事件处理
                if (getCalendarStatus(MASK_CALENDAR_SCROLL)) {
                    float x = event.getX();
                    float y = event.getY();
                    float distanceX = mDownX - x;
                    float distanceY = mDownY - y;
                    //当前未确定滑动方向时
                    if (mScrollDirection == SCROLL_AXIS_NONE) {
                        float absX = Math.abs(distanceX);
                        float absY = Math.abs(distanceY);
                        if (getCalendarStatus(MASK_CALENDAR_SCROLL_HORIZONTAL) && getCalendarStatus(MASK_CALENDAR_SCROLL_VERTICAL)) {
                            //根据滑动的距离确定滑动方向
                            if (absX > 10 && absX > absY) {
                                mScrollDirection = SCROLL_AXIS_HORIZONTAL;
                            } else if (absY > 10 && absY > absX) {
                                mScrollDirection = SCROLL_AXIS_VERTICAL;
                            }
                        } else if (getCalendarStatus(MASK_CALENDAR_SCROLL_HORIZONTAL) && absX > 10) {
                            mScrollDirection = SCROLL_AXIS_HORIZONTAL;
                        } else if (getCalendarStatus(MASK_CALENDAR_SCROLL_VERTICAL) && absY > 10) {
                            mScrollDirection = SCROLL_AXIS_VERTICAL;
                        }
                        //一次滑动事件中如果已经确定了滑动方向则不会再有任何改变
                    }

                    //根据滑动方向,更新滑动距离并刷新界面
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
                //若未进行任何滑动事件,则尝试处理为点击或长按事件
                if (mScrollDirection == SCROLL_AXIS_NONE) {
                    long upTime = System.currentTimeMillis();
                    if (upTime - mDownTime < 350) {
                        //单击,计算按下位置的日期,更新当前选中日期
                        int day = computeCellDay(mDownX, mDownY);
                        if (day != -1) {
                            //切换选中日期
                            mSelectedDay = day;
                        }
                    } else {
                        //TODO:长按
                    }
                }

                //若允许进行滑动,则尝试处理滑动事件
                if (getCalendarStatus(MASK_CALENDAR_SCROLL)) {
                    //水平滑动方向
                    if (mScrollDirection == SCROLL_AXIS_HORIZONTAL) {
                        //计算最新的滑动距离
                        mScrollDistance = mDownX - event.getX();
                        //若滑动距离超过1/3则进行切换
                        if (mScrollDistance <= -mViewBounds.x / 3) {
                            //切换到下个月
                            switch2NewMonth(false);
                        } else if (mScrollDistance >= mViewBounds.x / 3) {
                            //切换到上个月
                            switch2NewMonth(true);
                        }
                    } else if (mScrollDirection == SCROLL_AXIS_VERTICAL) {
                        //垂直滑动方向
                        mScrollDistance = mDownY - event.getY();
                        if (mScrollDistance <= -mViewBounds.y / 3) {
                            switch2NewMonth(false);
                        } else if (mScrollDistance >= mViewBounds.y / 3) {
                            switch2NewMonth(true);
                        }
                    }
                }
                //滑动方向重置
                mScrollDirection = SCROLL_AXIS_NONE;
                mScrollDistance = 0;
                //刷新界面
                invalidate();
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
        //初始化绘制前的数据
        initBeforeDraw();
        //绘制背景色
        canvas.drawColor(mColor.mBackgroundColor);

        float startX, startY;
        int year, month;

        //设置当前选中日期的时间
        mRecycleDate.set(Calendar.YEAR, mSelectedYear);
        mRecycleDate.set(Calendar.MONTH, mSelectedMonth);

        //滑动界面是否绘制的标志
        boolean isScrollDraw = false;
        //当前处于滑动状态,绘制滑动时的界面
        if (mScrollDistance != 0) {
            //水平方向的滑动
            if (mScrollDirection == SCROLL_AXIS_HORIZONTAL) {
                //上个月界面
                startX = -mViewBounds.x - mScrollDistance;
                mRecycleDate.add(Calendar.MONTH, -1);
                if (startX + mViewBounds.x > 0) {
                    year = mRecycleDate.get(Calendar.YEAR);
                    month = mRecycleDate.get(Calendar.MONTH);
                    drawMonth(canvas, startX, 0, year, month);
                }

                //本月界面
                startX += mViewBounds.x;
                mRecycleDate.set(Calendar.YEAR, mSelectedYear);
                mRecycleDate.set(Calendar.MONTH, mSelectedMonth);
                year = mRecycleDate.get(Calendar.YEAR);
                month = mRecycleDate.get(Calendar.MONTH);
                drawMonth(canvas, startX, 0, year, month);

                //下个月界面
                startX += mViewBounds.x;
                mRecycleDate.set(Calendar.YEAR, mSelectedYear);
                mRecycleDate.set(Calendar.MONTH, mSelectedMonth);
                mRecycleDate.add(Calendar.MONTH, 1);
                if (startX < mViewBounds.x) {
                    year = mRecycleDate.get(Calendar.YEAR);
                    month = mRecycleDate.get(Calendar.MONTH);
                    drawMonth(canvas, startX, 0, year, month);
                }

                isScrollDraw = true;
            } else if (mScrollDirection == SCROLL_AXIS_VERTICAL) {
                //垂直方向的滑动
                //上个月
                startY = -mViewBounds.y - mScrollDistance;
                mRecycleDate.add(Calendar.MONTH, -1);
                if (startY + mViewBounds.y > 0) {
                    year = mRecycleDate.get(Calendar.YEAR);
                    month = mRecycleDate.get(Calendar.MONTH);
                    drawMonth(canvas, 0, startY, year, month);
                }

                //本月
                startY += mViewBounds.y;
                mRecycleDate.set(Calendar.YEAR, mSelectedYear);
                mRecycleDate.set(Calendar.MONTH, mSelectedMonth);
                year = mRecycleDate.get(Calendar.YEAR);
                month = mRecycleDate.get(Calendar.MONTH);
                drawMonth(canvas, 0, startY, year, month);

                //下个月
                startY += mViewBounds.y;
                mRecycleDate.set(Calendar.YEAR, mSelectedYear);
                mRecycleDate.set(Calendar.MONTH, mSelectedMonth);
                mRecycleDate.add(Calendar.MONTH, 1);
                if (startY < mViewBounds.y) {
                    year = mRecycleDate.get(Calendar.YEAR);
                    month = mRecycleDate.get(Calendar.MONTH);
                    drawMonth(canvas, 0, startY, year, month);
                }

                isScrollDraw = true;
            }
        }

        //若未进行滑动界面的绘制,则绘制静态界面
        if (!isScrollDraw) {
            year = mRecycleDate.get(Calendar.YEAR);
            month = mRecycleDate.get(Calendar.MONTH);
            drawMonth(canvas, 0, 0, year, month);
        }

        //绘制后的数据重置及处理
        handleAfterDraw();
    }

    /**
     * 数据初始化,基本的默认的数值及对象创建
     */
    private void init() {
        mColor = new ColorSetting();

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

        //默认选中日期使用当天的日期
        mSelectedDay = mTodayDate.get(Calendar.DAY_OF_MONTH);
        mSelectedMonth = mTodayDate.get(Calendar.MONTH);
        mSelectedYear = mTodayDate.get(Calendar.YEAR);

        mDatePaint = new Paint();
        mDatePaint.setAntiAlias(true);
        mDrawRect = new RectF();
        mRecycleRectf = new RectF();

        //创建默认的假期及加班图标
        mDrawHoliday = new TipDrawable(Color.WHITE, Constant.DEFAULT_BACKGROUND_COLOR_GREEN, "假");
        mDrawWork = new TipDrawable(Color.WHITE, Constant.DEFAULT_BACKGROUND_COLOR_RED, "班");

        //创建记录缓存月份数据的容器
        mRecycleMonthStack = new Stack<>();

        //创建缓存月份数据的map
        mCacheDateMap = new SparseArrayCompat<>();
        mCacheMonthCount = 5;
        for (int i = 0; i < mCacheMonthCount + 2; i++) {
            SparseArrayCompat<DayCell> month = new SparseArrayCompat<>(31);
            for (int d = 1; d <= 31; d++) {
                month.put(d, new DayCell());
            }
            mRecycleMonthStack.add(month);
        }
    }

    /**
     * 绘制工作前的初始化工作
     */
    private void initBeforeDraw() {
        if (mViewBounds.equals(0, 0)) {
            mViewBounds.set(getWidth(), getHeight());
            mCellWidth = mViewBounds.x / 7;
        }

        if (mCacheDateMap.size() > mCacheMonthCount) {
            int firstMonth = (mSelectedMonth - mCacheMonthCount / 2 + 12) % 12;
            int secondMonth = (firstMonth + mCacheMonthCount) % 12;
            if (firstMonth + mCacheMonthCount > 11) {
                for (int i = secondMonth; i < firstMonth; i++) {
                    SparseArrayCompat<DayCell> monthMap = mCacheDateMap.get(i);
                    mCacheDateMap.remove(i);
                    if (monthMap != null) {
                        mRecycleMonthStack.add(monthMap);
                    }
                }
            } else {
                for (int i = 0; i < firstMonth; i++) {
                    SparseArrayCompat<DayCell> monthMap = mCacheDateMap.get(i);
                    mCacheDateMap.remove(i);
                    if (monthMap != null) {
                        mRecycleMonthStack.add(monthMap);
                    }
                }
                for (int i = secondMonth; i < 12; i++) {
                    SparseArrayCompat<DayCell> monthMap = mCacheDateMap.get(i);
                    mCacheDateMap.remove(i);
                    if (monthMap != null) {
                        mRecycleMonthStack.add(monthMap);
                    }
                }
            }
        }
    }

    /**
     * 绘制工作结束后的处理工作
     */
    private void handleAfterDraw() {
        //设置不强制清除缓存数据,便于月份缓存数据的复用
        mIsForceClearCache = false;
    }

    /**
     * 计算日历绘制相关的基础参数
     *
     * @param year         当前需要绘制的年份
     * @param month        当前需要绘制的月份
     * @param weekStartDay 当前绘制时星期开始的第一天
     */
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

    /**
     * 计算出按下位置的日期单元
     *
     * @param downX 按下位置x坐标
     * @param downY 按下位置y坐标
     * @return 若日期为合法日期, 则返回该值, 否则返回-1
     */
    protected int computeCellDay(float downX, float downY) {
        //计算实际的日期显示高度,取决于是否存在星期标题
        float actualCellPointY = downY - getExceptCalendarTopHeight();

        //计算出按下位置的列
        int column = (int) (downX / mCellWidth);
        //计算出按下位置的行
        int row = (int) (actualCellPointY / mCacheHeight);
        //计算出按下位置的实际位置
        int position = row * 7 + column + 1;

        //计算出实际按下日期,除去前面可能有上个月的部分天数
        int day = position - mAdvanceDayOfMonth;
        if (day > 0 && day <= mMonthDays) {
            //日期在合理范围内时返回,否则返回-1无效值
            return day;
        } else {
            return -1;
        }
    }

    /**
     * 计算出星期标题的高度,根据配置信息返回固定高度或者是动态计算后得到的高度
     *
     * @param viewWidth  当前日历控件的宽度
     * @param viewHeight 当前日历控件的高度
     * @return
     */
    protected float computeWeekTitleHeight(float viewWidth, float viewHeight) {
        //若数据不合法或者不需要星期标题,则返回0
        if (!getCalendarStatus(MASK_CALENDAR_WEEK_TITLE) || viewWidth <= 0 || viewHeight <= 0) {
            return 0;
        } else {
            //若需要返回固定星期高度
            if (getCalendarStatus(MASK_CALENDAR_WEEK_TITLE_FIX)) {
                return mFixWeekTitleHeight;
            } else {
                //计算不存在星期标题高度时可能的高度
                float cellHeightExcept = viewHeight / 6;
                //计算出宽和高中最小值
                float minSize = Math.min(viewWidth / 7, cellHeightExcept);
                //取期高度的2/5为标题字体大小
                float textSize = minSize * 2f / 5;
                //细微调整日字体大小
                return textSize * 3f / 2;
            }
        }
    }

    /**
     * 判断当前日期单元格是否可见,用于判断单元格是否可进行绘制
     *
     * @param startX 开始绘制的X坐标
     * @param startY 开始绘制的Y坐标
     * @return
     */
    protected boolean computeIfCellCanSeen(float startX, float startY) {
        //绘制区域的坐标
        float endStartX = startX + mCellWidth;
        float endStartY = startY + mCacheHeight;
        //坐标在合法范围内时可见或部分可见
        return endStartX > 0 && startX < mViewBounds.x && endStartY > 0 && startY < mViewBounds.y;
    }

    /**
     * 判断当前是否为选中的日期(仅日期)
     *
     * @param day
     * @return
     */
    protected boolean computeIfIsSelectedDay(int day) {
        return day == mSelectedDay;
    }

    /**
     * 切换到上个月份或者下个月份
     *
     * @param isIncreased true为切换到下个月份,false为切换到上个月份
     */
    protected void switch2NewMonth(boolean isIncreased) {
        mRecycleDate.set(mSelectedYear, mSelectedMonth, 1);
        mRecycleDate.add(Calendar.MONTH, isIncreased ? 1 : -1);
        mSelectedYear = mRecycleDate.get(Calendar.YEAR);
        mSelectedMonth = mRecycleDate.get(Calendar.MONTH);
    }

    /**
     * 绘制某个月份的日期,核心绘制方法.绘制为整个月的界面数据
     *
     * @param canvas
     * @param initX     开始绘制的X坐标
     * @param initY     开始绘制的Y坐标
     * @param mainYear  当前需要绘制的日期年份
     * @param mainMonth 当前需要绘制的日期月份
     */
    protected void drawMonth(Canvas canvas, float initX, float initY, int mainYear, int mainMonth) {
        //绘制前计算绘制需要的参数,包括绘制单元的行数/高度/字体大小等
        computeCalendarBaseParams(mainYear, mainMonth, mWeekStartDay);
        int year, month, day, monthDay;
        float startX = initX, startY = initY;

        //若需要绘制星期标题,则尝试绘制星期标题
        if (getCalendarStatus(MASK_CALENDAR_WEEK_TITLE)) {
            //获取星期标题文本的内容
            String[] item = getCalendarStatus(MASK_CALENDAR_WEEK_TITLE_CHINESE) ? WEEK_DESC_ITEM[0] : WEEK_DESC_ITEM[1];
            //绘制星期标题
            drawWeekTitle(canvas, startX, startY, item);
            //绘制后添加标题高度
            startY += mWeekTitleHeight;
        }

        //若需要绘制上个月数据
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
                DayCell cell = mCacheDateMap.get(month).get(i);
                if (computeIfCellCanSeen(startX, startY)) {
                    drawCell(canvas, startX, startY, cell, MONTH_STATUS_PRE);
                }
                startX += mCellWidth;
            }
        } else {
            //不需要绘制时直接移动绘制的X坐标
            startX += mCellWidth * mAdvanceDayOfMonth;
        }


        //当前选中月份必须绘制的
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
            DayCell cell = mCacheDateMap.get(month).get(i);
            if (computeIfCellCanSeen(startX, startY)) {
                drawCell(canvas, startX, startY, cell, MONTH_STATUS_CURRENT);
            }
            startX += mCellWidth;
        }

        //若需要绘制下个月的数据
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
                DayCell cell = mCacheDateMap.get(month).get(i);
                if (computeIfCellCanSeen(startX, startY)) {
                    drawCell(canvas, startX, startY, cell, MONTH_STATUS_NEXT);
                }
                startX += mCellWidth;
            }
        }
    }

    /**
     * 更新绘制月份的数据
     *
     * @param year              需要更新日期年份
     * @param month             需要更新日期月份
     * @param fromDay           需要更新日期的开始日期
     * @param dayDistance       需要更新日期的天数
     * @param isContainFirstDay 是否包括了指定的第一天开始日期,不管是否包括第一天,都更新{@code dayDistance}天数.
     * @param direction         更新方向,true为向后更新,即从指定日期开始向后计算{@code dayDistance}数量的天数,false为向前更新
     */
    protected void updateMonthDate(int year, int month, int fromDay, int dayDistance, boolean isContainFirstDay, boolean direction) {
        //若不需要强制更新数据并且缓存数据中存在当前的月份数据时,则不更新,节省计算资源
        //此处一般会在日期配置信息没有改动时触发(最典型为仅刷新当前月份而不是滑动界面)
        //或者当滑动界面时则仅会加载一次新的月份数据(在整个滑动周期中)
        if (!mIsForceClearCache && mCacheDateMap.get(month) != null) {
            return;
        }
        mRecycleDate.set(year, month, fromDay);

        int begin = 0;
        SparseArrayCompat<DayCell> monthMap = mRecycleMonthStack.pop();
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
                DayCell outCell = monthMap.get(day);
                //设置日期数据
                setDayCell(outCell, mRecycleDate);
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
                DayCell outCell = monthMap.get(day);
                setDayCell(outCell, mRecycleDate);
                mRecycleDate.add(Calendar.DAY_OF_MONTH, -1);
            }
        }
        mCacheDateMap.put(month, monthMap);
    }

    /**
     * 设置日期数据,{@link DayCell}仅仅只是一个日期数据缓存的对象,本身所有数据来自于计算后得到的数据
     *
     * @param cell        日期对象,一般不可为空;为null时不会进行任何处理
     * @param changedDate 当前需要设置更新的日期数据(daycell大部分数据来自此参数)
     */
    protected void setDayCell(DayCell cell, Calendar changedDate) {
        if (cell == null) {
            return;
        }
        int changedDay, changedMonth, changedYear, weekDay;
        //获取更新日期的年/月/日及星期
        changedDay = changedDate.get(Calendar.DAY_OF_MONTH);
        changedMonth = changedDate.get(Calendar.MONTH);
        changedYear = changedDate.get(Calendar.YEAR);
        weekDay = changedDate.get(Calendar.DAY_OF_WEEK);

        //公历日期
        cell.setDay(changedDay);
        cell.setMonth(changedMonth);
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

            //根据节假日或者加班日设置数据
            if (cell.isHoliday()) {
                cell.setSpecialDate(DayCell.MASK_DATE_HOLIDAY, true);
            }
        }
    }

    /**
     * 绘制单个日期单元格对象
     *
     * @param canvas
     * @param startX      日期单元格开始绘制X位置
     * @param startY      日期单元格开始绘制Y位置
     * @param cell        单元格对象
     * @param monthStatus 绘制月份状态类型<br>
     *                    <li>{@link #MONTH_STATUS_PRE},上个月份
     *                    <li>{@link #MONTH_STATUS_CURRENT},当前选中月份
     *                    <li>{@link #MONTH_STATUS_NEXT},下个月份
     */
    protected void drawCell(Canvas canvas, float startX, float startY, DayCell cell, int monthStatus) {
        if (cell == null) {
            return;
        } else {
            int nowYear, nowMonth, nowDay;
            float tempX, tempY;
            //计算出绘制区域的最小边(绘制工作只会在居中的正方形区域中进行)
            float minSize = Math.min(mCellWidth, mCacheHeight);
            //默认处理颜色值
            int suggestColor = mColor.mNormalDateTextColor;
            //节日颜色值
            int fesTextColor = mColor.mFestivalTextColor;
            //周末颜色值
            int weekendColor = mColor.mWeekendTextColor;
            //农历颜色值
            int lunarColor = mColor.mLunarTextColor;
            //日期颜色值
            int dateTextColor = suggestColor;

            nowYear = mTodayDate.get(Calendar.YEAR);
            nowMonth = mTodayDate.get(Calendar.MONTH);
            nowDay = mTodayDate.get(Calendar.DAY_OF_MONTH);

            //若是绘制上个月或者下个月的数据
            if (monthStatus == MONTH_STATUS_PRE
                    || monthStatus == MONTH_STATUS_NEXT) {
                dateTextColor = mColor.mMinorDateTextColor;
                lunarColor = mColor.mMinorDateTextColor;
                //根据配置要求设置相应的字体颜色
                if (getCalendarStatus(MASK_CALENDAR_MINOR_WEEKEND)) {
                    weekendColor = mColor.mMinorDateTextColor;
                }
                if (getCalendarStatus(MASK_CALENDAR_MINOR_FESTIVAL)) {
                    fesTextColor = mColor.mMinorDateTextColor;
                }
            }
            mDrawRect.set(startX, startY, startX + mCellWidth, startY + mCacheHeight);

            //若周末文本则使用周末文本颜色
            //此参数在这里设置是因为后面有可能日期是被选中日期,则文本颜色为白色
            dateTextColor = cell.isWeekend() ? weekendColor : dateTextColor;
            //选中当天背景色
            if (monthStatus == MONTH_STATUS_CURRENT && computeIfIsSelectedDay(cell.getDay())) {
                dateTextColor = mColor.mSelectDateTextColor;
                mDatePaint.setColor(mColor.mSelectDateBackgroundColor);
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
            if (cell.isToday(nowYear, nowMonth, nowDay)) {
                mDatePaint.setColor(mColor.mSelectDateBackgroundColor);
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
                drawFestivalOrLunarDate(canvas, cell, startX, startY, fesTextColor, mColor.mSelectDateTextColor, lunarColor, minSize);
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

    /**
     * 绘制日期底部的小图标
     *
     * @param canvas
     * @param startX      开始绘制X位置
     * @param startY      开始绘制Y位置
     * @param cellMinSize 单元格小最区域值
     * @param suggestSize 建议使用的图标大小值,默认是使用正方形
     */
    protected void drawBottomDrawable(Canvas canvas, float startX, float startY, float cellMinSize, float suggestSize) {
        float tempX, tempY;
        //绘制图标的X位置,left
        tempX = startX + mCellWidth / 2 - suggestSize / 2;
        //绘制图标的Y位置,top
        tempY = startY + mCacheHeight / 2 + ((mCacheHeight / 2 - suggestSize)) / 2;
        //绘制图标的区域
        mRecycleRectf.set(tempX, tempY, tempX + suggestSize, tempY + suggestSize);

        //若底部图片存在,则使用底部图片绘制
        if (mBottomBmp != null && !mBottomBmp.isRecycled()) {
            canvas.drawBitmap(mBottomBmp, null, mRecycleRectf, mDatePaint);
        } else if (mBottomDraw != null) {
            //若底部图标存在,则使用底部图标
            mBottomDraw.setBounds((int) mRecycleRectf.left, (int) mRecycleRectf.top,
                    (int) mRecycleRectf.right, (int) mRecycleRectf.bottom);
            mBottomDraw.draw(canvas);
        }
    }

    /**
     * 绘制节日或者是农历日期,取决于日期配置信息
     *
     * @param canvas
     * @param cell              日期对象
     * @param startX            开始绘制X位置
     * @param startY            开始绘制Y位置
     * @param fesTextColor      节日字体颜色
     * @param selectedDateColor 选中日期字体颜色
     * @param lunarColor        农历字体颜色
     * @param cellMinSize       绘制区域的最小值
     */
    protected void drawFestivalOrLunarDate(Canvas canvas, DayCell cell, float startX, float startY,
                                           int fesTextColor, int selectedDateColor, int lunarColor, float cellMinSize) {
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

            //不显示农历日期时,清除日期
            if (!getFestivalStatus(MASK_FESTIVAL_LUNAR)) {
                lunarFestival = null;
            }
            //不显示公历节日时,清除节日
            if (!getFestivalStatus(MASK_FESTIVAL_SOLAR)) {
                solarFestival = null;
            }
            //不显示节气时,清除节气
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
            //若日期被选中,则文本需要使用选中字体颜色
            mDatePaint.setColor(computeIfIsSelectedDay(cell.getDay()) ? selectedDateColor : fesTextColor);
            mDatePaint.setTextAlign(Paint.Align.CENTER);

            //绘制在下半圆中
            tempX = startX + mCellWidth / 2;
            tempY = startY + mCacheHeight / 2 + cellMinSize * 1f / 3;
            canvas.drawText(festivalOrDate, tempX, tempY, mDatePaint);
        }
    }


    /**
     * 绘制星期标题
     *
     * @param canvas
     * @param startX
     * @param startY
     * @param weekItems 绘制的星期标题对象,共七个数据
     */
    protected void drawWeekTitle(Canvas canvas, float startX, float startY, String[] weekItems) {
        int begin = mWeekStartDay;
        //设置星期绘制字体颜色
        mDatePaint.setColor(mColor.mWeekTitleTextColor);
        for (int i = 0; i < 7; i++) {
            int index = (begin - 1) % 7;
            if (index >= 0 && index < 7) {
                String item = weekItems[index];
                //依次绘制星期
                drawCommonText(item, startX, startY, mCellWidth, mWeekTitleHeight, canvas, mDatePaint);
                startX += mCellWidth;
            }
            begin++;
        }
    }

    /**
     * 默认的绘制文本的方法,文本的字体大小根据绘制区域动态计算
     *
     * @param text
     * @param startX
     * @param startY
     * @param width  绘制区域的宽度
     * @param height 绘制区域的高度
     * @param canvas
     * @param paint
     */
    protected void drawCommonText(String text, float startX, float startY, float width, float height, Canvas canvas, Paint paint) {
        if (text == null || text.length() <= 0 || width <= 0 || height <= 0) {
            return;
        } else {
            //计算所有文本被绘制时每个文字占用的宽度
            float widthSize = width * 3f / (5 * text.length());
            //计算1/2绘制区域的高度
            float heightSize = height * 1f / 2;
            //计算两者中最小值作为字体大小
            float textSize = Math.min(widthSize, heightSize);

            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetrics fm = paint.getFontMetrics();

            //计算Baseline的位置
            float tempX = startX + width / 2;
            float tempY = startY + (height / 2 + (fm.bottom - fm.top) / 2 - fm.bottom);
            canvas.drawText(text, tempX, tempY, paint);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY})
    public @interface Week {
    }

    /**
     * 设置一周开始的星期
     *
     * @param day 该值只能是Calendar中week相关的数据
     * @return
     */
    public CalendarView setWeekStartDay(@Week int day) {
        mWeekStartDay = day;
        return this;
    }


    /**
     * 设置选中日期
     *
     * @param year
     * @param month 月份从0开始,可使用{@link Calendar}中相关的月份常量
     * @param day
     * @return
     */
    public boolean setSelectedDate(int year, int month, int day) {
        int status = 0;
        if (year >= 1970 && year <= 2050) {
            mSelectedYear = year;
            status |= 0b001;
        }
        if (month >= 0 && month <= 11) {
            mSelectedMonth = month;
            status |= 0b010;
        }
        if (day >= 1 && day <= 31) {
            //TODO:校验设置的日期
            mSelectedDay = day;
            status |= 0b100;
        }
        return status == 0b111;
    }

    /**
     * 设置选中年份
     *
     * @param year
     * @return
     */
    public boolean setSelectedYear(int year) {
        return setSelectedDate(year, mSelectedMonth, mSelectedDay);
    }

    /**
     * 设置选中月份,月份从0开始,可使用{@link Calendar}中相关的月份常量
     *
     * @param month
     * @return
     */
    public boolean setSelectedMonth(int month) {
        return setSelectedDate(mSelectedYear, month, mSelectedDay);
    }

    /**
     * 设置选中日期
     *
     * @param day
     * @return
     */
    public boolean setSelectedDay(int day) {
        return setSelectedDate(mSelectedYear, mSelectedMonth, day);
    }

    /**
     * 获取选中年份
     *
     * @return
     */
    public int getSelectedYear() {
        return mSelectedYear;
    }

    /**
     * 获取选中月份
     *
     * @return
     */
    public int getSelectedMonth() {
        return mSelectedMonth;
    }

    /**
     * 获取选中日期
     *
     * @return
     */
    public int getSelectedDay() {
        return mSelectedDay;
    }


    /**
     * 设置固定的星期标题栏高度
     *
     * @param heightPx
     * @return
     */
    public boolean setFixWeekTitleHeight(float heightPx) {
        if (heightPx >= 0) {
            mFixWeekTitleHeight = heightPx;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取颜色配置对象
     *
     * @return
     */
    public ColorSetting getColorSetting() {
        return mColor;
    }

    /**
     * 设置颜色配置对象
     *
     * @param color calendar所有使用到的字体颜色及背景色的配置对象
     * @return
     */
    public CalendarView setColorSetting(ColorSetting color) {
        if (color != null) {
            mColor = color;
            invalidate();
        }
        return this;
    }

    /**
     * 重置为选中日期为今天并跳转到今天的日期
     */
    public CalendarView resetToToday() {
        mTodayDate.setTimeInMillis(System.currentTimeMillis());
        if (mSelectedYear != mTodayDate.get(Calendar.YEAR)) {
            //强制刷新数据
            mIsForceClearCache = true;
        }
        mSelectedYear = mTodayDate.get(Calendar.YEAR);
        mSelectedMonth = mTodayDate.get(Calendar.MONTH);
        mSelectedDay = mTodayDate.get(Calendar.DAY_OF_MONTH);
        invalidate();
        return this;
    }


    /**
     * 获取当前日历的上部分未实际显示日期的高度(可能是星期标题等)
     *
     * @return
     */
    public float getExceptCalendarTopHeight() {
        return getCalendarStatus(MASK_CALENDAR_WEEK_TITLE) ? mWeekTitleHeight : 0;
    }


    /**
     * 重置日历配置为默认的配置信息
     *
     * @return
     */
    public CalendarView resetCalendarStatus() {
        //日期信息,默认显示次要月份,并且次要月份全部不高亮,显示农历日期及周末标题栏
        mCalendarMask = MASK_CALENDAR_MINOR_MONTH
                | MASK_CALENDAR_PREVIOUS_MONTH | MASK_CALENDAR_NEXT_MONTH
                | MASK_CALENDAR_MINOR_WEEKEND | MASK_CALENDAR_MINOR_FESTIVAL
                | MASK_CALENDAR_WEEK_TITLE | MASK_CALENDAR_WEEK_TITLE_CHINESE
                | MASK_CALENDAR_LUNAR_DATE
                | MASK_CALENDAR_SCROLL | MASK_CALENDAR_SCROLL_HORIZONTAL | MASK_CALENDAR_SCROLL_VERTICAL;
        return this;
    }

    /**
     * 重置节日配置为默认的配置信息
     *
     * @return
     */
    public CalendarView resetFestivalStatus() {
        //默认显示节日,农历优先并显示所有农历/公历/节气
        // 0b0001_1111
        mFestivalMask = MASK_FESTIVAL_SHOW
                | MASK_FESTIVAL_LUNAR | MASK_FESTIVAL_SOLAR | MASK_FESTIVAL_SOLAR_TERM
                | MASK_FESTIVAL_LUNAR_FIRST;
        return this;
    }

    /**
     * 获取某个日历配置的状态,true为该配置已经设置或者要求显示,false为不使用该配置
     *
     * @param mask 日历配置<br>
     *             <li>{@link #MASK_CALENDAR_LUNAR_DATE},显示农历日期</li>
     *             <li>{@link #MASK_CALENDAR_MINOR_FESTIVAL},次要月份节日颜色使用次要字体颜色</li>
     *             <li>{@link #MASK_CALENDAR_MINOR_MONTH},显示将要月份(暂时未使用)</li>
     *             <li>{@link #MASK_CALENDAR_MINOR_WEEKEND},次要月份周末颜色使用次要字体颜色</li>
     *             <li>{@link #MASK_CALENDAR_NEXT_MONTH},显示下个月份(次要月份)</li>
     *             <li>{@link #MASK_CALENDAR_PREVIOUS_MONTH},显示上个月份(次要月份)</li>
     *             <li>{@link #MASK_CALENDAR_SCROLL},允许日历滑动切换月份</li>
     *             <li>{@link #MASK_CALENDAR_SCROLL_HORIZONTAL},允许横向日历滑动切换月份</li>
     *             <li>{@link #MASK_CALENDAR_SCROLL_VERTICAL},允许纵向日历滑动切换月份</li>
     *             <li>{@link #MASK_CALENDAR_TAG},日历标志,暂时未使用</li>
     *             <li>{@link #MASK_CALENDAR_WEEK_TITLE},日历星期标题</li>
     *             <li>{@link #MASK_CALENDAR_WEEK_TITLE_CHINESE},星期标题使用中文</li>
     *             <li>{@link #MASK_CALENDAR_WEEK_TITLE_FIX},星期标题高度固定</li>
     * @return
     */
    public boolean getCalendarStatus(int mask) {
        return (mCalendarMask & mask) != 0;
    }

    /**
     * 为已有的日历配置中添加新的配置要求,如果需要清除所有配置重新设置使用{@link #setCalendarFlag(int)}
     *
     * @param mask 日历配置<br>
     *             <li>{@link #MASK_CALENDAR_LUNAR_DATE},显示农历日期</li>
     *             <li>{@link #MASK_CALENDAR_MINOR_FESTIVAL},次要月份节日颜色使用次要字体颜色</li>
     *             <li>{@link #MASK_CALENDAR_MINOR_MONTH},显示将要月份(暂时未使用)</li>
     *             <li>{@link #MASK_CALENDAR_MINOR_WEEKEND},次要月份周末颜色使用次要字体颜色</li>
     *             <li>{@link #MASK_CALENDAR_NEXT_MONTH},显示下个月份(次要月份)</li>
     *             <li>{@link #MASK_CALENDAR_PREVIOUS_MONTH},显示上个月份(次要月份)</li>
     *             <li>{@link #MASK_CALENDAR_SCROLL},允许日历滑动切换月份</li>
     *             <li>{@link #MASK_CALENDAR_SCROLL_HORIZONTAL},允许横向日历滑动切换月份</li>
     *             <li>{@link #MASK_CALENDAR_SCROLL_VERTICAL},允许纵向日历滑动切换月份</li>
     *             <li>{@link #MASK_CALENDAR_TAG},日历标志,暂时未使用</li>
     *             <li>{@link #MASK_CALENDAR_WEEK_TITLE},日历星期标题</li>
     *             <li>{@link #MASK_CALENDAR_WEEK_TITLE_CHINESE},星期标题使用中文</li>
     *             <li>{@link #MASK_CALENDAR_WEEK_TITLE_FIX},星期标题高度固定</li>
     * @return
     */
    public CalendarView addCalendarFlag(int mask) {
        mCalendarMask |= mask;
        return this;
    }

    /**
     * 移除某个已经设置的日历配置,不建议组合多个配置同时移除
     *
     * @param mask
     * @return
     */
    public CalendarView removeCalendarFlag(int mask) {
        mCalendarMask &= ~mask;
        return this;
    }

    /**
     * 清除已存在的日历配置信息并重新设置为其它配置.仅添加/删除某个配置使用{@link #addCalendarFlag(int)}和{@link #removeCalendarFlag(int)}<br>
     * 多个配置可以通过 {@code |} 的形式组合并设置
     *
     * @param flag 日历配置<br>
     *             <li>{@link #MASK_CALENDAR_LUNAR_DATE},显示农历日期</li>
     *             <li>{@link #MASK_CALENDAR_MINOR_FESTIVAL},次要月份节日颜色使用次要字体颜色</li>
     *             <li>{@link #MASK_CALENDAR_MINOR_MONTH},显示将要月份(暂时未使用)</li>
     *             <li>{@link #MASK_CALENDAR_MINOR_WEEKEND},次要月份周末颜色使用次要字体颜色</li>
     *             <li>{@link #MASK_CALENDAR_NEXT_MONTH},显示下个月份(次要月份)</li>
     *             <li>{@link #MASK_CALENDAR_PREVIOUS_MONTH},显示上个月份(次要月份)</li>
     *             <li>{@link #MASK_CALENDAR_SCROLL},允许日历滑动切换月份</li>
     *             <li>{@link #MASK_CALENDAR_SCROLL_HORIZONTAL},允许横向日历滑动切换月份</li>
     *             <li>{@link #MASK_CALENDAR_SCROLL_VERTICAL},允许纵向日历滑动切换月份</li>
     *             <li>{@link #MASK_CALENDAR_TAG},日历标志,暂时未使用</li>
     *             <li>{@link #MASK_CALENDAR_WEEK_TITLE},日历星期标题</li>
     *             <li>{@link #MASK_CALENDAR_WEEK_TITLE_CHINESE},星期标题使用中文</li>
     *             <li>{@link #MASK_CALENDAR_WEEK_TITLE_FIX},星期标题高度固定</li>
     * @return
     */
    public CalendarView setCalendarFlag(int flag) {
        mCalendarMask = flag;
        return this;
    }

    /**
     * 获取某个节日配置的状态,true为该配置已经设置或者要求显示,false为不使用该配置
     *
     * @param mask 节日配置<br>
     *             <li>{@link #MASK_FESTIVAL_LUNAR},显示农历节日</li>
     *             <li>{@link #MASK_FESTIVAL_LUNAR_FIRST},优先显示农历节日</li>
     *             <li>{@link #MASK_FESTIVAL_SHOW},显示节日信息</li>
     *             <li>{@link #MASK_FESTIVAL_SOLAR},显示公历节日</li>
     *             <li>{@link #MASK_FESTIVAL_SOLAR_FIRST},优先显示公历节日</li>
     *             <li>{@link #MASK_FESTIVAL_SOLAR_TERM},显示节气</li>
     *             <li>{@link #MASK_FESTIVAL_SOLAR_TERM_FIRST},优先显示节气</li>
     * @return
     */
    public boolean getFestivalStatus(int mask) {
        return (mFestivalMask & mask) != 0;
    }

    /**
     * 为已有的节日配置中添加新的配置要求,如果需要清除所有配置重新设置使用{@link #setFestvalFlag(int)}
     *
     * @param mask 节日配置<br>
     *             <li>{@link #MASK_FESTIVAL_LUNAR},显示农历节日</li>
     *             <li>{@link #MASK_FESTIVAL_LUNAR_FIRST},优先显示农历节日</li>
     *             <li>{@link #MASK_FESTIVAL_SHOW},显示节日信息</li>
     *             <li>{@link #MASK_FESTIVAL_SOLAR},显示公历节日</li>
     *             <li>{@link #MASK_FESTIVAL_SOLAR_FIRST},优先显示公历节日</li>
     *             <li>{@link #MASK_FESTIVAL_SOLAR_TERM},显示节气</li>
     *             <li>{@link #MASK_FESTIVAL_SOLAR_TERM_FIRST},优先显示节气</li>
     * @return
     */
    public CalendarView addFestivalFlag(int mask) {
        mFestivalMask |= mask;
        return this;
    }

    /**
     * 移除某个已经设置的节日配置,不建议组合多个配置同时移除
     *
     * @param mask
     * @return
     */
    public CalendarView removeFestivalFlag(int mask) {
        mFestivalMask &= ~mask;
        return this;
    }

    /**
     * 清除已存在的节日配置信息并重新设置为其它配置.仅添加/删除某个配置使用{@link #addFestivalFlag(int)} 和{@link #removeFestivalFlag(int)} <br>
     * 多个配置可以通过 {@code |} 的形式组合并设置
     *
     * @param flag 节日配置<br>
     *             <li>{@link #MASK_FESTIVAL_LUNAR},显示农历节日</li>
     *             <li>{@link #MASK_FESTIVAL_LUNAR_FIRST},优先显示农历节日</li>
     *             <li>{@link #MASK_FESTIVAL_SHOW},显示节日信息</li>
     *             <li>{@link #MASK_FESTIVAL_SOLAR},显示公历节日</li>
     *             <li>{@link #MASK_FESTIVAL_SOLAR_FIRST},优先显示公历节日</li>
     *             <li>{@link #MASK_FESTIVAL_SOLAR_TERM},显示节气</li>
     *             <li>{@link #MASK_FESTIVAL_SOLAR_TERM_FIRST},优先显示节气</li>
     * @return
     */
    public CalendarView setFestvalFlag(int flag) {
        mFestivalMask = flag;
        return this;
    }

}
