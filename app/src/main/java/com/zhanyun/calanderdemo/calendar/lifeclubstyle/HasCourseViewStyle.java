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
 * Created by janecer on 2017/9/12 0012
 * des: 有课程样式
 */

public class HasCourseViewStyle extends IDayViewStyle {


    Bitmap mSelectBg;

    public HasCourseViewStyle(Context context) {
        super(context);

        setHasClickBg(true);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        mSelectBg = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bg_lifeclub_date_select, options);
    }

    @Override
    protected void init() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.parseColor("#444A49"));
        mTextPaint.setTextSize(mContext.getResources().getDimension(R.dimen.w_15));

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.parseColor("#08AEEA"));
        mCirclePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void drawDayTxt(Canvas canvas, CalendarDay calendarDay, CalendarDay selectDay, String content, float x, float y) {
        if (selectDay.getDayString().equals(calendarDay.getDayString())) {
            mTextPaint.setColor(mContext.getResources().getColor(android.R.color.white));
        } else {
            mTextPaint.setColor(Color.parseColor("#444A49"));
        }
        canvas.drawText(content, x, y, mTextPaint);
    }

    @Override
    public void drawDayBackground(Canvas canvas, CalendarDay calendarDay, CalendarDay selectDay, int weekDay, float parentWidth, int rowHeight, int rowNum) {
        if (selectDay.getDayString().equals(calendarDay.getDayString())) {//被选中的日期背景
            float x, y;
            x = mContext.getResources().getDimension(R.dimen.activity_horizontal_margin) + parentWidth / 7 * (weekDay - 1) + parentWidth / 7 / 2;
            y = rowHeight * rowNum + rowHeight / 2;
            canvas.drawBitmap(mSelectBg, x - (mSelectBg.getWidth() / 2), y - (mSelectBg.getHeight() / 2), null);
//            radus = rowHeight * 2 / 6;
//            canvas.drawCircle(
//                    x,
//                    y,
//                    radus,
//                    mCirclePaint
//            );
        }
    }
}
