[TOC]

# `IDrawCallback`方法及参数说明

## createDayCell
```
    /**
     * 创建日期对象,日期对象必须是{@link DayCell},此部分的日期对象是缓存并且会被复用的
     *
     * @return 此方法不可返回空对象
     */
    @NonNull
    DayCell createDayCell();
```

## updateDayCellAfterNewSetting
```
    /**
     * 在设置日期对象数据后回调,需要对日期对象相关数据处理时可在此接口进行处理
     *
     * @param cell
     */
    void updateDayCellAfterNewSetting(@NonNull DayCell cell);
```

## beforeCellDraw
```
    /**
     * 某个具体日期绘制前的回调,提供该日期绘制期间不变的参数及信息;<br>
     * 若需要在最底层绘制自定义的某些界面或数据也可以在此处绘制处理;<br>
     * 此绘制所有绘制的界面与其它绘制内容重叠部分会被覆盖
     *
     * @param view     当前绘制的日历控件
     * @param drawArea 绘制区域
     * @param color    颜色配置对象
     * @param cell     绘制日期
     * @param minSize  绘制区域的最小边长
     * @param textSize 日期字体大小(与节日字体大小无关)
     * @param canvas
     * @param paint
     */
    void beforeCellDraw(@NonNull AbsCalendarView view, @NonNull RectF drawArea, @NonNull ColorSetting color, @NonNull DayCell cell, float minSize, float textSize, Canvas canvas, Paint paint);
```

## afterCellDraw
```
    /**
     * 某个具体日期绘制后的回调,在该日期最顶层需要绘制自定义界面或数据可以在此处绘制<br>
     * 此处所有绘制界面会覆盖其它任何绘制内容
     *
     * @param canvas
     * @param paint
     */
    void afterCellDraw(Canvas canvas, Paint paint);
```

## drawWeekTitleBackground
```
    /**
     * 绘制周末标题的背景色
     *
     * @param canvas
     * @param drawArea 绘制区域
     * @param paint
     */
    void drawWeekTitleBackground(Canvas canvas, @NonNull RectF drawArea, Paint paint);
```

## drawWeekTitle
```
    /**
     * 绘制周末标题文本<br>
     * 默认文本绘制方式为居中绘制,即X坐标为文本中心X位置,Y坐标为文本baseline位置
     *
     * @param canvas
     * @param drawArea 绘制区域
     * @param weekTag  周末标识,周日到周一,用于自定义标题绘制时识别当前绘制星期
     * @param textX    文本绘制X轴位置
     * @param textY    文本绘制Y轴位置
     * @param textSize 推荐字体大小
     * @param week     周末文本
     * @param paint
     */
    void drawWeekTitle(Canvas canvas, @NonNull RectF drawArea, @AbsCalendarView.Week int weekTag, float textX, float textY, float textSize, @NonNull String week, Paint paint);
```

## drawDateBackground
```
    /**
     * 绘制日期背景色,默认为透明
     *
     * @param canvas
     * @param recommendRectf 推荐绘制区域(正方形,可直接绘制为圆形)
     * @param isToday        当前日期是否今日
     * @param isSelected     当前日期是否选中日期
     * @param paint
     */
    void drawDateBackground(Canvas canvas, @NonNull RectF recommendRectf, boolean isToday, boolean isSelected, Paint paint);
```

## drawSelectedDayBackground
```
    /**
     * 绘制选中日期背景色,默认绘制为圆形背景
     *
     * @param canvas
     * @param recommendRectf 推荐绘制区域(正方形,可直接绘制为圆形)
     * @param drawable       绘制背景drawable
     * @param isToday        当前日期是否今日
     * @param paint
     */
    void drawSelectedDayBackground(Canvas canvas, @NonNull RectF recommendRectf, @Nullable Drawable drawable, boolean isToday, Paint paint);
```

## drawTodayBackground
```
    /**
     * 绘制今日日期背景色,默认绘制为边缘圆环
     *
     * @param canvas
     * @param recommendRectf 推荐绘制区域(正方形,可直接绘制为圆形)
     * @param drawable       绘制背景drawable
     * @param paint
     */
    void drawTodayBackground(Canvas canvas, @NonNull RectF recommendRectf, @Nullable Drawable drawable, Paint paint);
```

