package com.zhanyun.calanderdemo.calendar;

import com.zhanyun.calanderdemo.calendar.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by janecer on 2017/9/7 0007
 * des:
 */

public class DayUtils {
    public static final int WEEKS_IN_YEAR = 12;
    public static final int DAY_IN_WEEK = 7;

    public DayUtils() {
    }

    public static int calculateWeekCount(CalendarDay startDay, CalendarDay endDay) {
        long x = endDay.getTime() - startDay.getTime();
        int days = (int)x / 86400000 + 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDay.getTime());
        int startDayOfWeek = calendar.get(7);
        calendar.setTimeInMillis(endDay.getTime());
        int endDayOfWeek = calendar.get(7);
        int week = days / 7 + 1;
        if(endDayOfWeek < startDayOfWeek) {
            ++week;
        }

        return week;
    }

    public static int calculateMonthCount(CalendarDay startDay, CalendarDay endDay) {
        byte monthCount = 0;
        return startDay.year == endDay.year?endDay.month - startDay.month + 1:(startDay.year < endDay.year?(endDay.year - startDay.year - 1) * 12 + (12 - startDay.month) + endDay.month + 1:monthCount);
    }

    public static int calculateMonthPosition(CalendarDay startDay, CalendarDay positionDay) {
        return startDay.year == positionDay.year?positionDay.month - startDay.month:(startDay.year < positionDay.year?(positionDay.year - startDay.year - 1) * 12 + (12 - startDay.month) + positionDay.month:0);
    }

    public static CalendarDay calculateFirstShowDay(CalendarDay startDay) {
        int day = startDay.calendar.get(7);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDay.getTime());
        calendar.roll(6, -day + 1);
        return new CalendarDay(calendar);
    }

    public static int calculateDayPosition(CalendarDay startDay, CalendarDay day) {
        long x = day.getTime() - startDay.getTime();
        return (int)x / 86400000;
    }

    public static CalendarDay calculatePositionDay(CalendarDay startDay, int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDay.getTime());
        calendar.add(2, position);
        return new CalendarDay(calendar.getTimeInMillis());
    }

    public static String formatEnglishTime(long times) {
        SimpleDateFormat df1 = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        return df1.format(new Date(times));
    }

    public static int getDaysInMonth(int month, int year) {
        switch(month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 1:
                return year % 4 == 0?29:28;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }
}
