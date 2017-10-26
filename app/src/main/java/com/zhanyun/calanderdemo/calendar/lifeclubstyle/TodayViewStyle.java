package com.zhanyun.calanderdemo.calendar.lifeclubstyle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.zhanyun.calanderdemo.R;
import com.zhanyun.calanderdemo.calendar.CalendarDay;
import com.zhanyun.calanderdemo.calendar.IDayViewStyle;

/**
 * Created by janecer on 2017/9/11 0011
 * des: 日期状态为今天
 */

public class TodayViewStyle extends IDayViewStyle {


    Bitmap mSelectBg;

    public TodayViewStyle(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.parseColor("#00B99E"));
        mTextPaint.setTextSize(mContext.getResources().getDimension(R.dimen.w_15));

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.parseColor("#08AEEA"));
        mCirclePaint.setStyle(Paint.Style.FILL);

        setHasClickBg(true);
    }

    @Override
    public void setHasClickBg(boolean flag) {
        super.setHasClickBg(flag);

        if (isHasClickBg) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            mSelectBg = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.bg_lifeclub_date_select, options);
        }
    }

    @Override
    public void drawDayTxt(Canvas canvas, CalendarDay calendarDay, CalendarDay selectDay, String content, float x, float y) {
        if (isHasClickBg) {
            if (selectDay.getDayString().equals(calendarDay.getDayString())) {
                mTextPaint.setColor(mContext.getResources().getColor(android.R.color.white));
            } else {
                mTextPaint.setColor(Color.parseColor("#00B99E"));
            }
        }
        canvas.drawText(content, x, y, mTextPaint);
    }

    @Override
    public void drawDayBackground(Canvas canvas, CalendarDay calendarDay, CalendarDay selectDay, int weekDay, float parentWidth, int rowHeight, int rowNum) {
        if (!isHasClickBg) {
            return;
        }
        if (selectDay.getDayString().equals(calendarDay.getDayString())) {//被选中的日期背景
//            canvas.drawCircle(mContext.getResources().getDimension(R.dimen.activity_horizontal_margin)
//                    + parentWidth / 7 * (weekDay - 1)
//                    + parentWidth / 7 / 2, rowHeight * rowNum + rowHeight / 2, rowHeight * 2 / 5, mCirclePaint);
            float x, y;
            x = mContext.getResources().getDimension(R.dimen.activity_horizontal_margin) + parentWidth / 7 * (weekDay - 1) + parentWidth / 7 / 2;
            y = rowHeight * rowNum + rowHeight / 2;
            canvas.drawBitmap(mSelectBg, x - mSelectBg.getWidth() / 2, y - mSelectBg.getHeight() / 2, null);
        }
    }
}
