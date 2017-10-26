package com.zhanyun.calanderdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhanyun.calanderdemo.calendar.CalendarDay;
import com.zhanyun.calanderdemo.calendar.DayUtils;
import com.zhanyun.calanderdemo.calendar.IDayViewStyle;
import com.zhanyun.calanderdemo.calendar.LifeClubMonthView;
import com.zhanyun.calanderdemo.calendar.lifeclubstyle.PastDayViewStyle;

import java.util.HashMap;

/**
 * Created by coud on 2017/8/1.
 */
public class MonthViewAdapter extends RecyclerView.Adapter<MonthViewAdapter.MonthViewHolder> implements LifeClubMonthView.OnDayClickListener {

    private Context mContext;
    private CalendarDay mStartDay;
    private CalendarDay mEndDay;
    private CalendarDay mSelectCalendarDay;
    private LifeClubMonthView.OnDayClickListener mOnDayClickListener;
    private HashMap<String,IDayViewStyle>  taskDateMap;

    public MonthViewAdapter(Context context, LifeClubMonthView.OnDayClickListener onDayClickListener) {
        mContext = context;
        mOnDayClickListener = onDayClickListener;
        mSelectCalendarDay = new CalendarDay(System.currentTimeMillis());
        taskDateMap = new HashMap<>();
    }

    public void setData(CalendarDay startDay, CalendarDay endDay, HashMap<String,IDayViewStyle>  taskDateMap) {
        mStartDay = startDay;
        mEndDay = endDay;
        this.taskDateMap = taskDateMap;
        notifyDataSetChanged();
    }

    public CalendarDay getStartDay() {
        if (mStartDay == null) {
            try {
                throw new Exception("The StartDay must initial before the select Day!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return mStartDay;
        }
        return null;
    }

    public void setSelectDay(CalendarDay calendarDay) {
        if (calendarDay == null) return;
        mSelectCalendarDay = calendarDay;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mStartDay == null || mEndDay == null) {
            return 0;
        }
        int monthCount = DayUtils.calculateMonthCount(mStartDay, mEndDay);
        return monthCount;
    }

    @Override
    public MonthViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LifeClubMonthView monthView = new LifeClubMonthView(mContext);
        monthView.setOnDayClickListener(this);
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
                ViewGroup.LayoutParams.MATCH_PARENT);
        monthView.setLayoutParams(params);
        MonthViewHolder viewHolder = new MonthViewHolder(monthView, mStartDay);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MonthViewHolder viewHolder, final int position) {
        viewHolder.bind(position, mSelectCalendarDay, taskDateMap);
    }

    @Override
    public void onDayClick(CalendarDay calendarDay) {
        mSelectCalendarDay = calendarDay;
        if (mOnDayClickListener != null) {
            mOnDayClickListener.onDayClick(calendarDay);
        }
        notifyDataSetChanged();
    }

    public void setOnDayClickListener(LifeClubMonthView.OnDayClickListener onDayClickListener) {
        mOnDayClickListener = onDayClickListener;
    }

    public static class MonthViewHolder extends RecyclerView.ViewHolder {

        LifeClubMonthView monthView;

        public MonthViewHolder(View view, CalendarDay startDay) {
            super(view);
            monthView = (LifeClubMonthView) view;
            monthView.setFirstDay(startDay);
        }

        public void bind(int position, CalendarDay calendarDay, HashMap<String,IDayViewStyle>  taskDayList) {
            monthView.setSelectDay(calendarDay);
            monthView.setMonthPosition(position,new PastDayViewStyle(monthView.getContext()), taskDayList);
        }

    }
}
