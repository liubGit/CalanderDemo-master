package com.zhanyun.calanderdemo.calendar;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by janecer on 2017/9/7 0007
 * des:
 */

public class CalendarDay {
    public Calendar calendar;
    public int day;
    public int month;
    public int year;

    public CalendarDay() {
        this.setTime(System.currentTimeMillis());
    }

    public CalendarDay(int year, int month, int day) {
        this.setDay(year, month - 1, day);
    }

    public CalendarDay(long timeInMillis) {
        this.setTime(timeInMillis);
    }

    public CalendarDay(Calendar calendar) {
        if(this.calendar == null) {
            this.calendar = calendar;
        }

        this.year = calendar.get(1);
        this.month = calendar.get(2);
        this.day = calendar.get(5);
    }

    private void setTime(long timeInMillis) {
        if(this.calendar == null) {
            this.calendar = Calendar.getInstance();
        }

        this.calendar.setTimeInMillis(timeInMillis);
        this.month = this.calendar.get(2);
        this.year = this.calendar.get(1);
        this.day = this.calendar.get(5);
    }

    public long getTime() {
        if(this.calendar == null) {
            this.calendar = Calendar.getInstance();
        }

        this.calendar.set(this.year, this.month, this.day);
        return this.calendar.getTimeInMillis();
    }

    public void set(CalendarDay calendarDay) {
        this.year = calendarDay.year;
        this.month = calendarDay.month;
        this.day = calendarDay.day;
    }

    public void setDay(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        if(this.calendar == null) {
            this.calendar = Calendar.getInstance();
        }

        this.calendar.set(year, month, day);
    }

    public int getDefaultMonth() {
        return this.month;
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month + 1;
    }

    public void setStringDay(String ymd) {
        if(!TextUtils.isEmpty(ymd)) {
            String[] strings = ymd.split("-");
            this.year = Integer.valueOf(strings[0]).intValue();
            this.month = Integer.valueOf(strings[1]).intValue() - 1;
            this.day = Integer.valueOf(strings[2]).intValue();
            if(this.calendar == null) {
                this.calendar = Calendar.getInstance();
            }

            this.calendar.set(this.year, this.month, this.day);
        }
    }

    public String getDayString() {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return df1.format(new Date(this.getTime()));
    }

    public boolean equals(Object obj) {
        return obj instanceof CalendarDay?this.getDayString().equals(((CalendarDay)obj).getDayString()):super.equals(obj);
    }
}
