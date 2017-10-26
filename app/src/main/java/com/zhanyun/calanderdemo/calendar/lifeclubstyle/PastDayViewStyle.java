package com.zhanyun.calanderdemo.calendar.lifeclubstyle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.zhanyun.calanderdemo.R;
import com.zhanyun.calanderdemo.calendar.CalendarDay;
import com.zhanyun.calanderdemo.calendar.IDayViewStyle;

/**
 * Created by janecer on 2017/9/12 0012
 * des: 需求已过去的日期,已过期的日期，未有课程排班 样式一样
 */

public class PastDayViewStyle extends IDayViewStyle {

    public PastDayViewStyle(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.parseColor("#B9BEBD"));
        mTextPaint.setTextSize(mContext.getResources().getDimension(R.dimen.w_15));
    }

    @Override
    public void drawDayTxt(Canvas canvas, CalendarDay calendarDay, CalendarDay selectDay, String content, float x, float y) {
        canvas.drawText(content, x, y, mTextPaint);
    }

    @Override
    public void drawDayBackground(Canvas canvas, CalendarDay calendarDay, CalendarDay selectDay, int weekDay, float parentWidth, int rowHeight, int rowNum) {

    }
}
