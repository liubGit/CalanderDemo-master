package com.zhanyun.calanderdemo.calendar;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zhanyun.calanderdemo.R;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by coud on 2017/8/1.
 */
public class LifeClubMonthView extends View {

    /**
     * 过去日期，已过期，今天，
     */
    public static final int STATE_PAST = 0 ,STATE_LOST = 1,STATE_TODAY = 2 ,STATE_COURSE = 3,STATE_NO_COURSE = 4 ;

    private final static String TAG = "LifeClubMonthView";
    private final static int DAY_IN_WEEK = 7;
    private final static int DEFAULT_HEIGHT = 32;
    protected static final int DEFAULT_NUM_ROWS = 7;

    private ArrayList<CalendarDay> mDays;
    private CalendarDay mFirstDay;
    private CalendarDay mSelectDay;
    private int mMonthPosition;

    protected int mRowHeight = DEFAULT_HEIGHT;
    private int mNumRows = DEFAULT_NUM_ROWS;
    private int rowNum;
    private int mCurrentMonth;
    private float mCellWidth;
    private float mHalfCellWidth;

    private OnDayClickListener mOnDayClickListener;

    private int textNormalColor;
    private Paint paintNormal;

    private HashMap<String,IDayViewStyle> mViewStyleHashMap ;
    private IDayViewStyle mDefaultViewStyle ;


    public LifeClubMonthView(Context context) {
        this(context, null);
    }

