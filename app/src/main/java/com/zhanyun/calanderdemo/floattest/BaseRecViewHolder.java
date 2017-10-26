package com.zhanyun.calanderdemo.floattest;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by janecer on 2017/9/23 0023
 * des:
 */

public class BaseRecViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> viewHolder;
    private View view;

    public BaseRecViewHolder(View view) {
        super(view);
        this.view = view;
        this.viewHolder = new SparseArray();
    }

    public View get(int id) {
        View childView = (View)this.viewHolder.get(id);
        if(childView == null) {
            childView = this.view.findViewById(id);
            this.viewHolder.put(id, childView);
        }
        return childView;
    }

    public View getConvertView() {
        return this.view;
    }

    public View getView(int id) {
        return this.get(id);
    }

    public TextView getTextView(int id) {
        return (TextView)this.get(id);
    }

    public Button getButton(int id) {
        return (Button)this.get(id);
    }

    public ImageView getImageView(int id) {
        return (ImageView)this.get(id);
    }

    public void setTextView(int id, CharSequence charSequence) {
        this.getTextView(id).setText(charSequence);
    }


}
