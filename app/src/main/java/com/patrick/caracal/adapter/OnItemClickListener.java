package com.patrick.caracal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 15920 on 2016/7/31.
 */
public interface OnItemClickListener {
    void onItemClick(Context context, int position, View view, RecyclerView.ViewHolder viewHolder);
}
