package com.zhanyun.calanderdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhanyun.calanderdemo.calendar.CalendarDay;
import com.zhanyun.calanderdemo.calendar.DayUtils;
import com.zhanyun.calanderdemo.calendar.lifeclubstyle.HasCourseViewStyle;
import com.zhanyun.calanderdemo.calendar.IDayViewStyle;
import com.zhanyun.calanderdemo.calendar.LifeClubMonthView;
import com.zhanyun.calanderdemo.calendar.lifeclubstyle.PastDayViewStyle;
import com.zhanyun.calanderdemo.calendar.lifeclubstyle.TodayViewStyle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MonthRecyclerView mrv ;
    private MonthViewAdapter monthViewAdapter;
    CalendarDay selectDay = new CalendarDay(System.currentTimeMillis());
    private int mCurrentMonthPos,mStartMonthPos,mEndMonthPos;
    TextView mTvDate ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mrv = (MonthRecyclerView) findViewById(R.id.mrv) ;
        mTvDate = (TextView) findViewById(R.id.tv_date) ;
        findViewById(R.id.iv_last).setOnClickListener(this);
        findViewById(R.id.iv_next).setOnClickListener(this);


        initMonthRecyclerView();

        long mouthTime = 1000 * 60 * 60 * 24 * 30 ;
        CalendarDay endDay = new CalendarDay(System.currentTimeMillis()) ;
        CalendarDay startDay = new CalendarDay(endDay.calendar.getTimeInMillis() + 2 * mouthTime) ;
        HashMap<String,IDayViewStyle> map = new HashMap<>() ;
        long time = System.currentTimeMillis() ;
        String todayStr, dayStr ;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
        todayStr = sdf.format(new Date()) ;
        for (int i =0 ; i < 50 ; i++) {
            dayStr = sdf.format(time + i* (1000 * 60 * 60 * 24)) ;
            if(todayStr .equals(dayStr)){
                map.put(dayStr,new TodayViewStyle(getApplicationContext())) ;
                continue;
            }
            map.put(dayStr,i % 2 == 0 ? new HasCourseViewStyle(getApplicationContext()) : new PastDayViewStyle(getApplicationContext())) ;
        }
        setDate(startDay,endDay,map);
    }

    private void initMonthRecyclerView() {
        monthViewAdapter = new MonthViewAdapter(this, new LifeClubMonthView.OnDayClickListener() {
            @Override
            public void onDayClick(CalendarDay calendarDay) {
                selectDay = calendarDay;
//                if (motionSchemeInfoMap.containsKey(calendarDay.getDayString())){
//                    showTaskDetail(motionSchemeInfoMap.get(calendarDay.getDayString()).getSchemeDetail());
//                }else {
//                    showNoTaskDetail();
//                }
//                tvDate.setText(getStringSelectDay(calendarDay));
                Toast.makeText(getApplicationContext(),"" + selectDay.getDayString(),Toast.LENGTH_SHORT).show();
            }
        });
        mrv.setAdapter(monthViewAdapter);
        mrv.setOnPageChangedListener(new MonthRecyclerView.OnPageChangedListener() {
            @Override
            public void onChanged(int position) {
                onMonthChange(position);
            }
        });
    }


    public void setDate(CalendarDay startDay, CalendarDay endDay,HashMap<String,IDayViewStyle> map) {
        monthViewAdapter.setData(startDay, endDay, map);
        CalendarDay today = new CalendarDay(System.currentTimeMillis()) ;

        mStartMonthPos = DayUtils.calculateMonthPosition(startDay,startDay) ;
        mEndMonthPos = DayUtils.calculateMonthPosition(startDay,endDay) ;
        int position = DayUtils.calculateMonthPosition(startDay, today);
        mrv.scrollToPosition(position);
        onMonthChange(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_last:
                if(mCurrentMonthPos <= mStartMonthPos) {
                    return;
                }
                mrv.smoothScrollToPosition(--mCurrentMonthPos);
                break;
            case R.id.iv_next:
                if(mCurrentMonthPos >= mEndMonthPos) {
                    return;
                }
                mrv.smoothScrollToPosition(++mCurrentMonthPos);
                break;
        }
    }

    private void onMonthChange(int position) {
        this.mCurrentMonthPos = position ;
        try {
            CalendarDay startDay = monthViewAdapter.getStartDay();
            int daysInMonth = DayUtils.getDaysInMonth(startDay.month, startDay.year);
            CalendarDay calendarDay1 = new CalendarDay(startDay.year, startDay.month + 1, daysInMonth > selectDay.day ? selectDay.day : daysInMonth);
            CalendarDay calendarDay = DayUtils.calculatePositionDay(calendarDay1, position);
            String mouth = String.valueOf(calendarDay.getMonth()) ;
            mTvDate.setText(calendarDay.getYear() + "年" + ((mouth.length() == 1) ? "0" + mouth : mouth) +"月");
//                   monthViewAdapter.setSelectDay(calendarDay);
//                    if (motionSchemeInfoMap.containsKey(calendarDay.getDayString())){
//                        showTaskDetail(motionSchemeInfoMap.get(calendarDay.getDayString()).getSchemeDetail());
//                    }else {
//                        showNoTaskDetail();
//                    }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
