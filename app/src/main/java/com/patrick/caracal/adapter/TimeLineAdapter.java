package com.patrick.caracal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.patrick.caracal.R;
import com.patrick.caracal.model.Trace;
import com.patrick.caracal.ui.view.TimelineView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Patrick on 16/8/4.
 */
public class TimeLineAdapter extends RealmRecyclerViewAdapter<Trace,TimeLineAdapter.ViewHolder> {

    public static final int TYPE_HEADER = 99;

    public static final int TYPE_TIMELINE = 1;

    //头部
    private View headerView;

    public TimeLineAdapter(Context context, OrderedRealmCollection<Trace> traces,View headerView) {
        super(context, traces, true);
        this.headerView = headerView;
    }

    @Override
    public int getItemCount() {
        //加上header
        return super.getItemCount()+1;
    }

    @Override
    public int getItemViewType(int position) {
//        if (headerView == null) return TimelineView.getTimeLineViewType(position,getItemCount());
        if (position == 0) return TYPE_HEADER;

        return TimelineView.getTimeLineViewType(position -1,getItemCount()-1);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headerView != null && viewType == TYPE_HEADER){
            return new ViewHolder(headerView);
        }

        View view = inflater.inflate(R.layout.item_details_timeline,parent,false);
        return new ViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position == 0) return;      //header holder 忽略

        Trace trace = getData().get(position-1);
        holder.acceptStation.setText(trace.acceptStation);
        holder.acceptTime.setText(trace.acceptTime);

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.acceptStation)
        public TextView acceptStation;

        @BindView(R.id.acceptTime)
        public TextView acceptTime;

        @BindView(R.id.timeline)
        public TimelineView timeline;

        public ViewHolder(View itemView,int lineType) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            timeline.initLine(lineType);
        }

        public ViewHolder(View headView){
            super(headView);

        }
    }
}