## drawDateText
```
    /**
     * 绘制日期文本<br>
     * 默认文本绘制方式为居中绘制,即X坐标为文本中心X位置,Y坐标为文本baseline位置
     *
     * @param canvas
     * @param isToday    当前日期是否为今日
     * @param isSelected 当前日期是否为选中日期
     * @param color      推荐文本颜色(已处理是否当前日期或者选中日期)
     * @param textSize   推荐字体大小
     * @param x          居中X轴坐标
     * @param y          baselineY轴坐标
     * @param date       日期文本
     * @param paint
     */
    void drawDateText(Canvas canvas, boolean isToday, boolean isSelected, int color, float textSize, float x, float y, @NonNull String date, Paint paint);
```

## isNeedDrawBottomDrawable
```
    /**
     * 是否需要绘制底部图标,若返回true,则绘制底部图标,放弃绘制节日或农历日期;若返回False,则绘制节日或农历日期
     *
     * @param cell       当前日期对象
     * @param isToday    当前日期是否为今日
     * @param isSelected 当前日期是否为选中日期
     * @return
     */
    boolean isNeedDrawBottomDrawable(@NonNull DayCell cell, boolean isToday, boolean isSelected);
```

## drawBottomDrawable
```
    /**
     * 绘制底部图标,当{@link #isNeedDrawBottomDrawable(DayCell, boolean, boolean)}返回true时回调
     *
     * @param canvas
     * @param isToday        当前日期是否今日
     * @param isSelected     当前日期是否选中日期
     * @param bmp            背景图
     * @param drawable       背景drawable
     * @param recommendRectf 推荐绘制区域(底部居中位置显示)
     * @param paint
     */
    void drawBottomDrawable(Canvas canvas, boolean isToday, boolean isSelected, @Nullable Bitmap bmp, @Nullable Drawable drawable, @NonNull RectF recommendRectf, Paint paint);
```

## drawFestivalOrLunarDate
```
    /**
     * 绘制节日或者农历日期文本<br>
     * 默认文本绘制方式为居中绘制,即X坐标为文本中心X位置,Y坐标为文本baseline位置
     *
     * @param canvas
     * @param isToday            当前日期是否今日
     * @param isSelected         当前日期是否选中日期
     * @param isLunar            是否绘制农历日期文本(false为节日文本)
     * @param color              推荐文本颜色
     * @param textSize           推荐字体大小
     * @param recommendMaxHeight 绘制可用最大高度(用于限制字体大小及显示)
     * @param x                  居中X轴坐标
     * @param y                  baselineY轴坐标
     * @param festivalOrDate     节日文本或者农历日期文本
     * @param paint
     */
    void drawFestivalOrLunarDate(Canvas canvas, boolean isToday, boolean isSelected, boolean isLunar, int color, float textSize, float recommendMaxHeight, float x, float y, @NonNull String festivalOrDate, Paint paint);
```

## drawHolidayDate
```
    /**
     * 绘制假期图标,此绘制是在日期上叠加,会挡住部分日期并且默认显示在左上角
     *
     * @param canvas
     * @param isToday        当前日期是否今日
     * @param isSelected     当前日期是否选中日期
     * @param drawable       绘制使用的drawable
     * @param recommendRectf 推荐绘制区域(正方形并位于左上角)
     * @param paint
     */
    void drawHolidayDate(Canvas canvas, boolean isToday, boolean isSelected, @Nullable Drawable drawable, @NonNull RectF recommendRectf, Paint paint);
```

## drawWorkDate
```
    /**
     * 绘制加班图标,此绘制是在日期上叠加,会挡住部分日期并默认显示在左上角
     *
     * @param canvas
     * @param isToday        当前日期是否今日
     * @param isSelected     当前日期是否选中日期
     * @param drawable       绘制使用的Drawable
     * @param recommendRectf 推荐绘制区域(正方形并位于左上角)
     * @param paint
     */
    void drawWorkDate(Canvas canvas, boolean isToday, boolean isSelected, @Nullable Drawable drawable, @NonNull RectF recommendRectf, Paint paint);
```

# `IActionCallback`方法及参数说明

## onSelectedDayChanged
```
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
```

## onResetToToday
```
    /**
     * 当日期被重置为今天时回调
     *
     * @param nowYear  今天的年份
     * @param nowMonth 今天的月份
     * @param nowDay   今天的日期
     */
    void onResetToToday(int nowYear, int nowMonth, int nowDay);
```