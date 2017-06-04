package com.taro.calendar.lib.utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 农历操作类，可将公历转成农历或者使用与农历相关的方法，以下为部分静态方法
 * </p>
 * <p>
 * <li>{@link #getLunarLeapMonthDays} 获取某年闰月天数
 * <li>{@link #getLunarDateStringInNumberYear} 获取年份以数字形式表示的农历日期字符串
 * <li>{@link #getDayCountBetween2LunarCalendar} 获取两个指定农历日期的天数差
 * </p>
 *
 * @author xuhaolin
 * @since <b>2015年3月12日-下午4:43:39</b>
 */
public class Lunar {
    public static void main(String[] args) {
        int y = (int) ((00 * 0.2422 + 3.87) - ((00 - 1) / 4));
        System.out.println(y);
        Calendar t = Calendar.getInstance();
        t.set(2013, 4, 7);
        Lunar l = new Lunar(t.getTimeInMillis());
        System.out.println("节气:" + l.getTermString());
        System.out.println("干支历:" + l.getCyclicalDateString());
        System.out.println("星期" + l.getDayOfWeek());
        System.out.println("农历" + l.getLunarDateString());

        System.out.print(t.getTime().getMonth() + "月");
        System.out
                .println(getbeforesolarTerm(t.get(Calendar.YEAR), t.getTime()));
        System.out.println(getaftersolarTerm(l.getLunarYear(), t.getTime()));

        System.out.println(l.getLunarYear() + "===" + t.get(Calendar.YEAR));
        Date[] jieqi = jieqilist(t.get(Calendar.YEAR));
        for (int i = 0; i < solarTerm.length; i++) {
            System.out.print(solarTerm[i]);
            System.out.print(jieqi[i].getMonth() + "月");
            System.out.println(jieqi[i].getDate());
        }
    }

    /**
     *
     * @Title: getLiChunDateStr
     * @Description: 获取立春的时间 如2049-1-3
     * @param @param year
     * @param @return
     * @return String
     * @throws
     * @author : zoujiayun
     */
    // public String getLiChunDateStr(int year){
    // double c = 4.6295;//定义c值 默认是20世纪 1900 -1999
    // if(year>=2000 && year<=2099){//21世纪 2000 - 2099
    // c = 3.87;
    // }else if(year>=2100 && year<=2199){//22世纪 2100 - 2199
    // c = 4.15;
    // }
    //
    // return null;
    // }

    /**
     * 获得某天前个节气日期差
     *
     * @return 日期数
     */
    public static long getbeforesolarTerm(int year, Date date) {
        List<Date> jieqi = Alljieqi(year);
        int[] jieqibeforeafter = getnearsolarTerm(year, date);
        return MyDate.DateDays(date, jieqi.get(jieqibeforeafter[0]));

    }

    /**
     * 获得某天后个节气日期差
     *
     * @return 日期数
     */
    public static long getaftersolarTerm(int year, Date date) {
        List<Date> jieqi = Alljieqi(year);
        int[] jieqibeforeafter = getnearsolarTerm(year, date);
        return MyDate.DateDays(date, jieqi.get(jieqibeforeafter[1]));

    }

    public static List<Date> Alljieqi(int year) {
        List<Date> jieqi = new ArrayList<Date>();
        Date[] temp;
        temp = jieqilist(year - 1);
        jieqi.addAll(Arrays.asList(temp));
        temp = jieqilist(year);
        jieqi.addAll(Arrays.asList(temp));
        temp = jieqilist(year + 1);
        jieqi.addAll(Arrays.asList(temp));
        return jieqi;
    }

    /**
     * 获得某天前后两个节气序号
     *
     * @return
     */
    public static int[] getnearsolarTerm(int year, Date date) {
        List<Date> jieqi = Alljieqi(year);

        int[] returnValue = new int[2];
        for (int i = 0; i < jieqi.size(); i++) {
            if (date.getTime() > jieqi.get(i).getTime()) {
                continue;
            }
            if (i % 2 == 0) {
                returnValue[0] = i - 2;
                returnValue[1] = i;
            } else {
                returnValue[0] = i - 1;
                returnValue[1] = i + 1;

            }
            break;
        }

        return returnValue;
    }

    /**
     * 获得某年中所有节气Date
     *
     * @return
     */
    public static Date[] jieqilist(int year) {
        Date[] returnvalue = new Date[solarTerm.length];

        for (int i = 0; i < solarTerm.length; i++) {

            Date t = getSolarTermCalendar(year, i);
            returnvalue[i] = t;

        }
        return returnvalue;
    }

    private final static int[] lunarInfo = {0x4bd8, 0x4ae0, 0xa570, 0x54d5,
            0xd260, 0xd950, 0x5554, 0x56af, 0x9ad0, 0x55d2, 0x4ae0, 0xa5b6,
            0xa4d0, 0xd250, 0xd295, 0xb54f, 0xd6a0, 0xada2, 0x95b0, 0x4977,
            0x497f, 0xa4b0, 0xb4b5, 0x6a50, 0x6d40, 0xab54, 0x2b6f, 0x9570,
            0x52f2, 0x4970, 0x6566, 0xd4a0, 0xea50, 0x6a95, 0x5adf, 0x2b60,
            0x86e3, 0x92ef, 0xc8d7, 0xc95f, 0xd4a0, 0xd8a6, 0xb55f, 0x56a0,
            0xa5b4, 0x25df, 0x92d0, 0xd2b2, 0xa950, 0xb557, 0x6ca0, 0xb550,
            0x5355, 0x4daf, 0xa5b0, 0x4573, 0x52bf, 0xa9a8, 0xe950, 0x6aa0,
            0xaea6, 0xab50, 0x4b60, 0xaae4, 0xa570, 0x5260, 0xf263, 0xd950,
            0x5b57, 0x56a0, 0x96d0, 0x4dd5, 0x4ad0, 0xa4d0, 0xd4d4, 0xd250,
            0xd558, 0xb540, 0xb6a0, 0x95a6, 0x95bf, 0x49b0, 0xa974, 0xa4b0,
            0xb27a, 0x6a50, 0x6d40, 0xaf46, 0xab60, 0x9570, 0x4af5, 0x4970,
            0x64b0, 0x74a3, 0xea50, 0x6b58, 0x5ac0, 0xab60, 0x96d5, 0x92e0,
            0xc960, 0xd954, 0xd4a0, 0xda50, 0x7552, 0x56a0, 0xabb7, 0x25d0,
            0x92d0, 0xcab5, 0xa950, 0xb4a0, 0xbaa4, 0xad50, 0x55d9, 0x4ba0,
            0xa5b0, 0x5176, 0x52bf, 0xa930, 0x7954, 0x6aa0, 0xad50, 0x5b52,
            0x4b60, 0xa6e6, 0xa4e0, 0xd260, 0xea65, 0xd530, 0x5aa0, 0x76a3,
            0x96d0, 0x4afb, 0x4ad0, 0xa4d0, 0xd0b6, 0xd25f, 0xd520, 0xdd45,
            0xb5a0, 0x56d0, 0x55b2, 0x49b0, 0xa577, 0xa4b0, 0xaa50, 0xb255,
            0x6d2f, 0xada0, 0x4b63, 0x937f, 0x49f8, 0x4970, 0x64b0, 0x68a6,
            0xea5f, 0x6b20, 0xa6c4, 0xaaef, 0x92e0, 0xd2e3, 0xc960, 0xd557,
            0xd4a0, 0xda50, 0x5d55, 0x56a0, 0xa6d0, 0x55d4, 0x52d0, 0xa9b8,
            0xa950, 0xb4a0, 0xb6a6, 0xad50, 0x55a0, 0xaba4, 0xa5b0, 0x52b0,
            0xb273, 0x6930, 0x7337, 0x6aa0, 0xad50, 0x4b55, 0x4b6f, 0xa570,
            0x54e4, 0xd260, 0xe968, 0xd520, 0xdaa0, 0x6aa6, 0x56df, 0x4ae0,
            0xa9d4, 0xa4d0, 0xd150, 0xf252, 0xd520};
    private final static int[] solarTermInfo = {0, 21208, 42467, 63836, 85337,
            107014, 128867, 150921, 173149, 195551, 218072, 240693, 263343,
            285989, 308563, 331033, 353350, 375494, 397447, 419210, 440795,
            462224, 483532, 504758};
    public final static String[] Tianan = {"甲", "乙", "丙", "丁", "戊", "己", "庚",
            "辛", "壬", "癸"};
    public final static String[] Deqi = {"子", "丑", "寅", "卯", "辰", "巳", "午",
            "未", "申", "酉", "戌", "亥"};
    public final static String[] Animals = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马",
            "羊", "猴", "鸡", "狗", "猪"};
    public final static String[] solarTerm = {"小寒", "大寒", "立春", "雨水", "惊蛰",
            "春分", "清明", "谷雨", "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋", "处暑",
            "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"};
    public final static String[] lunarString1 = {"零", "一", "二", "三", "四", "五",
            "六", "七", "八", "九"};
    public final static String[] lunarString2 = {"初", "十", "廿", "三", "正", "腊",
            "冬", "闰"};
    /**
     * 国历节日 *表示放假日
     */
    private final static String[] sFtv = {"0101*元旦", "0214 情人节", "0308 妇女节",
            "0312 植树节", "0315 消权日", "0401 愚人节", "0501*劳动节", "0504 青年节",
            "0512 护士节", "0601 儿童节", "0701 建党节", "0801 建军节",
            "0910 教师节", "0928 孔子诞辰", "1001*国庆节", "1024 联合国日", "1111 光棍节",
            "1220 澳门回归", "1224 平安夜", "1225 圣诞节"};
    // private final static String[] sFtv = { "0101*元旦", "0214 情人节", "0308 妇女节",
    // "0312 植树节", "0315 消费者权益日", "0401 愚人节", "0501*劳动节", "0504 青年节",
    // "0509 郝维节", "0512 护士节", "0601 儿童节", "0701 建党节 香港回归纪念", "0801 建军节",
    // "0808 父亲节", "0816 燕衔泥节", "0909 毛泽东逝世纪念", "0910 教师节", "0928 孔子诞辰",
    // "1001*国庆节", "1006 老人节", "1024 联合国日", "1111 光棍节", "1112 孙中山诞辰纪念",
    // "1220 澳门回归纪念", "1225 圣诞节", "1226 毛泽东诞辰纪念" };
    /**
     * 农历节日 *表示放假日
     */
    private final static String[] lFtv = {"0101*春节", "0115 元宵节", "0202 龙抬头",
            "0505 端午节", "0707 七夕节", "0715 中元节", "0815 中秋节", "0909 重阳节",
            "1208 腊八节", "1224 南小年", "1223 北小年", "1230 除夕", "1229 除夕"};
    // private final static String[] lFtv = {
    // "0101*春节、弥勒佛诞", "0106 定光佛诞", "0115 元宵节",
    // "0208 释迦牟尼佛出家", "0215 释迦牟尼佛涅槃", "0209 海空上师诞",
    // "0219 观世音菩萨诞", "0221 普贤菩萨诞", "0316 准提菩萨诞",
    // "0404 文殊菩萨诞", "0408 释迦牟尼佛诞",
    // "0415 佛吉祥日——释迦牟尼佛诞生、成道、涅槃三期同一庆(即南传佛教国家的卫塞节)",
    // "0505 端午节", "0513 伽蓝菩萨诞", "0603 护法韦驮尊天菩萨诞",
    // "0619 观世音菩萨成道——此日放生、念佛，功德殊胜",
    // "0707 七夕情人节", "0713 大势至菩萨诞", "0715 中元节",
    // "0724 龙树菩萨诞", "0730 地藏菩萨诞", "0815 中秋节",
    // "0822 燃灯佛诞", "0909 重阳节", "0919 观世音菩萨出家纪念日",
    // "0930 药师琉璃光如来诞", "1005 达摩祖师诞", "1107 阿弥陀佛诞",
    // "1208 释迦如来成道日，腊八节", "1224 小年",
    // "1229 华严菩萨诞", "0100*除夕"
    // };

    /**
     * 某月的第几个星期几，数字的意义是:前两个是月份，中间一个是该月第几个星期，最后一个数字是该星期的星期几
     * <p>
     * <li>星期日，1
     * <li>星期一，2
     * <li>星期二，3
     * <li>星期三，4
     * <li>星期四，5
     * <li>星期五，6
     * <li>星期六，7
     * </p>
     */
    private static String[] wFtv = {"0521 母亲节", "0631 父亲节", "1145 感恩节"};

    private final static Pattern sFreg = Pattern
            .compile("^(\\d{2})(\\d{2})([\\s\\*])(.+)$");
    private final static Pattern wFreg = Pattern
            .compile("^(\\d{2})(\\d)(\\d)([\\s\\*])(.+)$");

    private static int toInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return -1;
        }
    }

    private synchronized void findFestival() {
        int sM = this.getSolarMonth();
        int sD = this.getSolarDay();
        int lM = this.getLunarMonth();
        int lD = this.getLunarDay();
        int sy = this.getSolarYear();
        boolean isBigEndMonth = false;

        // 若当月为12月份，计算其天数
        if (lM == 12) {
            int monthDayOf12 = Lunar.getLunarMonthDays(this.lunarYear,
                    this.lunarMonth);
            if (monthDayOf12 == 30) {
                isBigEndMonth = true;
            }
        }

        Matcher m;
        // 若当前的月份是闰月，则不查找任何农历节日
        if (!isLeap) {
            for (int i = 0; i < Lunar.lFtv.length; i++) {
                m = Lunar.sFreg.matcher(Lunar.lFtv[i]);
                if (m.find()) {
                    if (lM == Lunar.toInt(m.group(1))
                            && lD == Lunar.toInt(m.group(2))) {
                        // 若当月为12月份且是大月，则跳过29号的除夕
                        if (isBigEndMonth) {
                            if (lD == 29) {
                                continue;
                            }
                        }
                        this.isLFestival = true;
                        this.lFestivalName = m.group(4);
                        if ("*".equals(m.group(3))) {
                            this.isHoliday = true;
                        }
                        break;
                    }
                }
            }

            for (int i = 0; i < Lunar.sFtv.length; i++) {
                m = Lunar.sFreg.matcher(Lunar.sFtv[i]);
                if (m.find()) {
                    if (sM == Lunar.toInt(m.group(1))
                            && sD == Lunar.toInt(m.group(2))) {
                        this.isSFestival = true;
                        this.sFestivalName = m.group(4);
                        if ("*".equals(m.group(3))) {
                            this.isHoliday = true;
                        }
                        break;
                    }
                }
            }
        }

        // 月周节日
        // 节日所在的月份
        int month = 0;
        // 节日所在的第几周
        int week = 0;
        // 星期几
        int weekday = 0;
        // 本月第一天
        Calendar firstDayOfMonthCalendar = Calendar.getInstance();
        // 默认本月第一天的星期为星期日
        int firstDayWeekdayIndex = 1;
        for (int i = 0; i < Lunar.wFtv.length; i++) {
            m = Lunar.wFreg.matcher(Lunar.wFtv[i]);
            if (m.find()) {
                month = Lunar.toInt(m.group(1));
                week = Lunar.toInt(m.group(2));
                weekday = Lunar.toInt(m.group(3));
                // 月份与星期都与节日要求的一致
                if (this.getSolarMonth() == month
                        && this.solar.get(Calendar.DAY_OF_WEEK) == weekday) {
                    firstDayOfMonthCalendar.set(sy, sM - 1, 1);
                    if (firstDayOfMonthCalendar.get(Calendar.DAY_OF_WEEK) > weekday) {
                        week++;
                    }

                    if (this.solar.get(Calendar.WEEK_OF_MONTH) == week) {
                        this.isSFestival = true;
                        this.sFestivalName += "|" + m.group(5);
                        if ("*".equals(m.group(4))) {
                            this.isHoliday = true;
                        }
                    }
                }
            }
        }
        if (sy > 1874 && sy < 1909) {
            this.description = "光绪"
                    + (((sy - 1874) == 1) ? "元" : "" + (sy - 1874));
        }
        if (sy > 1908 && sy < 1912) {
            this.description = "宣统"
                    + (((sy - 1908) == 1) ? "元" : String.valueOf(sy - 1908));
        }
        if (sy > 1911 && sy < 1950) {
            this.description = "民国"
                    + (((sy - 1911) == 1) ? "元" : String.valueOf(sy - 1911));
        }
        if (sy > 1949) {
            this.description = "共和国"
                    + (((sy - 1949) == 1) ? "元" : String.valueOf(sy - 1949));
        }
        this.description += "年";
        this.sFestivalName = this.sFestivalName.replaceFirst("^\\|", "");
        this.isFinded = true;
    }

    /**
     * <p>
     * 获取当前农历日期对应的特殊日子，如节气，农历节日，公历节日等。按以下优先顺序返回，<b>节气>农历节日>公历节日</b>
     * </p>
     *
     * @return String 返回当前农历日期对应的特殊节日等，不存在节日返回空串“”
     * @author xuhaolin
     * @since <b>2015年3月17日-上午11:25:12</b>
     */
    public String getSpecialDateString() {
        String lunarDateStr = this.getTermString();
        if (CommonUtils.isNullOrEmpty(lunarDateStr)) {
            if (this.isFestival()) {
                if (this.isLFestival()) {
                    lunarDateStr = this.getLFestivalName();
                } else if (this.isSFestival()) {
                    lunarDateStr = this.getSFestivalName();
                }
            }
        }
        return lunarDateStr;
    }

    private boolean isFinded = false;
    private boolean isSFestival = false;
    private boolean isLFestival = false;
    private String sFestivalName = "";
    private String lFestivalName = "";
    private String description = "";
    private boolean isHoliday = false;

    /**
     * 返回农历年闰月月份
     *
     * @param lunarYear 指定农历年份(数字)
     * @return 该农历年闰月的月份(数字, 没闰返回0)
     */
    public static int getLunarLeapMonth(int lunarYear) {
        // 数据表中,每个农历年用16bit来表示,
        // 前12bit分别表示12个月份的大小月,最后4bit表示闰月
        // 若4bit全为1或全为0,表示没闰, 否则4bit的值为闰月月份
        int leapMonth = Lunar.lunarInfo[lunarYear - 1900] & 0xf;
        leapMonth = (leapMonth == 0xf ? 0 : leapMonth);
        return leapMonth;
    }

    /**
     * 返回农历年闰月的天数
     *
     * @param lunarYear 指定农历年份(数字)
     * @return 该农历年闰月的天数(数字)
     */
    public static int getLunarLeapMonthDays(int lunarYear) {
        // 下一年最后4bit为1111,返回30(大月)
        // 下一年最后4bit不为1111,返回29(小月)
        // 若该年没有闰月,返回0
        return Lunar.getLunarLeapMonth(lunarYear) > 0 ? ((Lunar.lunarInfo[lunarYear - 1899] & 0xf) == 0xf ? 30
                : 29)
                : 0;
    }

    /**
     * 返回农历年的总天数
     *
     * @param lunarYear 指定农历年份(数字)
     * @return 该农历年的总天数(数字)
     */
    public static int getLunarYearDays(int lunarYear) {
        // 按小月计算,农历年最少有12 * 29 = 348天
        int daysInLunarYear = 348;
        // 数据表中,每个农历年用16bit来表示,
        // 前12bit分别表示12个月份的大小月,最后4bit表示闰月
        // 每个大月累加一天
        for (int i = 0x8000; i > 0x8; i >>= 1) {
            daysInLunarYear += ((Lunar.lunarInfo[lunarYear - 1900] & i) != 0) ? 1
                    : 0;
        }
        // 加上闰月天数
        daysInLunarYear += Lunar.getLunarLeapMonthDays(lunarYear);

        return daysInLunarYear;
    }

    /**
     * 返回农历年正常月份的总天数
     *
     * @param lunarYear  指定农历年份(数字)
     * @param lunarMonth 指定农历月份(数字)
     * @return 该农历年闰月的月份(数字, 没闰返回0)
     */
    public static int getLunarMonthDays(int lunarYear, int lunarMonth) {
        // 数据表中,每个农历年用16bit来表示,
        // 前12bit分别表示12个月份的大小月,最后4bit表示闰月
        int daysInLunarMonth = ((Lunar.lunarInfo[lunarYear - 1900] & (0x10000 >> lunarMonth)) != 0) ? 30
                : 29;
        return daysInLunarMonth;
    }

    /**
     * 取 Date 对象中用全球标准时间 (UTC) 表示的日期
     *
     * @param date 指定日期
     * @return UTC 全球标准时间 (UTC) 表示的日期
     */
    public static synchronized int getUTCDay(Date date) {
        Lunar.makeUTCCalendar();
        synchronized (utcCal) {
            utcCal.clear();
            utcCal.setTimeInMillis(date.getTime());
            return utcCal.get(Calendar.DAY_OF_MONTH);
        }
    }

    private static GregorianCalendar utcCal = null;

    private static synchronized void makeUTCCalendar() {
        if (Lunar.utcCal == null) {
            Lunar.utcCal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        }
    }

    /**
     * 返回全球标准时间 (UTC) (或 GMT) 的 1970 年 1 月 1 日到所指定日期之间所间隔的毫秒数。
     *
     * @param y   指定年份
     * @param m   指定月份
     * @param d   指定日期
     * @param h   指定小时
     * @param min 指定分钟
     * @param sec 指定秒数
     * @return 全球标准时间 (UTC) (或 GMT) 的 1970 年 1 月 1 日到所指定日期之间所间隔的毫秒数
     */
    public static synchronized long UTC(int y, int m, int d, int h, int min,
                                        int sec) {
        Lunar.makeUTCCalendar();
        synchronized (utcCal) {
            utcCal.clear();
            utcCal.set(y, m, d, h, min, sec);
            return utcCal.getTimeInMillis();
        }
    }

    /**
     * 返回公历年节气的日期
     *
     * @param solarYear 指定公历年份(数字)
     * @param index     指定节气序号(数字,0从小寒算起)
     * @return 日期(数字, 所在月份的第几天)
     */
    private static int getSolarTermDay(int solarYear, int index) {

        return Lunar.getUTCDay(getSolarTermCalendar(solarYear, index));
    }

    /**
     * 返回公历年节气的日期
     *
     * @param solarYear 指定公历年份(数字)
     * @param index     指定节气序号(数字,0从小寒算起)
     * @return 日期(数字, 所在月份的第几天)
     */
    public static Date getSolarTermCalendar(int solarYear, int index) {
        long l = (long) 31556925974.7 * (solarYear - 1900)
                + solarTermInfo[index] * 60000L;
        l = l + Lunar.UTC(1900, 0, 6, 2, 5, 0);
        return new Date(l);
    }

    private Calendar solar;
    private int lunarYear;
    private int lunarMonth;
    private int lunarDay;
    private boolean isLeap;
    private boolean isLeapYear;
    private int solarYear;
    private int solarMonth;
    private int solarDay;
    private int cyclicalYear = 0;
    private int cyclicalMonth = 0;
    private int cyclicalDay = 0;
    private int maxDayInMonth = 29;

    /**
     * 通过 Date 对象构建农历信息
     *
     * @param date 指定日期对象
     */
    public Lunar(Date date) {
        if (date == null) {
            date = new Date();
        }
        this.init(date.getTime());
    }

    /**
     * 通过 TimeInMillis 构建农历信息
     *
     * @param TimeInMillis
     */
    public Lunar(long TimeInMillis) {
        this.init(TimeInMillis);
    }

    /**
     * 更新当前的农历日期
     *
     * @param timeInMills
     */
    public void updateDate(long timeInMills) {
        init(timeInMills);
    }

    private void init(long TimeInMillis) {
        this.solar = Calendar.getInstance();
        this.solar.setTimeInMillis(TimeInMillis);
        Calendar baseDate = new GregorianCalendar(1900, 0, 31);
        long offset = (TimeInMillis - baseDate.getTimeInMillis()) / 86400000;
        // 按农历年递减每年的农历天数，确定农历年份
        this.lunarYear = 1900;
        int daysInLunarYear = Lunar.getLunarYearDays(this.lunarYear);
        while (this.lunarYear < 2100 && offset >= daysInLunarYear) {
            offset -= daysInLunarYear;
            daysInLunarYear = Lunar.getLunarYearDays(++this.lunarYear);
        }
        // 农历年数字

        // 按农历月递减每月的农历天数，确定农历月份
        int lunarMonth = 1;
        // 所在农历年闰哪个月,若没有返回0
        int leapMonth = Lunar.getLunarLeapMonth(this.lunarYear);
        // 是否闰年
        this.isLeapYear = leapMonth > 0;
        // 闰月是否递减
        boolean leapDec = false;
        boolean isLeap = false;
        int daysInLunarMonth = 0;
        while (lunarMonth < 13 && offset > 0) {
            if (isLeap && leapDec) { // 如果是闰年,并且是闰月
                // 所在农历年闰月的天数
                daysInLunarMonth = Lunar.getLunarLeapMonthDays(this.lunarYear);
                leapDec = false;
            } else {
                // 所在农历年指定月的天数
                daysInLunarMonth = Lunar.getLunarMonthDays(this.lunarYear,
                        lunarMonth);
            }
            if (offset < daysInLunarMonth) {
                break;
            }
            offset -= daysInLunarMonth;

            if (leapMonth == lunarMonth && isLeap == false) {
                // 下个月是闰月
                leapDec = true;
                isLeap = true;
            } else {
                // 月份递增
                lunarMonth++;
            }
        }
        this.maxDayInMonth = daysInLunarMonth;
        // 农历月数字
        this.lunarMonth = lunarMonth;
        // 是否闰月
        this.isLeap = (lunarMonth == leapMonth && isLeap);
        // 农历日数字
        this.lunarDay = (int) offset + 1;
        // 取得干支历
        this.getCyclicalData();

        //重置节日的查找工作
        this.isFinded = false;
        this.isLFestival = false;
        this.isSFestival = false;
        this.lFestivalName = "";
        this.sFestivalName = "";
        this.description = "";
        this.isHoliday = false;
    }

    /**
     * 取干支历 不是历年，历月干支，而是中国的从立春节气开始的节月，是中国的太阳十二宫，阳历的。
     */
    private void getCyclicalData() {
        this.solarYear = this.solar.get(Calendar.YEAR);
        this.solarMonth = this.solar.get(Calendar.MONTH);
        this.solarDay = this.solar.get(Calendar.DAY_OF_MONTH);
        // 干支历
        int cyclicalYear = 0;
        int cyclicalMonth = 0;
        int cyclicalDay = 0;

        // 干支年 1900年立春後为庚子年(60进制36)
        int term2 = Lunar.getSolarTermDay(solarYear, 2); // 立春日期
        // 依节气调整二月分的年柱, 以立春为界
        if (solarMonth < 1 || (solarMonth == 1 && solarDay < term2)) {
            cyclicalYear = (solarYear - 1900 + 36 - 1) % 60;
        } else {
            cyclicalYear = (solarYear - 1900 + 36) % 60;
        }

        // 干支月 1900年1月小寒以前为 丙子月(60进制12)
        int firstNode = Lunar.getSolarTermDay(solarYear, solarMonth * 2); // 传回当月「节」为几日开始
        // 依节气月柱, 以「节」为界
        if (solarDay < firstNode) {
            cyclicalMonth = ((solarYear - 1900) * 12 + solarMonth + 12) % 60;
        } else {
            cyclicalMonth = ((solarYear - 1900) * 12 + solarMonth + 13) % 60;
        }

        // 当月一日与 1900/1/1 相差天数
        // 1900/1/1与 1970/1/1 相差25567日, 1900/1/1 日柱为甲戌日(60进制10)
        cyclicalDay = (int) (Lunar
                .UTC(solarYear, solarMonth, solarDay, 0, 0, 0) / 86400000 + 25567 + 10) % 60;
        this.cyclicalYear = cyclicalYear;
        this.cyclicalMonth = cyclicalMonth;
        this.cyclicalDay = cyclicalDay;
    }

    /**
     * 取农历年生肖
     *
     * @return 农历年生肖(例:龙)
     */
    public static String getAnimalString(int year) {
        return Lunar.Animals[(year - 4) % 12];
    }

    /**
     * 返回公历日期的节气字符串
     *
     * @return 二十四节气字符串, 若不是节气日, 返回空串(例:冬至)
     */
    public String getTermString() {
        // 二十四节气
        String termString = "";
        if (Lunar.getSolarTermDay(solarYear, solarMonth * 2) == solarDay) {
            termString = Lunar.solarTerm[solarMonth * 2];
        } else if (Lunar.getSolarTermDay(solarYear, solarMonth * 2 + 1) == solarDay) {
            termString = Lunar.solarTerm[solarMonth * 2 + 1];
        }
        return termString;
    }

    /**
     * 取得干支历字符串
     *
     * @return 干支历字符串(例:甲子年甲子月甲子日)
     */
    public String getCyclicalDateString() {
        return this.getCyclicaYear() + "年" + this.getCyclicaMonth() + "月"
                + this.getCyclicaDay() + "日";
    }

    /**
     * 年份天干
     *
     * @return 年份天干
     */
    public int getTiananY() {
        return Lunar.getTianan(this.cyclicalYear);
    }

    /**
     * 月份天干
     *
     * @return 月份天干
     */
    public int getTiananM() {
        return Lunar.getTianan(this.cyclicalMonth);
    }

    /**
     * 日期天干
     *
     * @return 日期天干
     */
    public int getTiananD() {
        return Lunar.getTianan(this.cyclicalDay);
    }

    /**
     * 年份地支
     *
     * @return 年分地支
     */
    public int getDeqiY() {
        return Lunar.getDeqi(this.cyclicalYear);
    }

    /**
     * 月份地支
     *
     * @return 月份地支
     */
    public int getDeqiM() {
        return Lunar.getDeqi(this.cyclicalMonth);
    }

    /**
     * 日期地支
     *
     * @return 日期地支
     */
    public int getDeqiD() {
        return Lunar.getDeqi(this.cyclicalDay);
    }

    /**
     * 取得干支年字符串
     *
     * @return 干支年字符串
     */
    public String getCyclicaYear() {
        return Lunar.getCyclicalString(this.cyclicalYear);
    }

    /**
     * 取得干支月字符串
     *
     * @return 干支月字符串
     */
    public String getCyclicaMonth() {
        return Lunar.getCyclicalString(this.cyclicalMonth);
    }

    /**
     * 取得干支日字符串
     *
     * @return 干支日字符串
     */
    public String getCyclicaDay() {
        return Lunar.getCyclicalString(this.cyclicalDay);
    }

    /**
     * 返回农历日期字符串
     *
     * @return 农历日期字符串
     */
    public String getLunarDayString() {
        return Lunar.getLunarDayString(this.lunarDay);
    }

    /**
     * 返回农历日期字符串
     *
     * @return 农历日期字符串
     */
    public String getLunarMonthString() {
        return (this.isLeap() ? "闰" : "")
                + Lunar.getLunarMonthString(this.lunarMonth);
    }

    /**
     * 返回农历日期字符串
     *
     * @return 农历日期字符串
     */
    public String getLunarYearString() {
        return Lunar.getLunarYearString(this.lunarYear);
    }

    /**
     * 返回农历表示字符串
     *
     * @return 农历字符串(例:甲子年正月初三)
     */
    public String getLunarDateString() {
        return this.getLunarYearString() + "年" + this.getLunarMonthString()
                + "月" + this.getLunarDayString() + "日";
    }

    /**
     * 农历年是否是闰月
     *
     * @return 农历年是否是闰月
     */
    public boolean isLeap() {
        return isLeap;
    }

    /**
     * 农历年是否是闰年
     *
     * @return 农历年是否是闰年
     */
    public boolean isLeapYear() {
        return isLeapYear;
    }

    /**
     * <p>
     * 获取农历年份以数字表示的字符串，如2015年正月初一
     * </p>
     *
     * @return String 返回按给定参数构造的字符串
     * @author xuhaolin
     * @since <b>2015年3月10日-下午3:38:44</b>
     */
    public String getLunarDateStringInNumberYear() {
        return this.lunarYear + "年" + this.getLunarMonthString() + "月"
                + this.getLunarDateString();
    }

    /**
     * <p>
     * 获取农历年份以数字表示的字符串，如2015年正月初一
     * </p>
     *
     * @param lunarYear  int 农历年份
     * @param lunarMonth int 农历月份
     * @param lunarDay   int 农历日期
     * @return String 返回按给定参数构造的字符串
     * @author xuhaolin
     * @since <b>2015年3月10日-下午3:32:31</b>
     */
    public static String getLunarDateStringInNumberYear(int lunarYear,
                                                        int lunarMonth, int lunarDay) {
        return lunarYear + "年" + Lunar.getLunarMonthString(lunarMonth) + "月"
                + Lunar.getLunarDayString(lunarDay);
    }

    /**
     * 当前农历月是否是大月
     *
     * @return 当前农历月是大月
     */
    public boolean isBigMonth() {
        return this.getMaxDayInMonth() > 29;
    }

    /**
     * 当前农历月有多少天
     *
     * @return 当前农历月有多少天
     */
    public int getMaxDayInMonth() {
        return this.maxDayInMonth;
    }

    /**
     * 农历日期
     *
     * @return 农历日期
     */
    public int getLunarDay() {
        return lunarDay;
    }

    /**
     * 农历月份
     *
     * @return 农历月份
     */
    public int getLunarMonth() {
        return lunarMonth;
    }

    /**
     * 农历年份
     *
     * @return 农历年份
     */
    public int getLunarYear() {
        return lunarYear;
    }

    /**
     * 公历日期
     *
     * @return 公历日期
     */
    public int getSolarDay() {
        return solarDay;
    }

    /**
     * 公历月份
     *
     * @return 公历月份 (不是从0算起)
     */
    public int getSolarMonth() {
        return solarMonth + 1;
    }

    /**
     * 公历年份
     *
     * @return 公历年份
     */
    public int getSolarYear() {
        return solarYear;
    }

    /**
     * 星期几
     *
     * @return 星期几(星期日为:1, 星期六为:7)
     */
    public int getDayOfWeek() {
        return this.solar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 黑色星期五
     *
     * @return 是否黑色星期五
     */
    public boolean isBlackFriday() {
        return (this.getSolarDay() == 13 && this.solar
                .get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY);
    }

    /**
     * 是否是今日
     *
     * @return 是否是今日
     */
    public boolean isToday() {
        Calendar clr = Calendar.getInstance();
        return clr.get(Calendar.YEAR) == this.solarYear
                && clr.get(Calendar.MONTH) == this.solarMonth
                && clr.get(Calendar.DAY_OF_MONTH) == this.solarDay;
    }

    /**
     * 取得公历节日名称
     *
     * @return 公历节日名称, 如果不是节日返回空串
     */
    public String getSFestivalName() {
        return this.sFestivalName;
    }

    /**
     * 取得农历节日名称
     *
     * @return 农历节日名称, 如果不是节日返回空串
     */
    public String getLFestivalName() {
        return this.lFestivalName;
    }

    /**
     * 是否是农历节日
     *
     * @return 是否是农历节日
     */
    public boolean isLFestival() {
        if (!this.isFinded) {
            this.findFestival();
        }
        return this.isLFestival;
    }

    /**
     * 是否是公历节日
     *
     * @return 是否是公历节日
     */
    public boolean isSFestival() {
        if (!this.isFinded) {
            this.findFestival();
        }
        return this.isSFestival;
    }

    /**
     * 是否是节日
     *
     * @return 是否是节日
     */
    public boolean isFestival() {
        if (!this.isFinded) {
            this.findFestival();
        }
        return this.isLFestival | this.isSFestival;
    }

    /**
     * 是否是放假日
     *
     * @return 是否是放假日
     */
    public boolean isHoliday() {
        if (!this.isFinded) {
            this.findFestival();
        }
        return this.isHoliday;
    }

    /**
     * 其它日期说明
     *
     * @return 日期说明(如:民国2年)
     */
    public String getDescription() {
        if (!this.isFinded) {
            this.findFestival();
        }
        return this.description;
    }

    /**
     * 干支字符串
     *
     * @param cyclicalNumber 指定干支位置(数字,0为甲子)
     * @return 干支字符串
     */
    private static String getCyclicalString(int cyclicalNumber) {
        return Lunar.Tianan[Lunar.getTianan(cyclicalNumber)]
                + Lunar.Deqi[Lunar.getDeqi(cyclicalNumber)];
    }

    /**
     * 获得地支
     *
     * @param cyclicalNumber
     * @return 地支 (数字)
     */
    private static int getDeqi(int cyclicalNumber) {
        return cyclicalNumber % 12;
    }

    /**
     * 获得天干
     *
     * @param cyclicalNumber
     * @return 天干 (数字)
     */
    private static int getTianan(int cyclicalNumber) {
        return cyclicalNumber % 10;
    }

    /**
     * 返回指定数字的农历年份表示字符串
     *
     * @param lunarYear 农历年份(数字,0为甲子)
     * @return 农历年份字符串
     */
    private static String getLunarYearString(int lunarYear) {
        return Lunar.getCyclicalString(lunarYear - 1900 + 36);
    }

    /**
     * 返回指定数字的农历月份表示字符串
     *
     * @param lunarMonth 农历月份(数字)
     * @return 农历月份字符串 (例:正)
     */
    public static String getLunarMonthString(int lunarMonth) {
        String lunarMonthString = null;
        switch (lunarMonth) {
            case 1:
                lunarMonthString = Lunar.lunarString2[4];
                break;
            case 11:
                lunarMonthString = Lunar.lunarString2[6];
                break;
            case 12:
                lunarMonthString = Lunar.lunarString2[5];
                break;
            case 10:
                lunarMonthString = Lunar.lunarString2[1];
                break;
            default:
                lunarMonthString = Lunar.lunarString1[lunarMonth];
                break;
        }
        return lunarMonthString;
    }

    /**
     * 返回指定数字的农历日表示字符串
     *
     * @param lunarDay 农历日(数字)
     * @return 农历日字符串 (例: 廿一)
     */
    public static String getLunarDayString(int lunarDay) {
        if (lunarDay < 1 || lunarDay > 30) {
            return "";
        }
        int i1 = lunarDay / 10;
        int i2 = lunarDay % 10;
        String c1 = Lunar.lunarString2[i1];
        String c2 = Lunar.lunarString1[i2];
        if (lunarDay < 11) {
            c1 = Lunar.lunarString2[0];
        }
        if (i2 == 0) {
            c2 = Lunar.lunarString2[1];
        }
        return c1 + c2;
    }

    /**
     * @param @param  year
     * @param @return
     * @return String
     * @throws
     * @Title: getYearStr
     * @Description: 返回中文的年
     * @author : zoujiayun
     */
    public static String getChineseYearStr(int year) {
        String[] chineseword = {"零", "一", "二", "三", "四", "五", "六", "七", "八",
                "九"};
        String ys = "";
        int index = year / 1000;
        ys += chineseword[index];
        year = year % 1000;
        index = year / 100;
        ys += chineseword[index];
        year = year % 100;
        index = year / 10;
        ys += chineseword[index];
        year = year % 10;
        index = year;
        ys += chineseword[index];
        return ys;
    }

    /**
     * <b>@Description:<b>得到中文的年月日<br/>
     * <b>@param year <b>@param month 2月用2表示 <b>@param day <b>@return<b>String<br/>
     * <b>@Author:<b>zoujiayun<br/>
     * <b>@Since:<b>2014-9-18-下午4:57:42<br/>
     */
    public static String[] getChineseYMDStrArr(int year, int month, int day) {
        return new String[]{getChineseYearStr(year),
                lunarMonthArr[month - 1], getLunarDayArr(30)[day - 1]};
    }

    /**
     * <b>@Description:<b>得到中文年月日信息<br/>
     * <b>@param ydm <b>@param count 表示是否有年的信息 3年月日 有 2月日 <b>@return<b>String<br/>
     * <b>@Author:<b>zoujiayun<br/>
     * <b>@Since:<b>2014-9-20-下午3:12:50<br/>
     */
    public static String getChineseYMDStr(int[] ydm, int count) {
        if (null != ydm && ydm.length > 0) {
            String[] ymdStrArr = getChineseYMDStrArr(ydm[0], ydm[1], ydm[2]);
            String monthDayStr = ymdStrArr[1] + "月" + ymdStrArr[2];
            if (2 == count) {
                return monthDayStr;
            } else if (3 == count) {
                return ymdStrArr[0] + "年" + monthDayStr;
            }
        }
        return null;
    }

    /**
     * <b>@Description:<b>得到汉字月的数组<br/>
     * <b>@return<b>String[]<br/>
     * <b>@Author:<b>zoujiayun<br/>
     * <b>@Since:<b>2014-7-16-下午8:31:50<br/>
     */
    private static String[] lunarMonthArr = {"正", "二", "三", "四", "五", "六",
            "七", "八", "九", "十", "冬", "腊"};
    private static String[] LUNAR_SMALL_MONTH_DAY_ARR = {"初一", "初二", "初三",
            "初四", "初五", "初六", "初七", "初八", "初九", "初十", "十一", "十二", "十三", "十四",
            "十五", "十六", "十七", "十八", "十九", "廿十", "廿一", "廿二", "廿三", "廿四", "廿五",
            "廿六", "廿七", "廿八", "廿九"};
    private static String[] LUNAR_BIG_MONTH_DAY_ARR = {"初一", "初二", "初三", "初四",
            "初五", "初六", "初七", "初八", "初九", "初十", "十一", "十二", "十三", "十四", "十五",
            "十六", "十七", "十八", "十九", "廿十", "廿一", "廿二", "廿三", "廿四", "廿五", "廿六",
            "廿七", "廿八", "廿九", "三十"};

    public static String[] getLunarMonthArr() {
        return lunarMonthArr;
    }

    /**
     * <b>@Description:<b>数字的天转换成汉字<br/>
     * <b>@param countDay : 29或者30 <b>@return<b>String[]<br/>
     * <b>@Author:<b>zoujiayun<br/>
     * <b>@Since:<b>2014-7-17-上午9:26:37<br/>
     */
    public static String[] getLunarDayArr(int countDay) {
        if (countDay < 1 || countDay > 30) {
            return null;
        } else if (countDay == 30) {
            return LUNAR_BIG_MONTH_DAY_ARR;
        } else {
            return LUNAR_SMALL_MONTH_DAY_ARR;
        }
    }

    /**
     * <p>
     * 计算两个农历日期之间的差，<b>日期1-日期2，firstDate-secondDate</b>，当农历日期之间存在闰月时可以区分计算，<b>
     * 但无法区分当前传入的日期是否刚好是闰月</b>
     * </p>
     *
     * @param firstYear   int 日期1年份
     * @param firstMonth  int 日期1月份
     * @param firstDay    int 日期1天
     * @param secondYear  int 日期2年份
     * @param secondMonth int 日期2月份
     * @param secondDay   int 日期2天
     * @return int 返回两个日期之间的天数差
     * @author xuhaolin
     * @since <b>2015年3月12日-上午10:22:10</b>
     */
    public static int getDayCountBetween2LunarCalendar(int firstYear,
                                                       int firstMonth, int firstDay, int secondYear, int secondMonth,
                                                       int secondDay) {
        int dayCount = 0;

        int lunarYear = secondYear;
        int lunarMonth = secondMonth;
        int lunarDay = secondDay;

        if (lunarYear == firstYear) {
            dayCount = getDayCountBetween2LunarMonthDay(firstYear, firstMonth,
                    firstDay, lunarMonth, lunarDay);
        } else {
            // 计算两个农历年之间的天数
            dayCount += getDayCountBetween2LunarYear(firstYear, lunarYear);
            int yearCount = firstYear - lunarYear;
            if (yearCount < 0) {
                // 添加农历年1指定日期到年末的天数（负）
                dayCount += getDayCountBetween2LunarMonthDay(firstYear,
                        firstMonth, firstDay, 12,
                        Lunar.getLunarMonthDays(firstYear, 12));
                // 添加农历年2年头到指定日期的天数（负）
                dayCount += getDayCountBetween2LunarMonthDay(lunarYear, 1, 1,
                        lunarMonth, lunarDay) - 1;
            } else if (yearCount > 0) {
                // 添加农历年1指定日期到年初的天数（正）
                dayCount += getDayCountBetween2LunarMonthDay(firstYear,
                        firstMonth, firstDay, 1, 1);
                // 添加农历年2指定日期到年末的天数（正）
                dayCount += getDayCountBetween2LunarMonthDay(lunarYear, 12,
                        Lunar.getLunarMonthDays(lunarYear, 12), lunarMonth,
                        lunarDay);
            } else {
                dayCount += getDayCountBetween2LunarMonthDay(firstYear,
                        firstMonth, firstDay, lunarMonth, lunarDay);
            }
        }
        return dayCount;
    }

    /**
     * <p>
     * 获取两个农历日期之间的天数差，当农历日期之间存在闰月时可以区分计算，<b>但无法区分当前传入的日期是否刚好是闰月</b>
     * ，<b>日期1-日期2，firstDate-secondDate</b>
     * </p>
     *
     * @param lunarInfo 需要与当前日期比较的农历日期
     * @return int 当前日期减给定日期的差，当前日期小于给定日期将会返回负数
     * @author xuhaolin
     * @since <b>2015年1月21日-下午1:31:26</b>
     */
    public int getDayCountCompare2LunarCalendar(Lunar lunarInfo) {
        int dayCount = 0;

        if (lunarInfo != null) {
            int lunarYear = lunarInfo.getLunarYear();
            int lunarMonth = lunarInfo.getLunarMonth();
            int lunarDay = lunarInfo.getLunarDay();

            dayCount = getDayCountBetween2LunarCalendar(this.lunarYear, this.lunarMonth, this.lunarDay,
                    lunarYear, lunarMonth, lunarDay);
        }
        return dayCount;
//            if (lunarYear == this.lunarYear) {
//                dayCount = getDayCountBetween2LunarMonthDay(this.lunarYear,
//                        this.lunarMonth, this.lunarDay, lunarMonth, lunarDay);
//            } else {
//                // 计算两个农历年之间的天数
//                dayCount += getDayCountBetween2LunarYear(this.lunarYear,
//                        lunarYear);
//                int yearCount = this.lunarYear - lunarYear;
//                if (yearCount < 0) {
//                    // 添加农历年1指定日期到年末的天数（负）
//                    dayCount += getDayCountBetween2LunarMonthDay(
//                            this.lunarYear, this.lunarMonth, this.lunarDay, 12,
//                            Lunar.getLunarMonthDays(this.lunarYear, 12));
//                    // 添加农历年2年头到指定日期的天数（负）
//                    dayCount += getDayCountBetween2LunarMonthDay(lunarYear, 1,
//                            1, lunarMonth, lunarDay) - 1;
//                } else if (yearCount > 0) {
//                    // 添加农历年1指定日期到年初的天数（正）
//                    dayCount += getDayCountBetween2LunarMonthDay(
//                            this.lunarYear, this.lunarMonth, this.lunarDay, 1,
//                            1);
//                    // 添加农历年2指定日期到年末的天数（正）
//                    dayCount += getDayCountBetween2LunarMonthDay(lunarYear, 12,
//                            Lunar.getLunarMonthDays(lunarYear, 12), lunarMonth,
//                            lunarDay);
//                } else {
//                    dayCount += getDayCountBetween2LunarMonthDay(
//                            this.lunarYear, this.lunarMonth, this.lunarDay,
//                            lunarMonth, lunarDay);
//                }
//            }
//        }
//        return dayCount;
    }

    /**
     * <p>
     * 获取两个农历日期之间的天数差，当农历日期之间存在闰月时可以区分计算，<b>但无法区分当前传入的日期是否刚好是闰月</b>，<b>日期1-日期2，
     * firstDate-secondDate</b>
     * </p>
     *
     * @param lunarYear  int 需要与当前日期比较的农历年份
     * @param lunarMonth int 需要与当前日期比较的农历月份
     * @param lunarDay   int 需要与当前日期比较的农历日期
     * @return int 当前日期减给定日期的差，当前日期小于给定日期将会返回负数
     * @author xuhaolin
     * @since <b>2015年1月21日-下午1:48:55</b>
     */
    public int getDayCountCompare2LunarCalendar(int lunarYear, int lunarMonth,
                                                int lunarDay) {
        int dayCount = 0;

        dayCount = getDayCountBetween2LunarCalendar(this.lunarYear, this.lunarMonth, this.lunarDay,
                lunarYear, lunarMonth, lunarDay);

//        if (lunarYear == this.lunarYear) {
//            dayCount = getDayCountBetween2LunarMonthDay(this.lunarYear,
//                    this.lunarMonth, this.lunarDay, lunarMonth, lunarDay);
//        } else {
//            // 计算两个农历年之间的天数
//            dayCount += getDayCountBetween2LunarYear(this.lunarYear, lunarYear);
//            int yearCount = this.lunarYear - lunarYear;
//            if (yearCount < 0) {
//                // 添加农历年1指定日期到年末的天数（负）
//                dayCount += getDayCountBetween2LunarMonthDay(this.lunarYear,
//                        this.lunarMonth, this.lunarDay, 12,
//                        Lunar.getLunarMonthDays(this.lunarYear, 12));
//                // 添加农历年2年头到指定日期的天数（负）
//                dayCount += getDayCountBetween2LunarMonthDay(lunarYear, 1, 1,
//                        lunarMonth, lunarDay) - 1;
//            } else if (yearCount > 0) {
//                // 添加农历年1指定日期到年初的天数（正）
//                dayCount += getDayCountBetween2LunarMonthDay(this.lunarYear,
//                        this.lunarMonth, this.lunarDay, 1, 1);
//                // 添加农历年2指定日期到年末的天数（正）
//                dayCount += getDayCountBetween2LunarMonthDay(lunarYear, 12,
//                        Lunar.getLunarMonthDays(lunarYear, 12), lunarMonth,
//                        lunarDay);
//            } else {
//                dayCount += getDayCountBetween2LunarMonthDay(this.lunarYear,
//                        this.lunarMonth, this.lunarDay, lunarMonth, lunarDay);
//            }
//        }

        return dayCount;
    }

    /**
     * <p>
     * 获取两个农历年之间间隔的年份总天数，不包括当前指定两个年份的天数，<b>日期1-日期2，firstDate-secondDate</b>
     * </p>
     *
     * @param yearFirst  int 第一个农历年
     * @param yearSecond int 第二个农历年
     * @return int 返回第一个农历年与第二个农历年之间的天数，第一个农历年小于第二个农历年则返回负数
     * @author xuhaolin
     * @since <b>2015年1月21日-下午1:33:52</b>
     */
    public static int getDayCountBetween2LunarYear(int yearFirst, int yearSecond) {
        int dayCount = 0;
        int yearCount = yearFirst - yearSecond;
        if (yearCount < 0) {
            yearCount++;
            for (; yearCount < 0; yearCount++) {
                dayCount += Lunar.getLunarYearDays(yearCount + yearSecond);
            }
            dayCount *= -1;
        } else if (yearCount > 0) {
            yearCount--;
            for (; yearCount > 0; yearCount--) {
                dayCount += Lunar.getLunarYearDays(yearCount + yearSecond);
            }
        }

        return dayCount;
    }

    /**
     * <p>
     * 获取指定年份(<b>年份要相同</b>)中两个月份日期之间的天数差，月份之间包括闰月也可以计算出来，<b>
     * 但无法区分当前的给定的两个月份本身是否闰月并计算
     * </b>，只当是唯一的一个月份进行计算，<b>日期1-日期2，firstDate-secondDate</b>
     * </p>
     *
     * @param year        int 指定年份
     * @param monthFirst  int 第一个月份
     * @param dayFirst    int 第一个月份的日期
     * @param monthSecond int 第二个月份
     * @param daySecond   int 第二个月份的日期
     * @return int 返回两个月份日期之间的天数差
     * @author xuhaolin
     * @since <b>2015年1月21日-下午1:35:36 </b>
     */
    public static int getDayCountBetween2LunarMonthDay(int year,
                                                       int monthFirst, int dayFirst, int monthSecond, int daySecond) {
        int dayCount = 0;
        // 两个月份相同
        if (monthFirst == monthSecond) {
            dayCount = (dayFirst > daySecond) ? (dayFirst - daySecond)
                    : (daySecond - dayFirst) * -1;
        } else {
            int monthCount = monthFirst - monthSecond;
            // 农历闰月
            int leapMonth = Lunar.getLunarLeapMonth(year);
            // 月份1小于月份2
            if (monthCount < 0) {
                monthCount++;
                // 计算月份1到月末的天数
                dayCount += Lunar.getLunarMonthDays(year, monthFirst)
                        - dayFirst;
                // 计算月份1的下个月到月份2中间几个月的总天数
                for (; monthCount < 0; monthCount++) {
                    int currentMonth = monthCount + monthSecond;
                    // 包含闰月再添加一个月的天数
                    dayCount += (leapMonth == currentMonth) ? Lunar
                            .getLunarMonthDays(year, currentMonth) : 0;
                    dayCount += Lunar.getLunarMonthDays(year, monthCount
                            + monthSecond);
                }
                // 计算月份2月初到指定日期天数
                dayCount += daySecond;
                // 以上三段总和即为两月间的总天数
                // 月份1小于月份2，返回负天数
                dayCount *= -1;
            } else {
                // 月份2小于月份1
                monthCount--;
                // 计算月份2到月末的天数
                dayCount += Lunar.getLunarMonthDays(year, monthSecond)
                        - daySecond;
                for (; monthCount > 0; monthCount--) {
                    // 计算月份2的下个月到月份1中间几个月的总天数
                    int currentMonth = monthCount + monthSecond;
                    // 包含闰月再添加一个月的天数
                    dayCount += (leapMonth == currentMonth) ? Lunar
                            .getLunarMonthDays(year, currentMonth) : 0;
                    dayCount += Lunar.getLunarMonthDays(year, monthCount
                            + monthSecond);
                }
                // 计算月份1月初到指定日期天数
                dayCount += dayFirst;
            }
        }
        return dayCount;
    }

    /**
     * <p>
     * 将农历转成公历，此方法占用的资源和时间可能会很多，不推荐短时间内重复大量调用
     * </p>
     * <p>
     * <b>记录下原农历的年月日，作为搜索公历的起始点，由于相同的日子，公历必然在农历之前</b>，所以以原农历的年月日作为公历起点，
     * 向后搜索，直到农历日期与原农历日期相同即为所求公历，
     * 如农历2015.1.1对应公历日期为2015.2.18，将2015.1.1作为公历向后搜索直到农历日期为2015
     * .1.1，所得的当天公历即为2015.2.18
     * </p>
     *
     * @param lunarYear  int 农历年份
     * @param lunarMonth int 农历月份
     * @param lunarDay   int 农历日期
     * @param maxUseTime long 最大的查询时间，<b>单位为ms</b>，超过此指定的查询时间则不管是否已查找到都会退出，用于防止死循环，<b>
     *                   默认为10s</b>；<b> 若不指定时间则将其设为负数，则使用默认时间10s</b>
     * @return Calendar 返回农历转换成的公历，查找不到对应的公历返回null
     * @author xuhaolin
     * @since <b>2015年3月11日-下午7:02:02</b>
     */
    public static Calendar getSolarCalendarFromLunar(int lunarYear,
                                                     int lunarMonth, int lunarDay, long maxUseTime) {
        // 记录下原农历的年月日，作为搜索公历的起始点
        // 由于相同的日子，公历必然在农历之前，所以以原农历的年月日作为公历起点
        // 向后搜索，直到农历日期与原农历日期相同即为所求公历
        // 如农历2015.1.1对应公历日期为2015.2.18
        // 将2015.1.1作为公历向后搜索直到农历日期为2015.1.1，所得的当天公历即为2015.2.18
        Calendar tempCalendar = Calendar.getInstance();
        int tempYear = 0;
        int tempMonth = 0;
        int tempDay = 0;
        tempCalendar.set(lunarYear, lunarMonth - 1, lunarDay);
        // 设置公历日期
        // 由于农历必然在公历后面，公历与农历相差一般是一个月
        // 这里将公历向前推进10天，10最少会在10天后才是要求的农历
        // （此处没有必然的算法，最少可能是20天，以10天算比较稳妥）
        tempCalendar.add(Calendar.DATE, 10);
        long startFindTime = System.currentTimeMillis();
        long endFindTime = 0;
        maxUseTime = maxUseTime < 0 ? 100000 : maxUseTime;
        for (; ; ) {
            endFindTime = System.currentTimeMillis();
            if (endFindTime - startFindTime > maxUseTime) {
                // 增加一个查询时间最高限判断
                // 若超过这个时间则退出
                return null;
            }
            // 此处为死循环，原因是无法确定需要循环多少次才能将查找到与农历对应的公历日期
            // 此处退出循环的只能是查找到之后退出
            // 由于某些原因无法查找到对应的日期将会死在此处
            Lunar tempLunar = new Lunar(tempCalendar.getTimeInMillis());
            tempYear = tempLunar.getLunarYear();
            tempMonth = tempLunar.getLunarMonth();
            tempDay = tempLunar.getLunarDay();

            if (tempMonth == lunarMonth && tempDay == lunarDay
                    && tempYear == lunarYear) {
                return tempCalendar;
            }
            tempCalendar.add(Calendar.DATE, 1);
        }
    }

}
