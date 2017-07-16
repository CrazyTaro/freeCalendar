[TOC]
# calendar
a useful calendar which you can custom its styles or actions easily.

# 概述
日历控件并不是会很常用到,但是在项目中用到的话很多时候是更倾向于UI自定义程度会比较高或者带有自己应用的特点.

github上已经存在很多很不错的日历控件.但是绝大多数的特点是该日历特有的并且UI的修改与控制也会很麻烦.

基于这个原因,所以就写了这个日历控件库希望**本身功能比较完整的情况下同时也能提供比较方便容易的自定义UI**的功能

# 版本更新内容
- 0.1.0,首次发布
- 0.2.0,新增周末标题文本可自定义的功能
- 0.3.0,文档调整,新增注释说明
- 0.4.0,打包带源码及注释的库
- 0.5.0

```
1.修复假期/加班图标可能无法正常显示的问题
2.修复日期工具可能导致出错的问题
3.修复xml属性设置显示不正常的问题
4.更新接口参数
```

# 功能特点
- 丰富的xml属性设置可以满足大部分的需要并且能直接在xml中预览出效果
- 大量的自定义绘制方法可直接控制日历的UI显示
- 自定义UI绘制采用接口回调的方式不需要继承任何View直接重写相关的方法即可

# TODO
- [] 优化绘制的方式和逻辑(长期工作)
- [] 调整"假期/加班"图标绘制方式
- [] 分离并独立农历/公历节日计算
- [] 提供一些日期相关的工具方法或接口

# 使用方式
## step1:在项目级别的`build.gradle`文件下声明仓库

```
allprojects {
	repositories {
		...
		maven { url 'https://www.jitpack.io' }
	}
}
```

## step2:引用需要的版本

