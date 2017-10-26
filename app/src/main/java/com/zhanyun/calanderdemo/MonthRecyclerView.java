package com.zhanyun.calanderdemo;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by coud on 2017/8/1.
 */
public class MonthRecyclerView extends RecyclerView {

    public interface OnPageChangedListener {
        void onChanged(int position);
    }

    public static int LIST_LEFT_OFFSET = -1;
    private LinearLayoutManager mManager;

    public void setOnPageChangedListener(OnPageChangedListener onPageChangedListener) {
        this.onPageChangedListener = onPageChangedListener;
    }

    private OnPageChangedListener onPageChangedListener;

    public MonthRecyclerView(Context context) {
        this(context, null);
    }

    public MonthRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initData();
    }

    private void initData() {
        mManager = new LinearLayoutManager(getContext());
        mManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(mManager);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                adjustPosition(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return super.onTouchEvent(e);
    }

    private void adjustPosition(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            int i = 0;
            View child = recyclerView.getChildAt(i);
            while (child != null && child.getRight() <= 0) {
                child = recyclerView.getChildAt(++i);
            }
            if (child == null) {
                // The view is no longer visible, just return
                return;
            }
            final int left = child.getLeft();
            final int right = child.getRight();
            final int midpoint = recyclerView.getWidth() / 2;
            if (left < LIST_LEFT_OFFSET) {
                if (right > midpoint) {
                    recyclerView.smoothScrollBy(left, 0);
                } else {
                    recyclerView.smoothScrollBy(right, 0);
                }
            }

            int firstVisiblePosition = mManager.findFirstVisibleItemPosition();
            if (onPageChangedListener != null) {
                onPageChangedListener.onChanged(firstVisiblePosition);
            }
        }
    }

}
