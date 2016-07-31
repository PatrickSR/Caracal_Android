package com.patrick.caracal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 15920 on 2016/7/31.
 */
public interface OnItemClickListener {
    void onItemClick(int position, View view, RecyclerView.ViewHolder viewHolder);
}
