package com.zhanyun.calanderdemo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zhanyun.calanderdemo.calendar.CalendarDay;
import com.zhanyun.calanderdemo.calendar.DayUtils;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by coud on 2017/8/1.
 */
public class MonthView extends View {

    private final static String TAG = "MonthView";
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

    private int todayCircleColor;
    private int selectCircleColor;
    private int textTaskColor;
    private int textNormalColor;
    private int textTodayColor;

    private Paint paintTodaCircle;
    private Paint paintSelectCircle;
    private Paint paintTask;
    private Paint paintNormal;
    private Paint paintOther;
    private Paint paintToday;

    private Map<String, Integer> taskDateMap;


    public MonthView(Context context) {
        this(context, null);
    }

    public MonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
        initPaint();
    }

    private void initPaint() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        todayCircleColor = getContext().getResources().getColor(android.R.color.transparent) ;//Color.parseColor("#009FFF");
        selectCircleColor = Color.parseColor("#00B99E");
        textTaskColor = Color.parseColor("#444A49");
        textNormalColor = Color.parseColor("#B9BEBD");
        textTodayColor = Color.parseColor("#00B99E");

        paintTodaCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTodaCircle.setColor(todayCircleColor);
        paintTodaCircle.setStyle(Paint.Style.FILL);

        paintSelectCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintSelectCircle.setColor(selectCircleColor);
        paintSelectCircle.setStyle(Paint.Style.FILL);

        paintTask = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTask.setColor(textTaskColor);
        paintTask.setTextSize(getResources().getDimension(R.dimen.w_16));

        paintNormal = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintNormal.setColor(textNormalColor);
        paintNormal.setTextSize(getResources().getDimension(R.dimen.w_16));

        paintOther = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintOther.setColor(textNormalColor);
        paintOther.setTextSize(getResources().getDimension(R.dimen.w_16));
        //paintOther.setAlpha((int) (255 * 0.6));

        paintToday = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintToday.setColor(textTodayColor);
        paintToday.setTextSize(getResources().getDimension(R.dimen.w_16));
    }

    private void initData() {
        mDays = new ArrayList<>();
        taskDateMap = new HashMap<>();
//        mRowHeight = getResources().getDimensionPixelSize(R.dimen.default_month_row_height);
        mRowHeight = getResources().getDimensionPixelSize(R.dimen.h_40);
    }

    public void setFirstDay(CalendarDay calendarDay) {
        mFirstDay = calendarDay;
    }

    public void setMonthPosition(int position, Map<String, Integer> taskDayList) {
        mMonthPosition = position;
        createDays();
        taskDateMap = taskDayList;
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

            String todayString = new CalendarDay().getDayString();
            if (todayString.equals(calendarDay.getDayString())) {
                drawTodayBackground(canvas, calendarDay, weekDay, parentWidth);
                drawTodayText(canvas, calendarDay, paintNormal, content, x, y);
            } else {
                drawOtherBackground(canvas, calendarDay, weekDay, parentWidth);
                drawOtherText(canvas, calendarDay, paintNormal, content, x, y);
//                drawPhoto(canvas, calendarDay, weekDay);
            }

            if (weekDay == 7) {
                rowNum++;
            }
        }
    }

    private void drawTodayText(Canvas canvas, CalendarDay calendarDay, Paint paintNormal, String content, float x, float y) {
        Integer status = taskDateMap.get(calendarDay.getDayString());
        if (status == null) {
            paintNormal.setColor(textTodayColor);
            canvas.drawText(content, x, y, paintNormal);
        } else {
            switch (status) {
                case 1://已跑
                case 2://过期未跑
//                    paintNormal.setColor(textNormalColor);
//                    canvas.drawText(content, x, y, calendarDay.getMonth() == mCurrentMonth ? paintNormal : paintOther);
                    break;
                case 0://未跑
                default:
                    paintNormal.setColor(textTodayColor);
                    canvas.drawText(content, x, y, paintNormal);
                    break;
            }
        }
    }

    private void drawOtherText(Canvas canvas, CalendarDay calendarDay, Paint paintNormal, String content, float x, float y) {
        Integer status = taskDateMap.get(calendarDay.getDayString());
        if (status == null) {
            paintNormal.setColor(textNormalColor);
            canvas.drawText(content, x, y, calendarDay.getMonth() == mCurrentMonth ? paintNormal : paintOther);
        } else {
            switch (status) {
                case 1://已跑
                case 2://过期未跑
                    paintNormal.setColor(textNormalColor);
                    canvas.drawText(content, x, y, calendarDay.getMonth() == mCurrentMonth ? paintNormal : paintOther);
                    break;
                case 0://未跑
                default:
                    paintNormal.setColor(textTaskColor);
                    canvas.drawText(content, x, y, paintNormal);
                    break;
            }
        }
    }

    private void drawTodayBackground(Canvas canvas, CalendarDay calendarDay, int weekDay, float parentWidth) {
        Integer status = taskDateMap.get(calendarDay.getDayString());
        if (status != null && status == 1) {
//            canvas.drawBitmap(finishBitmap, getResources().getDimension(R.dimen.activity_horizontal_margin)
//                    + mCellWidth * (weekDay - 1) + mHalfCellWidth - finishBitmap.getWidth() / 2, mRowHeight * rowNum, null);
        } else {
            canvas.drawCircle(getResources().getDimension(R.dimen.activity_horizontal_margin)
                    + parentWidth / DAY_IN_WEEK * (weekDay - 1)
                    + parentWidth / DAY_IN_WEEK / 2, mRowHeight * rowNum + mRowHeight / 2, mRowHeight * 2 / 5, paintTodaCircle);
        }
    }

    private void drawOtherBackground(Canvas canvas, CalendarDay calendarDay, int weekDay, float parentWidth) {
        Integer status = taskDateMap.get(calendarDay.getDayString());
        if (status != null) {
            if (status != 1  && status!=2) {
                if (mSelectDay.getDayString().equals(calendarDay.getDayString())) {
                    canvas.drawCircle(getResources().getDimension(R.dimen.activity_horizontal_margin)
                            + parentWidth / DAY_IN_WEEK * (weekDay - 1)
                            + parentWidth / DAY_IN_WEEK / 2, mRowHeight * rowNum + mRowHeight / 2, mRowHeight * 2 / 5, paintSelectCircle);
                }
            }
        } else {
            if (mSelectDay.getDayString().equals(calendarDay.getDayString())) {
                canvas.drawCircle(getResources().getDimension(R.dimen.activity_horizontal_margin)
                        + parentWidth / DAY_IN_WEEK * (weekDay - 1)
                        + parentWidth / DAY_IN_WEEK / 2, mRowHeight * rowNum + mRowHeight / 2, mRowHeight * 2 / 5, paintSelectCircle);
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
            if (mOnDayClickListener != null && calendarDay != null) {
                mOnDayClickListener.onDayClick(calendarDay);
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
            case "一":
                content = "Man";
                break;
            case "二":
                content = "Tue";
                break;
            case "三":
                content = "Wed";
                break;
            case "四":
                content = "Thu";
                break;
            case "五":
                content = "Fri";
                break;
            case "六":
                content = "Sat";
                break;
            case "日":
                content = "Sun";
                break;
        }
        return content;
    }

    public interface OnDayClickListener {
        void onDayClick(CalendarDay calendarDay);
    }
}