最新版本: [![](https://jitpack.io/v/CrazyTaro/freeCalendar.svg)](https://jitpack.io/#CrazyTaro/freeCalendar)
```
dependencies {
        compile 'com.github.CrazyTaro:freeCalendar:latest_release'
}
```

## step3:在xml中使用

```
<com.taro.calendar.lib.CalendarView
        android:id="@+id/cv_calendar"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

## step4:自定义UI控制

### 颜色样式配置
大部分的颜色可以在xml中直接设置,也可以在代码中进行设置;所有的颜色配置都在`ColorSetting`的类中.

```
view.getColorSetting()
        .setBackgroundColor(ColorSetting.DEFAULT_COLOR_BLUE)
        .setDateBackgroundColor(Color.parseColor("#ff9900"));
```

也可以`new ColorSetting()`并配置后再设置给`calendarView`

---

### 假期/加班图标设置
由于默认UI是不处理并显示任何图标的,假期及加班图标的设置需要重写部分绘制UI.

```
@Override
public void updateDayCellAfterNewSetting(@NonNull DayCell cell, @NonNull Lunar lunarDate) {
    //农历日期类计算当前日期是需要休假的
    //现在正在调整节日的计算及处理,后面可能就不再是进行类似的判断
    //之后根据调整后的节日计算再判断当前日期是否需要休假,这部分将会考虑被独立出来处理
    if (lunarDate.isHoliday()) {
        //必须对日期对象中相关的特殊日期标识进行设置才有效
        //需要显示底部图标时也需要设置相关的标识
        //MASK_DATE_HOLIDAY,假期标识
        //MASK_DATE_WORK,加班标识
        //MASK_DATE_BOTTOM_DRAWABLE,显示底部图标标识
        cell.setSpecialDate(DayCell.MASK_DATE_HOLIDAY, true);
    }
}
```

设置了显示假期图标或者加班图标后,还可以设置假期或加班图标的标识;该图标的类型是`TipDrawable`,通过调用对应的方法即可设置相关样式.包括背景色/文本等;获取默认的假期/加班图标可调用`calendarView`的方法`getWorkTipDrawable`/`getFestivalDrawable`;

```
view.getFestivalDrawable()
        //设置图标文本
        .setText("啊")
        //设置文本颜色
        .setTextColor(Color.WHITE)
        //设置图标背景色
        .setBackgroundColor(Color.BLACK);
```

---

### 完全自定义UI
继承`BaseCalendarDrawHelper`,对需要修改或者控制的UI进行重写即可.

其中`BaseCalendarDrawHelper`是一个默认实现了所有绘制接口的类,默认的控件`CalendarView`的绘制操作即使用此类的实例.该类是实现了`IDrawCallback`的绘制接口.

直接实现`IDrawCallback`也是一样的,但一次需要实现的接口有点多,推荐还是继承`BaseCalendarDrawHelper`进行并重写相关方法即可;

```
public class BaseCalendarDrawHelper implements IDrawCallback {
    //实现对应的方法...
}
```

在回调的方法中,实际上需要使用的样式已经设置在`paint`中,如果不需要改动样式直接就可以绘制自己需要的UI

```
public class BaseCalendarDrawHelper implements IDrawCallback {
    @Override
    public void drawWeekTitleBackground(Canvas canvas, @NonNull RectF drawArea, Paint paint) {
        canvas.drawRect(drawArea, paint);
    }

    @Override
    public void drawSelectedDayBackground(Canvas canvas, @NonNull RectF recommendRectf, @Nullable Drawable drawable, boolean isToday, Paint paint) {
        if (drawable != null) {
            //存在drawable使用背景drawable绘制
            drawable.setBounds((int) recommendRectf.left, (int) recommendRectf.top,
                    (int) recommendRectf.right, (int) recommendRectf.bottom);
            drawable.draw(canvas);
        } else {
            //否则绘制圆形背景
            canvas.drawArc(recommendRectf, 0, 360, false, paint);
        }
    }

    @Override
    public void drawDateText(Canvas canvas, boolean isToday, boolean isSelected, int color, float textSize, float x, float y, @NonNull String date, Paint paint) {
        canvas.drawText(date, x, y, paint);
    }

    //其它绘制方法...
}
```

以上给出了几个默认绘制方法的实现,可见看到实际上只需要调用`canvas`默认对应的方法就可以进行绘制了(图形或者是文字等);

当需要自己调整绘制的UI时,则可以使用方法提供的参数自行调整.如上默认提供的推荐区域为正方形,但是可以将UI绘制为圆形.具体视需求而定.

**重要提示:`BaseCalendarDrawHelper`中基本每个方法都实现了相关的绘制逻辑,不需要默认的UI绘制方式则重写时注意不要调用`super`方法**,以上示例为`BaseCalendarDrawHelper`的实现,具体每个方法的默认实现请查看该类的源码.

更多接口方法及说明请查看以下内容

# 效果图
- 默认效果(带农历日期/节日/日期不居中/显示次要月份)

![](https://raw.githubusercontent.com/CrazyTaro/freeCalendar/master/screenshot/normal.png)

---

- 文本居中(仔细看主要的元素都集中在下半部分,可能会感觉有点挤)

![](https://raw.githubusercontent.com/CrazyTaro/freeCalendar/master/screenshot/date_in_center.png)

---

- 无农历日期

![](https://raw.githubusercontent.com/CrazyTaro/freeCalendar/master/screenshot/lunar_date_no_show.png)

---

- 不显示次要月份

![](https://raw.githubusercontent.com/CrazyTaro/freeCalendar/master/screenshot/minor_month_no_show.png)

---

- 简洁文本居中(日期显示居中,会显得清楚端正)

![](https://raw.githubusercontent.com/CrazyTaro/freeCalendar/master/screenshot/simple_date_in_center.png)

---

- 自定义周末标题

![](https://raw.githubusercontent.com/CrazyTaro/freeCalendar/master/screenshot/custom_week_title.png)

---

- 显示假期/加班图标

![](https://raw.githubusercontent.com/CrazyTaro/freeCalendar/master/screenshot/festival_icon.png)

- 滑动效果

![](https://raw.githubusercontent.com/CrazyTaro/freeCalendar/master/screenshot/calendar.gif)

---

- 丰富的可设置属性

![](https://raw.githubusercontent.com/CrazyTaro/freeCalendar/master/screenshot/calendar_xml_attr.gif)

# 接口及属性参考
## xml属性及意义
- 节日(包含农历/公历节日及节气)属性

节日相关的属性值类型都相同,仅列出第一个

|属性名称|属性意义|属性值类型|
|--|--|--|
|festivalShowSolarTermFirst|是否优先显示节气|false|
|festivalShowSolarFirst|是否优先显示公历节日|
|festivalShowLunarFirst|是否优先显示农历节日|
|festivalSolarTermShow|是否显示节气|
|festivalLunarShow|是否显示农历节日|
|festivalSolarShow|是否显示公历节日|
|festivalShow|是否显示节日|

- 日历(设置或样式)属性

日历相关的属性值类型都相同,仅列出第一个

|属性名称|属性意义|属性值类型|
|--|--|--|
|calendarScrollVertical|是否允许纵向滑动|false|
|calendarScrollHorizontal|是否允许横向滑动|
|calendarScrollEnable|是否允许滑动|
|calendarWeekTitleChinese|是否使用中文的周末标题|
|calendarWeekTitleHeightFix|是否使用固定高度的周末标题|
|calendarWeekTitleShow|是否显示周末标题|
|calendarMinorFestivalColor|次要月份节日字体颜色是否使用次要颜色|
|calendarMinorWeekendColor|次要月份周末字体颜色是否使用次要颜色|
|calendarLunarDateShow|是否显示农历日期|
|calendarNextMonthShow|是否显示下个月数据|
|calendarPreMonthShow|是否显示上个月数据|
|calendarDateInCenter|是否日历文本(日期)居中显示|
|calendarScrollNoAnimation|是否滑动切换界面不使用动画|

关于部分属性说明可能有点模糊的下面特别说明一下.

1.次要月份指非当前月份的上个月份和下个月份
```
如当前月份为5月,则次要月份为4,6月;
当前月份为6月时,则次要月份为5,7月;
即效果图中灰色部分月份
```
2.次要颜色是指用于次要月份的主要文本颜色

3.次要月份的节日/周末颜色默认跟随次要颜色,对应属性设置为`false`时则使用设置的节日/周末颜色
```
若设置周末颜色为红色,次要颜色为灰色,当`calendarMinorWeekendColor`为false时,则次要月份的周末颜色为`红色`,否则为`灰色`
```
4.优先显示节日属性只有该节日允许显示时才有效

5.当所有优先显示属性都设置(或者同时设置了多个时),实际的显示顺序永远如下:
```
农历节日->公历节日->节气,当其中某个优先属性没有设置时,则忽略该节日;
若设置农历节日优先,但当天不存在农历节日,同时存在公历节日及节气时,则优先显示公历节日;
```

- 颜色相关属性

|属性名称|属性意义|属性值类型|
|--|--|--|
|backgroundColor|背景色|color|
|dateBackgroundColor|文本背景色|
|defaultTextColor|默认文本颜色|
|minorTextColor|次要文本颜色|
|weekendTextColor|周末文本颜色|
|festivalTextColor|节日文本颜色|
|selectedDayTextColor|选中日期文本颜色|
|todayDayTextColor|今天文本颜色|
|selectedBackgroundColor|选中日期背景色|
|todayBackgroundColor|今天背景色|

- 其它样式相关属性

|属性名称|属性意义|属性值类型|
|--|--|--|
|startWeekDay|一周开始的时间|enum,直接使用`sunday`等声明|
|selectedDay|当前选中日期(仅天)|integer|
|bottomDrawable|日历底部的drawable|reference|
|festivalDrawable|节日(假期)drawable`废弃`|reference|
|workDrawable|加班drawable`废弃`|reference|
|initYear|初始年份|integer|
|initMonth|初始月份|integer|
|initDay|初始日期|integer|
|weekTitleFixHeight|周末标题的固定高度|dimen|
|horizontalScrollRate|横向滑动切换界面的触发比率|float,0-1|
|verticalScrollRate|垂直滑动切换界面的触发比率|float,0-1|

部分属性说明如下:

1.节日及加班drawable是针对国内常见的节日及加班调休的机制,可以在日历中显示出当天是否节日或者是否需要加班;该属性不可用是因为该drawable的类型必须是`TipDrawable`;当需要完全自定义该图标的UI时,可在drawHelper中自行处理

2.滑动触发比率是指在该界面滑动的距离相对于view的宽(横向)高(垂直)长度的比率,当超过该比率时,则触发月份的切换,否则松开将回来当前月份(即界面不变);
```
该属性在界面大小变动比较大时会比较实用,默认为0.3f;当日历填充屏幕时,由于屏幕高度比较高,0.3f的高度比率可能用户手指滑动不太方便,可以调整该比率小一点更适合滑动;
```

## 行为操作回调接口方法
在对日历进行一些操作时,会有相应的方法进行回调处理;

|方法名称|返回值|说明|
|--|--|--|
|onSelectedDayChanged|-|当选中日期改变时该方法会回调(不管只有日期还是全部)|
|onResetToToday|-|当日期被重置为今天时回调|

## 绘制接口方法及功能
UI的绘制是通过`IDrawCallback`接口进行回调的.即使是默认可直接使用的`CalendarView`也是通过默认的`BaseCalendarDrawHelper`进行绘制UI;

当然在样式不需要改变很多的情况下,绝大部分的逻辑与判断已经处理好了并且仅仅只需要调用`canvas`对应的绘制方法进行绘制即可.

**绘制接口有点多,做好心理准备哦**

### 接口方法及参数说明

|方法名称|返回值|说明|
|--|--|--|
|createDayCell|DayCell|创建日期对象,日期对象必须是`DayCell`,此部分的日期对象是缓存并且会被复用的|
|updateDayCellAfterNewSetting|-|在设置日期对象数据后回调,需要对日期对象相关数据处理时可在此接口进行处理|
|beforeCellDraw|-|某个具体日期绘制前的回调,提供该日期绘制期间不变的参数及信息;若需要在最底层绘制自定义的某些界面或数据也可以在此处绘制处理;此绘制所有绘制的界面与其它绘制内容重叠部分会被覆盖|
|afterCellDraw|-|某个具体日期绘制后的回调,在该日期最顶层需要绘制自定义界面或数据可以在此处绘制;此处所有绘制界面会覆盖其它任何绘制内容|
|drawWeekTitleBackground|-|绘制周末标题的背景色|
|drawWeekTitle|-|绘制周末标题文本|
|drawDateBackground|-|绘制日期背景色,默认为透明|
|drawSelectedDayBackground|-|绘制选中日期背景色,默认绘制为圆形背景|
|drawTodayBackground|-|绘制今日日期背景色,默认绘制为边缘圆环|
|drawDateText|-|绘制日期文本|
|isNeedDrawBottomDrawable|boolean|是否需要绘制底部图标,若返回true,则绘制底部图标,放弃绘制节日或农历日期;若返回False,则绘制节日或农历日期|
|drawBottomDrawable|-|绘制底部图标,当`isNeedDrawBottomDrawable`返回true时回调|
|drawFestivalOrLunarDate|-|绘制节日或者农历日期文本|
|drawHolidayDate|-|绘制假期图标,此绘制是在日期上叠加,会挡住部分日期并且默认显示在左上角|
|drawWorkDate|-|绘制加班图标,此绘制是在日期上叠加,会挡住部分日期并默认显示在左上角|

### 部分重点方法说明

有部分方法在`BaseCalendarDrawHelper`中并没有实现具体代码,但是实际上是有很重要的预备作用的.下面重点说一下这几个方法.具体可直接查看源码.

- `createDayCell`-创建日期对象方法

创建日期对象,日期对象必定是`DayCell`类型,但是这里是允许自定义创建对象的.所以当实际需要处理的日期对象数据包含了其它自定义的数据类型时,可以继承自`DayCell`进行返回;**日期对象数据是会缓存起来复用的,请尽量不要在子类中引用Activity等临时性比较强的对象,防止内在泄漏**

---

- `updateDayCellAfterNewSetting`-更新了dayCell数据后的回调

由于`DayCell`会被复用,所以当切换月份或者其它任何被复用的情况下,dayCell都会被重新赋值,赋值后此方法会回调;可在此方法中进行一些判断或者逻辑处理,或者给自定义的dayCell添加一些相关的属性或者设置

---

- `beforeCellDraw`-绘制某个日期之前的回调

此回调首先提供了大量绘制该cell时使用的参数(在绘制过程都是不变的,除非手动去修改设置);如绘制的区域,颜色配置,日期字体大小;之后在其它绘制回调中不再提供这些不变的配置参数,所以如果需要时可以在此处缓存下来.**此处所有参数仅在此次日期绘制时不变,绘制新的日期时参数可能会变动**;

其中参数`drawArea`需要特别说明一下,这里是指该日期所占的绘制区域,**是个矩形,是个矩形,是个矩形!**,并且每个日期的绘制区域是相连在一起的(本质日历就是一个表格类型),当需要把绘制UI成特别形状时必须在此绘制区域内并且需要自行调整.

最后,在此方法中可以进行一些最初的绘制操作.但该部分的所有绘制结果都会被后续绘制重叠的内容覆盖.(即绘制在最底层)

---

- `afterCellDraw`-绘制某个日期之后的回调

此回调与`beforeCellDraw`非常相似,但是仅有一个目的,做绘制后工作的处理或者是自行调整UI绘制,此部分的绘制操作会覆盖原有的所有绘制内容.


[接口及参数详情说明](https://github.com/CrazyTaro/freeCalendar/blob/master/interface_introduction.md)

# 其它

农历日期计算的源码来自网络,如果有任何不当的地方,侵删.
