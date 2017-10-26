package com.zhanyun.calanderdemo.floattest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhanyun.calanderdemo.R;

/**
 * Created by janecer on 2017/9/23 0023
 * des:
 */

public class FloatActivity extends Activity {

    RecyclerView mRecyclerView ;
    AppBarLayout barLayout ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler) ;
        barLayout = (AppBarLayout) findViewById(R.id.appbarLayout) ;

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int visiblePosition =((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                    if (visiblePosition == 0) {
                        barLayout.setExpanded(true, true) ;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i("scroll","dx : " + dx +"  dy : " + dy) ;

            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new BaseRecViewHolder(new TextView(FloatActivity.this)) ;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                BaseRecViewHolder baseRecViewHolder = (BaseRecViewHolder) holder;
                TextView mTvTxt = (TextView) baseRecViewHolder.getConvertView();
                mTvTxt.setText("__" + position);
            }

            @Override
            public int getItemCount() {
                return 500;
            }
        });
    }
}
