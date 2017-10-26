package com.zhanyun.calanderdemo.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by janecer on 2017/9/11 0011
 * des:
 *     该接口的实现所需要的画笔，由实现改接口的类自行实例化
 */

public abstract class IDayViewStyle {

    protected Paint mTextPaint ;

    protected Paint mCirclePaint ;

    protected Context mContext ;

    protected boolean isHasClickBg = false ;

    public IDayViewStyle(Context context) {
        this.mContext = context ;
        init();
    }

    /**
     * 设置被点击后设置绘制该日期的背景
     * @param flag
     */
    public void setHasClickBg(boolean flag) {
        this.isHasClickBg = flag ;
    }

    public boolean isHasClickBg() {
        return isHasClickBg;
    }

    protected abstract void init();

    public abstract void drawDayTxt(Canvas canvas, CalendarDay calendarDay,CalendarDay selectDay, String content, float x, float y);


    public abstract void drawDayBackground(Canvas canvas, CalendarDay calendarDay,CalendarDay selectDay, int weekDay, float parentWidth,int rowHeight,int rowNum);
}