    public LifeClubMonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LifeClubMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
        initPaint();
    }

    private void initPaint() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        textNormalColor = Color.parseColor("#B9BEBD");

        paintNormal = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintNormal.setColor(textNormalColor);
        paintNormal.setTextSize(getResources().getDimension(R.dimen.w_15));
    }

    private void initData() {
        mDays = new ArrayList<>();
        mRowHeight = getResources().getDimensionPixelSize(R.dimen.h_40);
    }

    public void setFirstDay(CalendarDay calendarDay) {
        mFirstDay = calendarDay;
    }

    public void setMonthPosition(int position,IDayViewStyle dayViewStyle,HashMap<String,IDayViewStyle> map) {
        mMonthPosition = position;
        createDays();
        this.mDefaultViewStyle = dayViewStyle ;
        this.mViewStyleHashMap = map ;
        invalidate();
    }

    public void setSelectDay(CalendarDay calendarDay) {
        mSelectDay = calendarDay;
    }

    private void createDays() {
        mDays.clear();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTimeInMillis(mFirstDay.getTime());
        int position = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.roll(Calendar.DAY_OF_MONTH, -(position - 1));
        calendar.add(Calendar.MONTH, mMonthPosition);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        mCurrentMonth = month + 1;

        Calendar lastMonth = (Calendar) calendar.clone();
        int dayDiff = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < dayDiff - 1; i++) {
            lastMonth.add(Calendar.DAY_OF_MONTH, -1);
            mDays.add(0, new CalendarDay(lastMonth));
        }

        Log.e(TAG, month + " yue " + year);
        int daysNum = DayUtils.getDaysInMonth(month, year);
        for (int i = 0; i < daysNum; i++) {
            mDays.add(new CalendarDay(calendar));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        for (; mDays.size() < 42; ) {
            mDays.add(new CalendarDay(calendar));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDays.size() < 28) {
            super.onDraw(canvas);
            return;
        }
        rowNum = 0;
        drawWeekLable(canvas);
        drawMonthNum(canvas);
    }

    private void drawWeekLable(Canvas canvas) {
        String[] weeks = DateFormatSymbols.getInstance().getShortWeekdays();
        for (int i = 0; i < weeks.length; i++) {

            String content = String.valueOf(weeks[i].replace("星期", "").replace("周", ""));
            content = getShortWeekday(content);

            Paint.FontMetrics fontMetrics = paintNormal.getFontMetrics();
            float fontHeight = fontMetrics.bottom - fontMetrics.top;
            float textWidth = paintNormal.measureText(content);

            float y = mRowHeight * rowNum + mRowHeight - (mRowHeight - fontHeight) / 2 - fontMetrics.bottom;
            float x = getResources().getDimension(R.dimen.activity_horizontal_margin)
                    + mCellWidth * (i - 1)
                    + mCellWidth / 2 - textWidth / 2;
            paintNormal.setColor(textNormalColor);
            canvas.drawText(content, x, y, paintNormal);
        }
        rowNum++;
    }

    private void drawMonthNum(Canvas canvas) {
        for (int i = 0; i < mDays.size(); i++) {
            CalendarDay calendarDay = mDays.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(calendarDay.getTime());
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            String content = String.valueOf(calendarDay.day);
            Paint.FontMetrics fontMetrics = paintNormal.getFontMetrics();
            float fontHeight = fontMetrics.bottom - fontMetrics.top;
            float textWidth = paintNormal.measureText(content);
            float parentWidth = getWidth() - 2 * getResources().getDimension(R.dimen.activity_horizontal_margin);
            float y = mRowHeight * rowNum + mRowHeight - (mRowHeight - fontHeight) / 2 - fontMetrics.bottom;
            float x = getResources().getDimension(R.dimen.activity_horizontal_margin)
                    + mCellWidth * (weekDay - 1)
                    + mHalfCellWidth - textWidth / 2;

            //String todayString = new CalendarDay().getDayString();
            IDayViewStyle dayViewStyle = mViewStyleHashMap.get(calendarDay.getDayString()) ;
            if(dayViewStyle == null) {
                mDefaultViewStyle.drawDayBackground(canvas,calendarDay,mSelectDay,weekDay,parentWidth,mRowHeight,rowNum);
                mDefaultViewStyle.drawDayTxt(canvas,calendarDay,mSelectDay,content,x,y);
            } else {
                dayViewStyle.drawDayBackground(canvas,calendarDay,mSelectDay,weekDay,parentWidth,mRowHeight,rowNum);
                dayViewStyle.drawDayTxt(canvas,calendarDay,mSelectDay,content,x,y);
            }

            if (weekDay == 7) {
                rowNum++;
            }
        }
    }



    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mRowHeight * mNumRows);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        float parentWidth = getWidth() - 2 * getResources().getDimension(R.dimen.activity_horizontal_margin);
        mCellWidth = parentWidth / DAY_IN_WEEK;
        mHalfCellWidth = mCellWidth / 2;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            CalendarDay calendarDay = getDayFromLocation(event.getX(), event.getY());
            if(calendarDay != null) {
                IDayViewStyle dayViewStyle = mViewStyleHashMap.get(calendarDay.getDayString());
                if (dayViewStyle == null || !dayViewStyle.isHasClickBg) {//设置了默认的日期风格，或者设置不可点击的标识 不触发日期点击事件
                    return false;
                }

                if (mOnDayClickListener != null ) {
                    mOnDayClickListener.onDayClick(calendarDay);
                }
            }
        }
        return true;
    }

    public CalendarDay getDayFromLocation(float x, float y) {
        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        if (x < padding) {
            return null;
        }

        if (x > getWidth() - padding) {
            return null;
        }

        if (y < mRowHeight || y > (rowNum + 1) * mRowHeight) {
            return null;
        }

        int yDay = (int) (y - mRowHeight) / mRowHeight;

        int xday = (int) ((x - padding) / ((getWidth() - padding * 2) / DAY_IN_WEEK));

        int position = yDay * DAY_IN_WEEK + xday;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mFirstDay.getTime());
        int monthPosition = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.roll(Calendar.DAY_OF_MONTH, -(monthPosition - 1));
        calendar.add(Calendar.MONTH, mMonthPosition);

        return mDays.get(position);
    }

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        mOnDayClickListener = onDayClickListener;
    }

    public String getShortWeekday(String content) {
        switch (content) {
            case "Mon":
                content = "一";
                break;
            case "Tue":
                content = "二";
                break;
            case "Wed":
                content = "三";
                break;
            case "Thu":
                content = "四";
                break;
            case "Fri":
                content = "五";
                break;
            case "Sat":
                content = "六";
                break;
            case "Sun":
                content = "日";
                break;
        }
        return content;
    }

    public interface OnDayClickListener {
        void onDayClick(CalendarDay calendarDay);
    }
}
