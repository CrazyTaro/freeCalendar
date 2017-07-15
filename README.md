[TOC]
# calendar
a useful calendar which you can custom its styles or actions easily.

# 概述
日历控件并不是会很常用到,但是在项目中用到的话很多时候是更倾向于UI自定义程度会比较高或者带有自己应用的特点.

github上已经存在很多很不错的日历控件.但是绝大多数的特点是该日历特有的并且UI的修改与控制也会很麻烦.

基于这个原因,所以就写了这个日历控件库希望**本身功能比较完整的情况下同时也能提供比较方便容易的自定义UI**的功能

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

- 滑动效果

![](https://raw.githubusercontent.com/CrazyTaro/freeCalendar/master/screenshot/calendar.gif)

---

- 丰富的可设置属性

![](https://raw.githubusercontent.com/CrazyTaro/freeCalendar/master/screenshot/calendar_xml_attr.gif)

# 版本更新内容
- 0.1.0,首次发布
- 0.2.0,新增周末标题文本可自定义的功能
- 0.3.0,文档调整,新增注释说明
- 0.4.0,打包带源码及注释的库

# 功能特点
- 丰富的xml属性设置可以满足大部分的需要并且能直接在xml中预览出效果
- 大量的自定义绘制方法可直接控制日历的UI显示
- 自定义UI绘制采用接口回调的方式不需要继承任何View直接重写相关的方法即可

# 使用方式
- step1:在项目级别的`build.gradle`文件下声明仓库

```
allprojects {
	repositories {
		...
		maven { url 'https://www.jitpack.io' }
	}
}
```

- step2:引用需要的版本

最新版本: [![](https://www.jitpack.io/v/CrazyTaro/freeCalendar.svg)](https://www.jitpack.io/#CrazyTaro/freeCalendar)
```
dependencies {
        compile 'com.github.CrazyTaro:freeCalendar:latest_release'
}
```

- step3:在xml中使用

```
<com.taro.calendar.lib.CalendarView
        android:id="@+id/cv_calendar"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

- step4:自定义UI控制

继承`BaseCalendarDrawHelper`,对需要修改或者控制的UI进行重写即可.
其中`BaseCalendarDrawHelper`是一个默认实现了所有绘制接口的类,默认的控件`CalendarView`的绘制操作即使用此类的实例.该类是实现了`IDrawCallback`的绘制接口.

直接实现`IDrawCallback`也是一样的,但一次需要实现的接口有点多,推荐还是继承`BaseCalendarDrawHelper`进行并重写相关方法即可;

**重要提示:`BaseCalendarDrawHelper`中基本每个方法都实现了相关的绘制逻辑,不需要默认的UI绘制方式则重写时注意不要调用`super`方法**

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

- 接口方法及参数说明

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

[接口及参数详情说明](https://github.com/CrazyTaro/freeCalendar/blob/master/interface_introduction.md)
